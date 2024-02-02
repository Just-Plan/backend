package com.jyp.justplan.domain.place.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place,Long> {
    List<Place> findByPlanId(Long planId);
}
