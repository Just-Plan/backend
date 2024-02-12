package com.jyp.justplan.domain.place.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GooglePlaceStatsRepository extends JpaRepository<GooglePlaceStats, Long> {

    void deleteAllByGooglePlaceId(Long googlePlaceId);

    List<GooglePlaceStats> findAllByGooglePlaceId(Long googlePlaceId);
}