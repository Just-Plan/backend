package com.jyp.justplan.domain.plan.domain;

import com.jyp.justplan.domain.BaseEntity;
import com.jyp.justplan.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPlan extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_plan_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    private boolean owner;

    public UserPlan(User user, Plan plan, boolean owner) {
        this.user = user;
        this.plan = plan;
        this.owner = owner;
    }
}
