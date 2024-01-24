package com.jyp.justplan.domain.plan.domain.tag;

import com.jyp.justplan.domain.plan.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanTagRepository extends JpaRepository<PlanTag, Long> {
    List<PlanTag> findByPlanOrderByUpdatedAtAsc(Plan plan);

    List<PlanTag> findByPlan(Plan plan);

    Optional<PlanTag> findByPlanAndTag(Plan plan, Tag tag);

    List<PlanTag> deleteByPlan(Plan plan);

    boolean existsByTag(Tag tag);
}
