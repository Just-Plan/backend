package com.jyp.justplan.domain.plan.domain;

import com.jyp.justplan.domain.BaseEntity;
import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE plan SET deleted = true WHERE plan_id = ?")
public class Plan extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private long id;

    private String title;

    // TODO: 지역 추가 시, 해당 enum이나 테이블 값으로 변경
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City region;
//    private String region;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    private boolean published = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_plan_id")
    private Plan originPlan;

    private boolean deleted = false;

    @Column(name = "use_expense")
    private boolean useExpense = false;

    public Plan(String title, ZonedDateTime startDate, ZonedDateTime endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setRegion(City region) {
        this.region = region;
    }

    public void updatePlan (String title, ZonedDateTime start_date, ZonedDateTime end_date) {
        this.title = title;
        this.startDate = start_date;
        this.endDate = end_date;
    }

    public void updatePublic (boolean published) {
        this.published = published;
    }
    public void updateUseExpense (boolean use_expense) {
        this.useExpense = use_expense;
    }

    public void setOriginPlan (Plan origin_plan) {
        this.originPlan = origin_plan;
    }
}
