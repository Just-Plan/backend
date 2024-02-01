package com.jyp.justplan.domain.place.domain;

import com.jyp.justplan.domain.BaseEntity;
import com.jyp.justplan.domain.plan.domain.Plan;
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
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLACE_ID", nullable = false)
    private Long id;
    @Column(length = 10, nullable = false)
    private int day;
    @Column(length = 10, nullable = false)
    private int orderNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAN_ID", nullable = false)
    private Plan plan;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MEMO_ID")
    private Memo memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GOOGLE_PLACE_ID", nullable = false)
    private GooglePlace googlePlace;

    public Place(int day, int orderNum, Plan plan, Memo memo, GooglePlace googlePlace) {
        this.day = day;
        this.orderNum = orderNum;
        this.plan = plan;
        this.memo = memo;
        this.googlePlace = googlePlace;
    }
}