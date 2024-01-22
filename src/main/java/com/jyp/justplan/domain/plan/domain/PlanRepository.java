package com.jyp.justplan.domain.plan.domain;

import com.jyp.justplan.domain.plan.exception.NoSuchPlanException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    @Query("select p from Plan p where p.deleted = false and p.isPublic = true")
    Page<Plan> findAll(Pageable pageable);

    // TODO: 유저 연동 후, MBTI에 따른 조회 기능 구현 필요
//    @Query("select p from Plan p where p.deleted = false and p.isPublic = true and p.user.mbti = :mbti")
//    Page<Plan> findAllByMbti(String mbti, Pageable pageable);

    // TODO: 유저 연동 후, 유저가 작성한 플랜 조회 기능 구현 필요
//    Page<Plan> findAllByUser(User user, Pageable pageable);

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
