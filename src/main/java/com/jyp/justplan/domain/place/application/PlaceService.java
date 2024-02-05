package com.jyp.justplan.domain.place.application;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.mbti.domain.MbtiType;
import com.jyp.justplan.domain.place.GooglePlacesProperties;
import com.jyp.justplan.domain.place.domain.GoogleMapType;
import com.jyp.justplan.domain.place.domain.GooglePlace;
import com.jyp.justplan.domain.place.domain.GooglePlaceRepository;
import com.jyp.justplan.domain.place.domain.Memo;
import com.jyp.justplan.domain.place.domain.MemoRepository;
import com.jyp.justplan.domain.place.domain.Place;
import com.jyp.justplan.domain.place.domain.PlaceRepository;
import com.jyp.justplan.domain.place.dto.request.MemoUpdateDto;
import com.jyp.justplan.domain.place.dto.request.PlaceListRequest;
import com.jyp.justplan.domain.place.dto.request.PlacePlanUpdateDto;
import com.jyp.justplan.domain.place.dto.request.PlaceRequest;
import com.jyp.justplan.domain.place.dto.request.PlaceUpdateRequest;
import com.jyp.justplan.domain.place.dto.response.GooglePlacesSearchResponse;
import com.jyp.justplan.domain.place.dto.response.PlaceDetailResponse;
import com.jyp.justplan.domain.place.dto.response.PlaceDetailResponse.Result.Photo;
import com.jyp.justplan.domain.place.dto.response.PlaceResponse;
import com.jyp.justplan.domain.place.dto.response.SchedulePlacesResponse;
import com.jyp.justplan.domain.place.exception.NoSuchGooglePlaceException;
import com.jyp.justplan.domain.place.exception.NoSuchPlaceException;
import com.jyp.justplan.domain.plan.application.PlanService;
import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.PlanRepository;
import com.jyp.justplan.domain.plan.exception.NoSuchPlanException;
import com.jyp.justplan.domain.plan.exception.NoSuchUserPlanException;
import com.jyp.justplan.domain.user.domain.User;
import com.jyp.justplan.domain.user.domain.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@Slf4j
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final MemoRepository memoRepository;
    private final GooglePlaceRepository googlePlaceRepository;
    private final PlanRepository planRepository;
    private final PlanService planService;
    private final UserRepository userRepository;
    private final WebClient webClient;
    private final GooglePlacesProperties googlePlacesProperties;
    private final GooglePlaceService googlePlaceService;

    @Autowired
    public PlaceService(PlaceRepository placeRepository, MemoRepository memoRepository,
        GooglePlaceRepository googlePlaceRepository, PlanRepository planRepository,
        PlanService planService, UserRepository userRepository, WebClient.Builder webClientBuilder, GooglePlacesProperties googlePlacesProperties, GooglePlaceService googlePlaceService) {
        this.placeRepository = placeRepository;
        this.memoRepository = memoRepository;
        this.googlePlaceRepository = googlePlaceRepository;
        this.planRepository = planRepository;
        this.planService = planService;
        this.userRepository = userRepository;
        this.webClient = webClientBuilder.baseUrl("https://maps.googleapis.com").build();
        this.googlePlacesProperties = googlePlacesProperties;
        this.googlePlaceService = googlePlaceService;
    }

    // 일정 생성
    @Transactional
    public void createPlace(PlaceListRequest placeListRequest, Long planId, Long userId) {
        Plan findPlan = getPlan(planId);
        extracted(userId, planId);

        for (PlaceRequest placeRequest : placeListRequest.getPlaces()) {
            // Memo 생성 로직을 각 Place에 대해 반복
            Memo savedMemo = memoRepository.save(new Memo());
            // GooglePlace 찾기 또는 새로 생성
            GooglePlace googlePlace = Optional.ofNullable(placeRequest.getGooglePlaceId())
                .flatMap(googlePlaceRepository::findById)
                .orElseGet(() -> findOrCreateGooglePlace(placeRequest, findPlan.getRegion()));
            // 각 Place에 대한 인스턴스 생성 및 저장
            Place newPlace = new Place(0, 0, findPlan, savedMemo, googlePlace);
            placeRepository.save(newPlace);
        }
    }

    // 일정에 대한 전체 장소 조회
    public SchedulePlacesResponse findPlacesByPlanId(Long planId) {
        planRepository.findById(planId).orElseThrow(() -> new NoSuchPlanException("존재하지 않는 planId 입니다: " + planId));

        List<Place> places = placeRepository.findByPlanId(planId);
        List<Place> sortedPlaces = places.stream()
            .sorted(Comparator.comparingInt(Place::getDay).thenComparingInt(Place::getOrderNum)).toList();
        Map<Integer, List<PlaceResponse>> groupedByDay = sortedPlaces.stream().collect(Collectors.groupingBy(Place::getDay,
            LinkedHashMap::new, Collectors.mapping(PlaceResponse::of, Collectors.toList())));
        return SchedulePlacesResponse.of(groupedByDay);
    }

    // 전체 장소 수정
    @Transactional
    public void updatePlaces(PlacePlanUpdateDto placePlanUpdateDto, Long userId, Long planId) {
        extracted(userId, planId);

        // Place 삭제
        List<Long> placeDeleteIds = placePlanUpdateDto.getPlaceDeleteIds();
        List<Place> placesToDelete = new ArrayList<>();

        placeDeleteIds.forEach(placeId -> {
            Optional<Place> optionalPlace = placeRepository.findById(placeId);
            optionalPlace.ifPresent(placesToDelete::add);
        });

        placeRepository.deleteAll(placesToDelete);

        placePlanUpdateDto.getDayUpdates().forEach((day, updates) -> {
            updates.forEach(updateRequest -> {
                Place place = getPlace(updateRequest);
                // orderNum과 day, memo 업데이트
                Memo memo = place.getMemo();
                memo.update(new MemoUpdateDto(updateRequest.getMemo().getContent(), updateRequest.getMemo().getColor()));
                place.update(updateRequest.getOrderNum(), day, memo);
            });
        });
    }

    // 장소 상세 조회 플로우
    public Mono<PlaceDetailResponse> getPlaceDetail(String name, String latitude, String longitude, String googlePlaceId) {
        return Mono.justOrEmpty(googlePlaceId)
            .switchIfEmpty(findPlaceIdByNameAndLocation(name, latitude, longitude))
            .flatMap(this::findPlaceDetailsByGoogleId)
            .flatMap(placeDetailResponse -> {
                placeDetailResponse.getResult().setTranslatedTypes();
                return googlePlaceService.fetchPhotoUrl(placeDetailResponse.getResult().getPhotos().get(0).getPhotoReference(), 400)
                    .map(url -> {
                        placeDetailResponse.getResult().setPhotos(List.of(new Photo(0, 0, url, Collections.emptyList())));
                        return placeDetailResponse;
                    });
            })
            .flatMap(placeDetailResponse -> {
                Optional<GooglePlace> optionalGooglePlace = googlePlaceRepository.findByName(placeDetailResponse.getResult().getName());
                if (optionalGooglePlace.isPresent()) {
                    // TODO: 추가 작업을 수행하고, placeDetailResponse를 수정하거나 업데이트합니다.
                    placeDetailResponse.getResult().setMbti(List.of(MbtiType.enfp, MbtiType.enfj, MbtiType.entp, MbtiType.entj, MbtiType.infj));
                }
                return Mono.just(placeDetailResponse);
            });
    }

    // 이름, 위도, 경도로 placeId 찾기
    public Mono<String> findPlaceIdByNameAndLocation(String name, String latitude, String longitude) {
        String apiKey = googlePlacesProperties.getApiKey();
        log.info(latitude);
        log.info(longitude);

        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/maps/api/place/nearbysearch/json")
                .queryParam("location", String.format("%s,%s", latitude, longitude))
                .queryParam("keyword", name)
                .queryParam("radius", 1)
                .queryParam("key", apiKey)
                .queryParam("language", "ko")
                .build())
            .retrieve()
            .bodyToMono(GooglePlacesSearchResponse.class)
            .map(response -> {
                if (response.getStatus().equals("OK") && !response.getResults().isEmpty()) {
                    return response.getResults().get(0).getPlaceId();
                } else {
                    throw new NoSuchGooglePlaceException("구글 아이디를 찾을 수 업습니다:" + name +" "+ latitude +" "+longitude);
                }
            });
    }

    // placeId로 장소 상세 정보 조회
    public Mono<PlaceDetailResponse> findPlaceDetailsByGoogleId(String googlePlaceId) {
        String apiKey = googlePlacesProperties.getApiKey();

        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/maps/api/place/details/json")
                .queryParam("place_id", googlePlaceId)
                .queryParam("fields", "name,rating,user_ratings_total,formatted_phone_number,types,opening_hours,photos")
                .queryParam("key", apiKey)
                .queryParam("language", "ko")
                .build())
            .retrieve()
            .bodyToMono(PlaceDetailResponse.class);
    }

    private Place getPlace(PlaceUpdateRequest updateRequest) {
        return placeRepository.findById(updateRequest.getPlaceId())
            .orElseThrow(() -> new NoSuchPlaceException("Place not found with id: " + updateRequest.getPlaceId()));
    }

    private void extracted(Long userId, Long planId) {
        Plan findPlan = getPlan(planId);
        User findUser = getUser(userId);
        planService.validateUserOfPlan(findPlan, findUser);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchUserPlanException("존재하지 않는 userId 입니다: " + userId));
    }

    private Plan getPlan(Long planId) {
        return planRepository.findById(planId)
            .orElseThrow(() -> new NoSuchPlanException("존재하지 않는 planId 입니다: " + planId));
    }

    private GooglePlace createGooglePlace(PlaceRequest request, City city) {
        // GooglePlace 객체 생성 및 초기화 로직
        GooglePlace newGooglePlace = GooglePlace.builder()
            .name(request.getName())
            .address(request.getFormattedAddress())
            .types(GoogleMapType.translateFromKorean(request.getTypes()))
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .photoReference(request.getPhotoReference())
            .city(city)
            .build();
        return googlePlaceRepository.save(newGooglePlace);
    }

    private GooglePlace findOrCreateGooglePlace(PlaceRequest request, City city) {
        // 위도와 경도를 기준으로 기존의 GooglePlace가 있는지 검사합니다.
        return googlePlaceRepository.findByLatitudeAndLongitude(request.getLatitude(), request.getLongitude())
            .orElseGet(() -> {
                GooglePlace newGooglePlace = createGooglePlace(request, city);
                return googlePlaceRepository.save(newGooglePlace);
            });
    }
}