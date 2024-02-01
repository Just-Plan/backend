package com.jyp.justplan.domain.place.domain;

import com.jyp.justplan.domain.BaseEntity;
import com.jyp.justplan.domain.city.domain.City;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CITY_ID", nullable = false)
    private City city;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        GooglePlace that = (GooglePlace) o;

        // id와 위도, 경도를 비교하여 동등성 판단
        return Objects.equals(id, that.id)
            && Math.abs(this.latitude - that.latitude) < THRESHOLD
            && Math.abs(this.longitude - that.longitude) < THRESHOLD;
    }

    @Override
    public int hashCode() {
        // id와 위도, 경도를 기반으로 해시코드 생성
        return Objects.hash(super.hashCode(), id, latitude, longitude);
    }
}
