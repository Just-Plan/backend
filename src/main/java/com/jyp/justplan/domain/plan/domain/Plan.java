package com.jyp.justplan.domain.plan.domain;

import com.jyp.justplan.domain.BaseEntity;
import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.plan.domain.budget.Budget;
import com.jyp.justplan.domain.plan.domain.expense.Expense;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "plan")
    private List<UserPlan> users = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City region;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id")
    private Budget budget;

    @Column(name = "use_expense")
    private boolean useExpense = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id")
    private Expense expense;

    private boolean published = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_plan_id")
    private Plan originPlan;

    private boolean deleted = false;

    public Plan(String title, ZonedDateTime startDate, ZonedDateTime endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addUser(UserPlan userPlan) {
        users.add(userPlan);
        userPlan.setPlan(this);
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

    public void setBudget (Budget budget) {
        this.budget = budget;
    }

    public void setExpense (Expense expense) {
        this.expense = expense;
    }

    public void setOriginPlan (Plan origin_plan) {
        this.originPlan = origin_plan;
    }
}
