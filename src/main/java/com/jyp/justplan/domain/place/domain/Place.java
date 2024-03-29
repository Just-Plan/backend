package com.jyp.justplan.domain.place.domain;

import com.jyp.justplan.domain.BaseEntity;
import com.jyp.justplan.domain.plan.domain.Plan;
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
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE place SET deleted_at = NOW() WHERE PLACE_ID = ?")
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
    @Setter
    private Memo memo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GOOGLE_PLACE_ID", nullable = false)
    private GooglePlace googlePlace;

    public Place(int day, int orderNum, Plan plan, Memo memo, GooglePlace googlePlace) {
        this.day = day;
        this.orderNum = orderNum;
        this.plan = plan;
        this.memo = memo;
        this.googlePlace = googlePlace;
    }

    public void update(int orderNum, int day, Memo memo) {
        this.orderNum = orderNum;
        this.day = day;
        this.memo = memo;
    }
}