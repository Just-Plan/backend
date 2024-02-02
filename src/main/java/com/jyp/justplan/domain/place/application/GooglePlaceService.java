package com.jyp.justplan.domain.place.application;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.city.domain.CityRepository;
import com.jyp.justplan.domain.place.domain.GooglePlace;
import com.jyp.justplan.domain.place.domain.GooglePlaceRepository;
import com.jyp.justplan.domain.place.GooglePlacesProperties;
import com.jyp.justplan.domain.place.dto.response.GooglePlaceResponse;
import com.jyp.justplan.domain.place.dto.response.GooglePlacesSearchApiResponse;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
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
    public List<GooglePlaceResponse> getGooglePlace(String textSearch, Long cityId) {
        String apiKey = googlePlacesProperties.getApiKey();
        City findCity = cityRepository.findById(cityId).orElseThrow(() -> new RuntimeException("City not found"));

        // 구글 지도 API에서 검색
        var googlePlacesSearchApiResponse = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/textsearch/json")
                .queryParam("query", textSearch)
                .queryParam("key", apiKey)
                .queryParam("language", "ko") // 한국어 설정
                .queryParam("location", findCity.getLatitude() + "," + findCity.getLongitude()) // 검색 위치 설정
                .build())
            .retrieve()
            .bodyToMono(GooglePlacesSearchApiResponse.class)
            .block();


        // 구글 검색 결과를 GooglePlaceResponse로 변환
        List<GooglePlaceResponse> googleResponses = googlePlacesSearchApiResponse.getResults().stream()
            .map(result -> {
                if (result.getPhotos() != null && !result.getPhotos().isEmpty()) {
                    String photoReference = result.getPhotos().get(0).getPhotoReference();
                    // 비동기 작업을 동기화하여 처리
                    String photoUrl = fetchPhotoUrl(photoReference, 400).block();
                    return GooglePlaceResponse.of(result, photoUrl);
                } else {
                    return GooglePlaceResponse.of(result, null);
                }
            }).toList();

        // 데이터베이스에서 cityId에 해당하는 모든 장소 검색
        List<GooglePlace> localPlaces = googlePlaceRepository.findByCityId(cityId);

        // 구글 검색 결과와 로컬 DB의 장소를 위도, 경도로 비교하여 같은 장소가 있으면 로컬 DB 정보로 교체
        List<GooglePlaceResponse> combinedResponses = new ArrayList<>();
        googleResponses.forEach(googlePlace -> {
            Optional<GooglePlace> matchingPlace = localPlaces.stream()
                .filter(localPlace -> isSamePlace(googlePlace, localPlace))
                .findFirst();

            if (matchingPlace.isPresent()) {
                // 일치하는 장소를 찾으면 로컬 DB의 정보로 교체
                combinedResponses.add(0, GooglePlaceResponse.of(matchingPlace.get())); // 맨 앞에 추가
            } else {
                combinedResponses.add(googlePlace); // 일치하는 장소가 없으면 구글 검색 결과 추가
            }
        });
        return combinedResponses;
    }

    public Mono<String> fetchPhotoUrl(String photoReference, int maxwidth) {
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
        double threshold = 0.0001; // 일치 여부를 판단하기 위한 임계값
        return Math.abs(googlePlace.getLatitude() - localPlace.getLatitude()) < threshold
            && Math.abs(googlePlace.getLongitude() - localPlace.getLongitude()) < threshold;
    }
}