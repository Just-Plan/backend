package com.jyp.justplan.domain.plan.domain.budget;

import com.jyp.justplan.domain.plan.domain.Plan;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Budget {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id")
    private Long id;

    private int card;
    private int cash;

    public void updateBudget(int card, int cash) {
        this.card = card;
        this.cash = cash;
    }
}
