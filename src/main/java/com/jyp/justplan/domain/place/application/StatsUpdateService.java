package com.jyp.justplan.domain.place.application;

import com.jyp.justplan.domain.mbti.domain.Mbti;
import com.jyp.justplan.domain.place.domain.GooglePlace;
import com.jyp.justplan.domain.place.domain.GooglePlaceRepository;
import com.jyp.justplan.domain.place.domain.GooglePlaceStats;
import com.jyp.justplan.domain.place.domain.GooglePlaceStatsRepository;
import com.jyp.justplan.domain.plan.domain.UserPlanRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StatsUpdateService {
    private final GooglePlaceStatsRepository googlePlaceStatsRepository;
    private final UserPlanRepository userPlanRepository;
    private final GooglePlaceRepository googlePlaceRepository;

    @Transactional
    public void updateStatsForGooglePlace(Long googlePlaceId) {
        List<Object[]> mbtiDistribution = userPlanRepository.findMbtiDistributionByGooglePlaceId(
            googlePlaceId, PageRequest.of(0, 5));

        googlePlaceStatsRepository.deleteAllByGooglePlaceId(googlePlaceId);
        GooglePlace googlePlace = googlePlaceRepository.findById(googlePlaceId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 GooglePlace ID"));

        for (Object[] distribution : mbtiDistribution) {
            Mbti mbti = (Mbti) distribution[0];
            Long count = (Long) distribution[1];

            GooglePlaceStats stats = new GooglePlaceStats(mbti, count.intValue(), googlePlace);

            googlePlaceStatsRepository.save(stats);
        }
    }
}