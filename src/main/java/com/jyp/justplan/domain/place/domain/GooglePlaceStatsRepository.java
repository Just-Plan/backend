package com.jyp.justplan.domain.place.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GooglePlaceStatsRepository extends JpaRepository<GooglePlaceStats, Long> {

    @Modifying
    @Query("DELETE FROM GooglePlaceStats gps WHERE gps.googlePlace.id = :googlePlaceId")
    void deleteAllByGooglePlaceId(@Param("googlePlaceId") Long googlePlaceId);


    List<GooglePlaceStats> findAllByGooglePlaceId(Long googlePlaceId);
}