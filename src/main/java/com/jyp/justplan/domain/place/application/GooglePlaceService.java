package com.jyp.justplan.domain.place.application;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.city.domain.CityRepository;
import com.jyp.justplan.domain.place.domain.GooglePlace;
import com.jyp.justplan.domain.place.domain.GooglePlaceRepository;
import com.jyp.justplan.domain.place.GooglePlacesProperties;
import com.jyp.justplan.domain.place.dto.request.GooglePlaceRequest;
import com.jyp.justplan.domain.place.dto.response.GooglePlaceApiResultResponse;
import com.jyp.justplan.domain.place.dto.response.GooglePlaceResponse;
import com.jyp.justplan.domain.place.dto.response.GooglePlacesSearchApiResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.stream.Collectors;

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
            .map(GooglePlaceResponse::of).toList();

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

//    public GooglePlaceResponse convertToGooglePlaceResponse(GooglePlaceApiResultResponse result) {
//        String firstType = result.getTypes() != null && !result.getTypes().isEmpty()
//            ? result.getTypes().get(0) : null;
//
//        String photoReference = result.getPhotos() != null && !result.getPhotos().isEmpty()
//                ? result.getPhotos().get(0).getPhotoReference() : null;
//
//        return new GooglePlaceResponse(
//                result.getId(),
//                result.getName(),
//                result.getFormattedAddress(),
//                firstType,
//                result.getGeometry().getLocation().getLat(),
//                result.getGeometry().getLocation().getLng(),
//                photoReference
//        );
//    }

    /*CREATE*/
    @Transactional
    public GooglePlaceResponse createGooglePlace(GooglePlaceRequest googlePlaceRequest) {

        GooglePlace googlePlace = googlePlaceRequest.toEntity().toBuilder()
                .build();

        GooglePlace savedGooglePlace = googlePlaceRepository.save(googlePlace);
        return GooglePlaceResponse.of(savedGooglePlace);
    }

    private boolean isSamePlace(GooglePlaceResponse googlePlace, GooglePlace localPlace) {
        // 위도와 경도를 비교하여 장소가 같은지 확인하는 로직 구현
        double threshold = 0.0001; // 일치 여부를 판단하기 위한 임계값
        return Math.abs(googlePlace.getLatitude() - localPlace.getLatitude()) < threshold
            && Math.abs(googlePlace.getLongitude() - localPlace.getLongitude()) < threshold;
    }
}
