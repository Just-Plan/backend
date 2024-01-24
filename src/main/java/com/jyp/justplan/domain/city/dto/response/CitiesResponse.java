package com.jyp.justplan.domain.city.dto.response;

import java.util.List;

public class CitiesResponse {
    private List<CityResponse> cities;

    public CitiesResponse() {
    }

    public CitiesResponse(final List<CityResponse> cities) {
        this.cities = cities;
    }

    public List<CityResponse> getCities() {
        return cities;
    }
}