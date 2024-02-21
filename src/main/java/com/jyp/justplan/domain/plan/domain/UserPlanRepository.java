package com.jyp.justplan.domain.plan.domain;

import com.jyp.justplan.domain.plan.exception.NoSuchUserPlanException;
import com.jyp.justplan.domain.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPlanRepository extends JpaRepository<UserPlan, Long> {
    List<UserPlan> findByUser(User user);
    List<UserPlan> findByPlan(Plan plan);

    Optional<UserPlan> findByPlanAndOwnerTrue(Plan plan);

    boolean existsByUserAndPlan(User user, Plan plan);

    void deleteByPlan(Plan plan);
    void deleteByUser(User user);

    default List<UserPlan> getByUser(User user) {
        List<UserPlan> userPlans = findByUser(user);
        return userPlans;
    }

    default List<UserPlan> getByPlan(Plan plan) {
        List<UserPlan> userPlans = findByPlan(plan);
        if (userPlans.isEmpty()) {
            throw new NoSuchUserPlanException("해당 플랜에 대한 유저 정보가 존재하지 않습니다.");
        }
        return userPlans;
    }

    @Query("SELECT u.mbti, COUNT(u.mbti) AS cnt " +
        "FROM UserPlan up " +
        "JOIN up.plan p " +
        "JOIN up.user u " +
        "WHERE p.id IN (SELECT pl.plan.id FROM Place pl WHERE pl.googlePlace.id = :googlePlaceId) " +
        "GROUP BY u.mbti " +
        "ORDER BY cnt DESC")
    List<Object[]> findMbtiDistributionByGooglePlaceId(@Param("googlePlaceId") Long googlePlaceId, Pageable pageable);


}
