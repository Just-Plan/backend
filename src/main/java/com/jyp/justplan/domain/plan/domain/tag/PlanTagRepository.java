package com.jyp.justplan.domain.plan.domain.tag;

import com.jyp.justplan.domain.plan.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PlanTagRepository extends JpaRepository<PlanTag, Long> {
    Set<PlanTag> findByPlan(Plan plan);

    Optional<PlanTag> findByPlanAndTag(Plan plan, Tag tag);

    Set<PlanTag> deleteByPlan(Plan plan);

    boolean existsByTag(Tag tag);
}
