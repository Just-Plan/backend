package com.jyp.justplan.domain.place.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlaceRepository extends JpaRepository<Place,Long> {
    List<Place> findByPlanId(Long planId);

//    default Place findByPlaceId(Long planId) {
//        return findById(planId).orElseThrow(RuntimeException::new);
//    }

    Optional<Place> findById(Long placeId);
    List<Place> findByIdIn(List<Long> ids);

    // 일정에 해당하는 랜덤한 하나의 장소 가져오기
    @Query(value = "select * from place p " +
            "join google_place gp on p.google_place_id = gp.google_place_id " +
            "where gp.photo_reference is not null " +
            "and p.plan_id = ?1 " +
            "order by rand() " +
            "limit 1",
            nativeQuery = true)
    Optional<Place> findRandomByPlanId(Long planId);
}
