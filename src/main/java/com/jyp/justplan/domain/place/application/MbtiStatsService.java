package com.jyp.justplan.domain.place.application;

import com.jyp.justplan.domain.place.domain.GooglePlaceStatsRepository;
import com.jyp.justplan.domain.mbti.domain.Mbti;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MbtiStatsService {

    private final GooglePlaceStatsRepository googlePlaceStatsRepository;

//    public List<MbtiCountDto> getTopMbtiForGooglePlace(Long googlePlaceId) {
//        List<Object[]> mbtiCounts = googlePlaceStatsRepository.findTopMbtiCountsByGooglePlaceId(googlePlaceId);
//        return mbtiCounts.stream()
//            .map(obj -> new MbtiCountDto((Mbti)obj[0], (Long)obj[1]))
//            .limit(5)
//            .collect(Collectors.toList());
//    }
}