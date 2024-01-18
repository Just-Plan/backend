package com.jyp.justplan.domain.city.domain;


import com.jyp.justplan.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "country")
@Entity
@NoArgsConstructor
@Getter
public class Country extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name="korean_name", nullable = false)
    private String koreanName;
    @Column(name="english_name", nullable = false)
    private String englishName;
    @Column(name="continent", nullable = false)
    private String continent;
    @Column(name="voltage", nullable = false)
    private String voltage;

    public Country(
        final String koreanName,
        final String englishName,
        final String continent,
        final String voltage
    ) {
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.continent = continent;
        this.voltage = voltage;
    }


}
