package com.jyp.justplan.domain.place.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GooglePlaceRepository extends JpaRepository<GooglePlace, Long> {

    List<GooglePlace> findByCityId(Long cityId);

    Optional<GooglePlace> findByLatitudeAndLongitude(double latitude, double longitude);

    Optional<GooglePlace> findByName(String name);
}
