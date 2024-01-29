package com.jyp.justplan.domain.plan.application;

import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.UserPlan;
import com.jyp.justplan.domain.plan.domain.UserPlanRepository;
import com.jyp.justplan.domain.plan.dto.response.UserPlanResponse;
import com.jyp.justplan.domain.plan.exception.UserPlanAlreadyExistsException;
import com.jyp.justplan.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPlanService {
    private final UserPlanRepository userPlanRepository;

    @Transactional
    public void saveUserPlan(User user, Plan plan, boolean owner) {
        UserPlan userPlan = new UserPlan(user, plan, owner);
        if (userPlanRepository.existsByUserAndPlan(user, plan)) {
            throw new UserPlanAlreadyExistsException(plan.getTitle() + " 일정에 대한 " + user.getName() + "의 정보가 이미 존재합니다.");
        }

        userPlanRepository.findByPlanAndOwnerTrue(plan)
                .ifPresent(up -> {
                    throw new UserPlanAlreadyExistsException(plan.getTitle() + " 일정에 대한 소유자의 정보가 이미 존재합니다.");
                });

        userPlanRepository.save(userPlan);
    }

    public User findOwnerByPlan(Plan plan) {
        UserPlan userPlan = userPlanRepository.findByPlanAndOwnerTrue(plan)
                .orElseThrow(() -> new UserPlanAlreadyExistsException(plan.getTitle() + " 일정에 대한 소유자의 정보가 존재하지 않습니다."));
        return userPlan.getUser();
    }

    public List<User> findUsersByPlan(Plan plan) {
        List<UserPlan> userPlans = userPlanRepository.getByPlan(plan);
        return userPlans.stream()
                .map(userPlan -> userPlan.getUser())
                .collect(Collectors.toList());
    }

    public List<UserPlanResponse> findUserPlanResponsesByPlan(Plan plan) {
        List<UserPlan> userPlans = userPlanRepository.getByPlan(plan);
        return userPlans.stream()
                .map(userPlan -> UserPlanResponse.toDto(userPlan))
                .collect(Collectors.toList());
    }

    public List<Plan> findPlansByUser(User user) {
        List<UserPlan> userPlans = userPlanRepository.getByUser(user);
        return userPlans.stream()
                .map(userPlan -> userPlan.getPlan())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteByPlan(Plan plan) {
        userPlanRepository.deleteByPlan(plan);
    }

    @Transactional
    public void deleteByUser(User user) {
        userPlanRepository.deleteByUser(user);
    }
}
