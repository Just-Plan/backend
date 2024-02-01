package com.jyp.justplan.domain.plan.domain;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.plan.exception.NoSuchPlanException;
import com.jyp.justplan.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    @Query("select p from Plan p where p.deleted = false and p.published = true")
    Page<Plan> findAll(Pageable pageable);

    @Query("select p from Plan p where p.deleted = false and p.published = true and p.region = ?1")
    Page<Plan> findAllByRegion(Pageable pageable, City region);

    default Plan getById(final Long id) {
        // TODO: Exception Handling
        return findById(id)
                .map(plan -> {
                    if (plan.isDeleted()) {
                        throw new NoSuchPlanException("삭제된 일정입니다.");
                    }
                    return plan;
                })
                .orElseThrow(() -> new NoSuchPlanException("존재하지 않는 일정입니다."));
    }
}
