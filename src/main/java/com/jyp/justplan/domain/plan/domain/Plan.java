package com.jyp.justplan.domain.plan.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private long planId;

    @NotNull
    private String title;

    @NotNull
    // TODO: 지역 추가 시, 해당 enum이나 테이블 값으로 변경
    private String region;

    @NotNull
    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @NotNull
    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "is_public")
    private boolean isPublic = true;

    @ManyToOne
    @JoinColumn(name = "origin_plan_id")
    private Plan originPlan;

    // TODO: BaseTimeEntity를 상속받아서 사용하도록 변경
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public Plan(String title, ZonedDateTime startDate, ZonedDateTime endDate, String region) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.region = region;
    }

    public void updatePlan (String title, ZonedDateTime start_date, ZonedDateTime end_date) {
        this.title = title;
        this.startDate = start_date;
        this.endDate = end_date;
    }

    public void updatePublic (boolean is_public) {
        this.isPublic = is_public;
    }

    public void setOriginPlan (Plan origin_plan) {
        this.originPlan = origin_plan;
    }
}
