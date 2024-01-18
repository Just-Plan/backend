package com.jyp.justplan.domain.plan.domain;

import com.jyp.justplan.domain.BaseEntity;
import com.jyp.justplan.domain.plan.exception.InvalidPlanException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE plan SET deleted = true WHERE plan_id = ?")
public class Plan extends BaseEntity {
    private static final int MAX_TITLE_LENGTH = 50;

    private static final ZonedDateTime MIN_DATE_TIME = ZonedDateTime.of(1900, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
    private static final ZonedDateTime MAX_DATE_TIME = ZonedDateTime.of(2200, 12, 31, 11, 59, 59, 999999000, ZoneId.systemDefault());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private long id;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_plan_id")
    private Plan originPlan;

    private boolean deleted = false;

    public Plan(String title, ZonedDateTime startDate, ZonedDateTime endDate, String region) {
        validateTitle(title);
        validateDate(startDate, endDate);

        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.region = region;
    }

    public void updatePlan (String title, ZonedDateTime start_date, ZonedDateTime end_date) {
        validateTitle(title);
        validateDate(start_date, end_date);

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

    /* validate */
    private void validateTitle (final String title) {
        if (title.trim() == "") {
            throw new InvalidPlanException("제목은 공백일 수 없습니다.");
        } else if (title.length() > MAX_TITLE_LENGTH) {
            throw new InvalidPlanException("제목은 " + MAX_TITLE_LENGTH + "자 이하여야 합니다.");
        }
    }

    private void validateDate (final ZonedDateTime start_date, final ZonedDateTime end_date) {
        if (start_date.isBefore(MIN_DATE_TIME) || start_date.isAfter(MAX_DATE_TIME)) {
            throw new InvalidPlanException("시작 날짜는 " + MIN_DATE_TIME + " ~ " + MAX_DATE_TIME + " 사이여야 합니다.");
        } else if (end_date.isBefore(MIN_DATE_TIME) || end_date.isAfter(MAX_DATE_TIME)) {
            throw new InvalidPlanException("종료 날짜는 " + MIN_DATE_TIME + " ~ " + MAX_DATE_TIME + " 사이여야 합니다.");
        } else if (start_date.isAfter(end_date)) {
            throw new InvalidPlanException("시작 날짜는 종료 날짜보다 빠를 수 없습니다.");
        }
    }
}
