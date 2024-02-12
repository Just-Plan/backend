package com.jyp.justplan.domain.place.application;

import com.jyp.justplan.domain.place.domain.GooglePlace;
import com.jyp.justplan.domain.place.domain.GooglePlaceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {
    private final StatsUpdateService statsUpdateService;
    private final GooglePlaceRepository googlePlaceRepository;

    // 1시간마다 실행
    @Scheduled(fixedRate = 3600000)
    public void updateGooglePlaceStats() {
        List<GooglePlace> allGooglePlaces = googlePlaceRepository.findAll();
        for (GooglePlace googlePlace : allGooglePlaces) {
            Long googlePlaceId = googlePlace.getId();
            try {
                statsUpdateService.updateStatsForGooglePlace(googlePlaceId);
                log.info("통계 업데이트 성공: GooglePlace ID = {}", googlePlaceId);
            } catch (Exception e) {
                log.error("통계 업데이트 실패: GooglePlace ID = {}, 에러: {}", googlePlaceId, e.getMessage());
            }
        }
    }
}
