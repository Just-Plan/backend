package com.jyp.justplan.domain.place.domain;

import com.jyp.justplan.domain.mbti.domain.Mbti;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Table(name = "google_place_stats")
@Entity
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE GooglePlaceStats SET deleted_at = NOW() WHERE google_place_stats_id = ?")
@Where(clause = "deleted_at is null")
public class GooglePlaceStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "google_place_stats_id")
    private Long id;

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

    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;
}
