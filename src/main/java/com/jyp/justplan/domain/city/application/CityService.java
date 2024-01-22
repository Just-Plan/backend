package com.jyp.justplan.domain.city.application;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.city.domain.CityRepository;
import com.jyp.justplan.domain.city.dto.response.CitiesResponse;
import com.jyp.justplan.domain.city.dto.response.CityResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CityService {

    private final CityRepository cityRepository;

    public CityService(final CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public CitiesResponse getRandomCities(int count) {
        List<City> randomCities = cityRepository.getRandomCities(count);
        List<CityResponse> collect = randomCities.stream().map(CityResponse::new).toList();
        return new CitiesResponse(collect);
    }
}