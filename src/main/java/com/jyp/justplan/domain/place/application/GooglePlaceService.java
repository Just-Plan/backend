package com.jyp.justplan.domain.place.application;

import com.jyp.justplan.domain.place.domain.GooglePlace;
import com.jyp.justplan.domain.place.domain.GooglePlaceRepository;
import com.jyp.justplan.domain.place.GooglePlacesProperties;
import com.jyp.justplan.domain.place.dto.request.GooglePlaceRequest;
import com.jyp.justplan.domain.place.dto.response.GooglePlaceApiResultResponse;
import com.jyp.justplan.domain.place.dto.response.GooglePlaceResponse;
import com.jyp.justplan.domain.place.dto.response.GooglePlacesSearchApiResponse;
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
    private final WebClient webClient;

    @Autowired
    public GooglePlaceService(GooglePlaceRepository googlePlaceRepository, GooglePlacesProperties googlePlacesProperties, WebClient.Builder webClientBuilder) {
        this.googlePlaceRepository = googlePlaceRepository;
        this.googlePlacesProperties = googlePlacesProperties;
        this.webClient = webClientBuilder.baseUrl("https://maps.googleapis.com/maps/api/place").build();
    }

    @Transactional
    public List<GooglePlaceResponse> getGooglePlace(String textSearch) {
        String apiKey = googlePlacesProperties.getApiKey();

        GooglePlacesSearchApiResponse googlePlacesSearchApiResponse = webClient.get()
                .uri("/textsearch/json?query={textSearch}&key={key}", textSearch, apiKey)
                .retrieve()
                .bodyToMono(GooglePlacesSearchApiResponse.class)
                .block();

        if (googlePlacesSearchApiResponse == null || googlePlacesSearchApiResponse.getResults() == null) {
            throw new RuntimeException();
        }

        return googlePlacesSearchApiResponse.getResults().stream()
                .map(this::convertToGooglePlaceResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public GooglePlaceResponse convertToGooglePlaceResponse(GooglePlaceApiResultResponse result) {

        String photoReference = result.getPhotos() != null && !result.getPhotos().isEmpty()
                ? result.getPhotos().get(0).getPhotoReference()
                : null;

        return new GooglePlaceResponse(
                result.getId(),
                result.getName(),
                result.getFormattedAddress(),
                result.getTypes(),
                result.getGeometry().getLocation().getLat(),
                result.getGeometry().getLocation().getLng(),
                photoReference
        );
    }

    /*CREATE*/
    @Transactional
    public GooglePlaceResponse createGooglePlace(GooglePlaceRequest googlePlaceRequest) {

        GooglePlace googlePlace = googlePlaceRequest.toEntity().toBuilder()
                .build();

        GooglePlace savedGooglePlace = googlePlaceRepository.save(googlePlace);
        return GooglePlaceResponse.of(savedGooglePlace);
    }

}
