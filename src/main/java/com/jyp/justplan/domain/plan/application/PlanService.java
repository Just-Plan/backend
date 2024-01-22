package com.jyp.justplan.domain.plan.application;

import com.jyp.justplan.domain.plan.application.tag.PlanTagService;
import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.PlanRepository;
import com.jyp.justplan.domain.plan.domain.tag.Tag;
import com.jyp.justplan.domain.plan.dto.request.PlanIdRequest;
import com.jyp.justplan.domain.plan.dto.request.PlanCreateRequest;
import com.jyp.justplan.domain.plan.dto.request.PlanUpdateRequest;
import com.jyp.justplan.domain.plan.dto.response.PlanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {
    private final PlanRepository planRepository;
    private final PlanTagService planTagService;

    /* Plan을 통한 PlanResponse 반환 (origin Plan 정보 포함) */
    private PlanResponse getPlanResponse(Plan plan) {
        Set<String> tags = planTagService.findTagsByPlan(plan);

        if (plan.getOriginPlan() != null) {
            Set<String> originTags = planTagService.findTagsByPlan(plan.getOriginPlan());
            PlanResponse originPlan = PlanResponse.toDto(plan.getOriginPlan(), originTags);
            return PlanResponse.toDto(plan, tags, originPlan);
        } else {
            return PlanResponse.toDto(plan, tags);
        }
    }

    /* 전체 플랜 조회 */
    public List<PlanResponse> getPlans(String type, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());

        Page<Plan> plans = planRepository.findAll(pageable);

        List<PlanResponse> planResponses = plans.stream()
                .map(this::getPlanResponse)
                .collect(Collectors.toList());

        return planResponses;
    }

    /* 플랜 단일 조회 */
    public PlanResponse getPlan(Long planId) {
        Plan plan = planRepository.getById(planId);
        return getPlanResponse(plan);
    }

    /* 플랜 생성 */
    @Transactional
    public PlanResponse savePlan(PlanCreateRequest request) {
        Plan plan = planRepository.save(request.toEntity());

        planTagService.savePlanTag(plan, request.getTags());

        return getPlanResponse(plan);
    }

    /* 플랜 복제 (가져오기) */
    @Transactional
    public PlanResponse copyPlan(PlanIdRequest request) {
        Plan origin_plan = planRepository.getById(request.getOriginPlanId());
        String title = origin_plan.getTitle() + " 복사본";
        Plan plan = planRepository.save(new Plan(title,
                origin_plan.getStartDate(),
                origin_plan.getEndDate(),
                origin_plan.getRegion()));
        plan.setOriginPlan(origin_plan);

        Set<String> origin_tags = planTagService.findTagsByPlan(origin_plan);
        planTagService.savePlanTag(plan, origin_tags);

        return getPlanResponse(plan);
    }

    /* 플랜 수정 (제목, 여행 일자, 태그) */
    @Transactional
    public PlanResponse updatePlan(PlanUpdateRequest request) {
        Plan plan = planRepository.getById(request.getPlanId());
        Set<String> tags = request.getTags();

        planTagService.savePlanTag(plan, tags);

        plan.updatePlan(request.getTitle(), request.getStartDate(), request.getEndDate());

        return getPlanResponse(plan);
    }

    /* 플랜 공개 여부 수정 */
    @Transactional
    public PlanResponse updatePlanPublic(PlanIdRequest request) {
        Plan plan = planRepository.getById(request.getOriginPlanId());
        plan.updatePublic(!plan.isPublic());
        return getPlanResponse(plan);
    }

    /* 플랜 삭제 */
    @Transactional
    public void deletePlan(Long planId) {
        Plan plan = planRepository.getById(planId);

//        planTagService.deletePlanTag(plan);
        planRepository.delete(plan);
    }
}
