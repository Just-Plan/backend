package com.jyp.justplan.domain.city.domain;

import com.jyp.justplan.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "city")
@Entity
@NoArgsConstructor
@Getter
public class City extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name="korean_name", nullable = false)
    private String koreanName;
    @Column(name="english_name", nullable = false)
    private String englishName;
    @Column(name="introduction", nullable = false, length = 500)
    private String introduction;
    @Column(name="latitude", nullable = false)
    private String latitude;
    @Column(name="longitude", nullable = false)
    private String longitude;
    @Column(name="timezone", nullable = false)
    private String timezone;
    @Column(name="time_difference", nullable = false)
    private int timeDifference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    public City(
        final String koreanName,
        final String englishName,
        final String introduction,
        final String latitude,
        final String longitude,
        final String timezone,
        final Country country,
        int timeDifference
    ) {
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.introduction = introduction;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.timeDifference = timeDifference;
        this.country = country;
    }
}