package com.jyp.justplan.domain.plan.domain.scrap;

import com.jyp.justplan.domain.BaseEntity;
import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Scrap extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrap_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    public Scrap(User user, Plan plan) {
        this.user = user;
        this.plan = plan;
    }
}
