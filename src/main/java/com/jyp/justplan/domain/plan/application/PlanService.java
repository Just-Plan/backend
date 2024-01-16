package com.jyp.justplan.domain.plan.application;

import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.PlanRepository;
import com.jyp.justplan.domain.plan.dto.request.PlanIdRequest;
import com.jyp.justplan.domain.plan.dto.request.PlanCreateRequest;
import com.jyp.justplan.domain.plan.dto.request.PlanUpdateRequest;
import com.jyp.justplan.domain.plan.dto.response.PlanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanService {
    private final PlanRepository planRepository;

    /* 플랜 조회 */
    public PlanResponse getPlan(Long planId) {
        Plan plan = planRepository.getById(planId);
        return PlanResponse.toDto(plan, 1);
    }

    /* 플랜 생성 */
    public PlanResponse savePlan(PlanCreateRequest request) {
        Plan plan = planRepository.save(request.toEntity());
        return PlanResponse.toDto(plan, 1);
    }

    /* 플랜 복제 (가져오기) */
    public PlanResponse copyPlan(PlanIdRequest request) {
        Plan origin_plan = planRepository.getById(request.getOriginPlanId());
        String title = origin_plan.getTitle() + " 복사본";
        Plan plan = planRepository.save(new Plan(title,
                origin_plan.getStartDate(),
                origin_plan.getEndDate(),
                origin_plan.getRegion()));
        plan.setOriginPlan(origin_plan);
        return PlanResponse.toDto(plan, 1);
    }

    /* 플랜 수정 (제목, 여행 일자) */
    public PlanResponse updatePlan(PlanUpdateRequest request) {
        Plan plan = planRepository.getById(request.getPlanId());
        plan.updatePlan(request.getTitle(), request.getStartDate(), request.getEndDate());
        return PlanResponse.toDto(plan, 1);
    }

    /* 플랜 공개 여부 수정 */
    public PlanResponse updatePlanPublic(PlanIdRequest request) {
        Plan plan = planRepository.getById(request.getOriginPlanId());
        plan.updatePublic(!plan.isPublic());
        return PlanResponse.toDto(plan, 1);
    }

    /* 플랜 삭제 */
    public void deletePlan(Long planId) {
        Plan plan = planRepository.getById(planId);
        planRepository.delete(plan);
    }
}
