package com.jyp.justplan.domain.place.application;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.city.domain.CityRepository;
import com.jyp.justplan.domain.mbti.domain.MbtiType;
import com.jyp.justplan.domain.place.GooglePlacesProperties;
import com.jyp.justplan.domain.place.domain.GooglePlace;
import com.jyp.justplan.domain.place.domain.GooglePlaceRepository;
import com.jyp.justplan.domain.place.domain.GooglePlaceStatsRepository;
import com.jyp.justplan.domain.place.dto.response.GooglePlaceResponse;
import com.jyp.justplan.domain.place.dto.response.GooglePlacesSearchApiResponse;
import com.jyp.justplan.domain.place.dto.response.GooglePlacesSearchApiResponse.GooglePlaceApiResultResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class GooglePlaceService {

    private final GooglePlaceRepository googlePlaceRepository;
    private final GooglePlacesProperties googlePlacesProperties;
    private final GooglePlaceStatsRepository googlePlaceStatsRepository;
    private final CityRepository cityRepository;
    private final WebClient webClient;

    @Autowired
    public GooglePlaceService(
        GooglePlaceRepository googlePlaceRepository,
        GooglePlacesProperties googlePlacesProperties,
        WebClient.Builder webClientBuilder,
        CityRepository cityRepository,
        GooglePlaceStatsRepository googlePlaceStatsRepository
    ) {
        this.googlePlaceRepository = googlePlaceRepository;
        this.googlePlacesProperties = googlePlacesProperties;
        this.cityRepository = cityRepository;
        this.webClient = webClientBuilder.baseUrl("https://maps.googleapis.com/maps/api/place").build();
        this.googlePlaceStatsRepository = googlePlaceStatsRepository;
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
                .flatMap(localPlace -> Flux.fromIterable(googlePlaceStatsRepository.findAllByGooglePlaceId(localPlace.getId()))
                    .map(googlePlaceStats -> googlePlaceStats.getMbti().getMbti())
                    .map(MbtiType::valueOf)
                    .collectList()
                    .map(mbtiList -> GooglePlaceResponse.of(localPlace, mbtiList))
                )
                .defaultIfEmpty(googleResponse)
        );
    }

    public Mono<String> fetchPhotoUrl(String photoReference, int maxwidth) {
        String url = String.format(
            "/photo?maxwidth=%d&photoreference=%s&key=%s",
            maxwidth, photoReference, googlePlacesProperties.getApiKey());

        return webClient
            .get()
            .uri(url)
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.FOUND)) {
                    return Mono.just(response.headers().header("Location").get(0));
                }
                return Mono.error(new RuntimeException("Failed to fetch photo URL"));
            });
    }

    private boolean isSamePlace(GooglePlaceResponse googlePlace, GooglePlace localPlace) {
        double threshold = 0.0001;
        return Math.abs(googlePlace.getLatitude() - localPlace.getLatitude()) < threshold
            && Math.abs(googlePlace.getLongitude() - localPlace.getLongitude()) < threshold;
    }
}