package com.jyp.justplan.domain.plan.domain.tag;

import com.jyp.justplan.domain.plan.domain.Plan;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanTag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public PlanTag(Plan plan, Tag tag) {
        this.plan = plan;
        this.tag = tag;
    }
}
