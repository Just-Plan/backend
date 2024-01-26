package com.jyp.justplan.domain.plan.domain.budget;

import com.jyp.justplan.domain.plan.domain.Plan;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Budget {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    private int card;
    private int cash;

    public Budget(Plan plan) {
        this.plan = plan;
        this.card = 0;
        this.cash = 0;
    }

    public void updateBudget(int card, int cash) {
        this.card = card;
        this.cash = cash;
    }
}
