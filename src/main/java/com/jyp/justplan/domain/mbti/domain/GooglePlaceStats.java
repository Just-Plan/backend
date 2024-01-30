package com.jyp.justplan.domain.mbti.domain;

import com.jyp.justplan.domain.place.domain.GooglePlace;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "google_place_stats")
@Entity
@NoArgsConstructor
@Getter
public class GooglePlaceStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "google_place_id")
    private GooglePlace googlePlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mbti_id")
    private Mbti mbti;

    @Column(name = "count")
    private int count;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @PreUpdate
    @PrePersist
    public void onPreUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }



}
