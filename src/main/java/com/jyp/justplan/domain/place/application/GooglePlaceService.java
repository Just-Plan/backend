package com.jyp.justplan.domain.place.application;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.city.domain.CityRepository;
import com.jyp.justplan.domain.place.GooglePlacesProperties;
import com.jyp.justplan.domain.place.domain.GooglePlace;
import com.jyp.justplan.domain.place.domain.GooglePlaceRepository;
import com.jyp.justplan.domain.place.dto.response.GooglePlaceResponse;
import com.jyp.justplan.domain.place.dto.response.GooglePlacesSearchApiResponse;
import com.jyp.justplan.domain.place.dto.response.GooglePlacesSearchApiResponse.GooglePlaceApiResultResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GooglePlaceService {

    private final GooglePlaceRepository googlePlaceRepository;
    private final GooglePlacesProperties googlePlacesProperties;
    private final CityRepository cityRepository;
    private final WebClient webClient;

    @Autowired
    public GooglePlaceService(
        GooglePlaceRepository googlePlaceRepository, GooglePlacesProperties googlePlacesProperties, WebClient.Builder webClientBuilder, CityRepository cityRepository
    ) {
        this.googlePlaceRepository = googlePlaceRepository;
        this.googlePlacesProperties = googlePlacesProperties;
        this.cityRepository = cityRepository;
        this.webClient = webClientBuilder.baseUrl("https://maps.googleapis.com/maps/api/place").build();
    }

    @Transactional(readOnly = true)
    public Flux<GooglePlaceResponse> getGooglePlace(String textSearch, Long cityId) {
        return findCityById(cityId)
            .flatMapMany(city -> fetchPlacesFromGoogle(textSearch, city))
            .flatMap(this::createGooglePlaceResponse)
            .flatMap(response -> mergeWithLocalPlaces(Flux.just(response), cityId));
    }

    private Mono<City> findCityById(Long cityId) {
        return Mono.fromCallable(() -> cityRepository.findById(cityId))
            .flatMap(optionalCity -> optionalCity
                .map(Mono::just)
                .orElseGet(() -> Mono.error(new RuntimeException("도시 아이디가 틀립니다:" + cityId)))
            );
    }

    private Flux<GooglePlaceApiResultResponse> fetchPlacesFromGoogle(String textSearch, City city) {
        String apiKey = googlePlacesProperties.getApiKey();
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/textsearch/json")
                .queryParam("query", textSearch)
                .queryParam("key", apiKey)
                .queryParam("language", "ko")
                .queryParam("location", city.getLatitude() + "," + city.getLongitude())
                .build())
            .retrieve()
            .bodyToMono(GooglePlacesSearchApiResponse.class)
            .flatMapMany(response -> Flux.fromIterable(response.getResults()));
    }

    private Mono<GooglePlaceResponse> createGooglePlaceResponse(GooglePlaceApiResultResponse result) {
        if (result.getPhotos() != null && !result.getPhotos().isEmpty()) {
            String photoReference = result.getPhotos().get(0).getPhotoReference();
            return fetchPhotoUrl(photoReference, 400)
                .map(photoUrl -> GooglePlaceResponse.of(result, photoUrl))
                .defaultIfEmpty(GooglePlaceResponse.of(result, null));
        } else {
            return Mono.just(GooglePlaceResponse.of(result, null));
        }
    }

    private Flux<GooglePlaceResponse> mergeWithLocalPlaces(Flux<GooglePlaceResponse> googleResponses, Long cityId) {
        List<GooglePlace> localPlaces = googlePlaceRepository.findByCityId(cityId);

        return googleResponses.flatMap(googleResponse ->
            Flux.fromIterable(localPlaces)
                .filter(localPlace -> isSamePlace(googleResponse, localPlace))
                .next()
                .map(GooglePlaceResponse::of) // 일치하는 로컬 DB의 정보로 교체
                .defaultIfEmpty(googleResponse)
        );
    }

    private Mono<String> fetchPhotoUrl(String photoReference, int maxwidth) {
        String url = String.format(
            "/photo?maxwidth=%d&photoreference=%s&key=%s",
            maxwidth, photoReference, googlePlacesProperties.getApiKey());

        return webClient
            .get()
            .uri(url)
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.FOUND)) { // 302 리디렉션 확인
                    return Mono.just(response.headers().header("Location").get(0)); // 리디렉션 URL 추출
                }
                return Mono.error(new RuntimeException("Failed to fetch photo URL"));
            });
    }

    private boolean isSamePlace(GooglePlaceResponse googlePlace, GooglePlace localPlace) {
        // 위도와 경도를 비교하여 장소가 같은지 확인하는 로직 구현
        double threshold = 0.0001;
        return Math.abs(googlePlace.getLatitude() - localPlace.getLatitude()) < threshold
            && Math.abs(googlePlace.getLongitude() - localPlace.getLongitude()) < threshold;
    }
}