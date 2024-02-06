package com.jyp.justplan.domain.place.domain;

import com.jyp.justplan.domain.BaseEntity;
import com.jyp.justplan.domain.city.domain.City;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE place SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at is null")
public class GooglePlace extends BaseEntity {
    private static final double THRESHOLD = 0.0001;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GOOGLE_PLACE_ID")
    private Long id;
    private String name;
    private String address;
    private String types;
    private double latitude;
    private double longitude;
    private String photoReference;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "google_place_stats_id")
    private GooglePlaceStats googlePlaceStats;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CITY_ID", nullable = false)
    private City city;
}
