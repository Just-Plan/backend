package com.jyp.justplan.domain.plan.domain.scrap;

import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    @Query("select s.plan from Scrap s where s.user = ?1 order by s.createdAt desc")
    Page<Plan> findAllByUserOrderByCreatedAt(Pageable pageable, User user);

    Optional<Scrap> findByUserAndPlan (User user, Plan plan);

    long countByPlan(Plan plan);

    long countByUser(User user);
}
