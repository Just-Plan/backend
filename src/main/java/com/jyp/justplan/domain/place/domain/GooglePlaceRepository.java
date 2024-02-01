package com.jyp.justplan.domain.place.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GooglePlaceRepository extends JpaRepository<GooglePlace, Long> {

        @Query("SELECT g FROM GooglePlace g WHERE g.name LIKE %?1% OR g.address LIKE %?1%")
        List<GooglePlace> findByKeyword(String keyword);

    List<GooglePlace> findByCityId(Long cityId);
}
