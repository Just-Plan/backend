package com.jyp.justplan.domain.plan.application;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.UserPlan;
import com.jyp.justplan.domain.plan.domain.UserPlanRepository;
import com.jyp.justplan.domain.plan.dto.response.UserPlanResponse;
import com.jyp.justplan.domain.plan.exception.UserPlanAlreadyExistsException;
import com.jyp.justplan.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        UserPlan userPlan = new UserPlan(user, owner);
        plan.addUser(userPlan);

        if (userPlanRepository.existsByUserAndPlan(user, plan)) {
            throw new UserPlanAlreadyExistsException(plan.getTitle() + " 일정에 대한 " + user.getName() + "의 정보가 이미 존재합니다.");
        }

        userPlanRepository.findByPlanAndOwnerTrue(plan)
                .ifPresent(up -> {
                    throw new UserPlanAlreadyExistsException(plan.getTitle() + " 일정에 대한 소유자의 정보가 이미 존재합니다.");
                });

        userPlanRepository.save(userPlan);
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
