package com.jyp.justplan.domain.place;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "google.places")
public class GooglePlacesProperties {
    private String apiKey;
}
