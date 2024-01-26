package com.jyp.justplan.domain.plan.domain.expense;

import com.jyp.justplan.domain.plan.domain.Plan;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expense {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    private int food;
    private int transportation;
    private int lodging;
    private int shopping;
    private int etc;

    public Expense(Plan plan) {
        this.plan = plan;
        this.food = 0;
        this.transportation = 0;
        this.lodging = 0;
        this.shopping = 0;
        this.etc = 0;
    }

    public void updateExpense(int food, int transportation, int lodging, int shopping, int etc) {
        this.food = food;
        this.transportation = transportation;
        this.lodging = lodging;
        this.shopping = shopping;
        this.etc = etc;
    }
}
