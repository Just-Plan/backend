package com.jyp.justplan.domain.plan.domain;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.plan.exception.NoSuchPlanException;
import com.jyp.justplan.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    @Query("select p from Plan p " +
            "join p.users up " +
            "where p.deleted = false and p.published = true " +
            "and up.user = ?1")
    Page<Plan> findAllByUser(Pageable pageable, User user);

    @Query("select p from Plan p " +
            "join p.users up " +
            "where p.deleted = false and p.published = true " +
            "and up.user = ?1")
    List<Plan> findAllByUser(User user);

    @Query("select p from Plan p " +
            "left join Scrap s on p = s.plan " +
            "where p.deleted = false and p.published = true " +
            "group by p " +
            "order by count(s.plan) desc")
    Page<Plan> findAllOrderByScrapCnt(Pageable pageable);

    @Query("select p from Plan p " +
            "left join Scrap s on p = s.plan " +
            "where p.deleted = false and p.published = true " +
            "and p.region = ?1 " +
            "group by p " +
            "order by count(s.plan) desc")
    Page<Plan> findAllByRegionOrderByScrapCnt(Pageable pageable, City region);

    @Query("select p from Plan p " +
            "left join Scrap s on p = s.plan " +
            "left join UserPlan up on p = up.plan " +
            "where p.deleted = false and p.published = true " +
            "and up.user.mbti.mbti in ?1 " +
            "group by p " +
            "order by count(s.plan) desc")
    Page<Plan> findAllByMbtiOrderByScrapCnt(Pageable pageable, List<String> mbti);

    @Query("select p from Plan p " +
            "left join Scrap s on p = s.plan " +
            "left join UserPlan up on p = up.plan " +
            "where p.deleted = false and p.published = true " +
            "and up.user.mbti.mbti in ?1 " +
            "and p.region = ?2 " +
            "group by p " +
            "order by count(s.plan) desc")
    Page<Plan> findAllByMbtiAndRegionOrderByScrapCnt(Pageable pageable, List<String> mbti, City region);

    default Plan getById(final Long id) {
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
