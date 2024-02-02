package com.jyp.justplan.domain.place.domain;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place,Long> {
    List<Place> findByPlanId(Long planId);

//    default Place findByPlaceId(Long planId) {
//        return findById(planId).orElseThrow(RuntimeException::new);
//    }

    Optional<Place> findById(Long placeId);
    List<Place> findByIdIn(List<Long> ids);
}
