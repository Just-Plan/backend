package com.jyp.justplan.domain.place;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "google.places")
public class GooglePlacesProperties {
    private String apiKey;
}
