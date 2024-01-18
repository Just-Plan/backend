package com.jyp.justplan.domain.city.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CityRepository extends JpaRepository<City, Long> {
    @Query(value = "SELECT c.*, country.* FROM city c JOIN country ON c.country_id = country.id ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<City> getRandomCities(@Param("count") int count);
}
