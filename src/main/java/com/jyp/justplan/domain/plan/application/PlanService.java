package com.jyp.justplan.domain.plan.application;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.city.domain.CityRepository;
import com.jyp.justplan.domain.city.dto.response.CityResponse;
import com.jyp.justplan.domain.plan.application.budget.BudgetService;
import com.jyp.justplan.domain.plan.application.expense.ExpenseService;
import com.jyp.justplan.domain.plan.application.tag.PlanTagService;
import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.PlanRepository;
import com.jyp.justplan.domain.plan.domain.tag.PlanTag;
import com.jyp.justplan.domain.plan.domain.tag.Tag;
import com.jyp.justplan.domain.plan.dto.request.PlanIdRequest;
import com.jyp.justplan.domain.plan.dto.request.PlanCreateRequest;
import com.jyp.justplan.domain.plan.dto.request.PlanUpdateRequest;
import com.jyp.justplan.domain.plan.dto.response.BudgetResponse;
import com.jyp.justplan.domain.plan.dto.response.ExpenseResponse;
import com.jyp.justplan.domain.plan.dto.response.PlanDetailResponse;
import com.jyp.justplan.domain.plan.dto.response.PlanResponse;
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
@Transactional(readOnly = true)
public class PlanService {
    private final PlanRepository planRepository;
    private final CityRepository cityRepository;
    private final PlanTagService planTagService;
    private final BudgetService budgetService;
    private final ExpenseService expenseService;

    /* Plan을 통한 PlanResponse 반환 (origin Plan 정보 포함) */
    private PlanDetailResponse getPlanDetailResponse(Plan plan) {
        // 일정에 해당하는 태그 조회
        List<PlanTag> tags = planTagService.findTagsByPlan(plan);
        List<String> tagNames = getTagNames(tags);

        CityResponse cityResponse = new CityResponse(plan.getRegion());

        // 일정에 해당하는 예산 조회
        BudgetResponse budgetResponse = budgetService.getBudget(plan);

        // 일정에 해당하는 지출 조회
        ExpenseResponse expenseResponse = plan.isUseExpense() ? expenseService.getExpense(plan) : null;

        if (plan.getOriginPlan() != null) {
            List<PlanTag> originTags = planTagService.findTagsByPlan(plan.getOriginPlan());
            List<String> originTagNames = originTags.stream()
                    .map(tag -> tag.getTag().getName())
                    .collect(Collectors.toList());

            PlanResponse originPlan = PlanResponse.toDto(plan.getOriginPlan(), originTagNames);
            return PlanDetailResponse.toDto(plan, tagNames, cityResponse, originPlan, budgetResponse, expenseResponse);
        } else {
            return PlanDetailResponse.toDto(plan, tagNames, cityResponse, budgetResponse, expenseResponse);
        }
    }

    private PlanResponse getPlanResponse(Plan plan) {
        List<PlanTag> tags = planTagService.findTagsByPlan(plan);
        List<String> tagNames = getTagNames(tags);

        return PlanResponse.toDto(plan, tagNames);
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
    public PlanDetailResponse getPlan(Long planId) {
        Plan plan = planRepository.getById(planId);
        return getPlanDetailResponse(plan);
    }

    /* 플랜 생성 */
    @Transactional
    public PlanDetailResponse savePlan(PlanCreateRequest request) {
        Plan plan = planRepository.save(request.toEntity());

        City city = cityRepository.getById(request.getRegionId());
        plan.setRegion(city);

        budgetService.createBudget(plan);
        expenseService.createExpense(plan);

        planTagService.savePlanTag(plan, request.getTags());

        return getPlanDetailResponse(plan);
    }

    /* 플랜 복제 (가져오기) */
    @Transactional
    public PlanDetailResponse copyPlan(PlanIdRequest request) {
        Plan origin_plan = planRepository.getById(request.getOriginPlanId());
        String title = origin_plan.getTitle() + " 복사본";

        Plan new_plan =  planRepository.save(new Plan(
                title,
                origin_plan.getStartDate(),
                origin_plan.getEndDate()
                ));

        new_plan.setRegion(origin_plan.getRegion());
        new_plan.setOriginPlan(origin_plan);

        List<PlanTag> origin_tags = planTagService.findTagsByPlan(origin_plan);
        planTagService.savePlanTag(new_plan, getTagNames(origin_tags));

        return getPlanDetailResponse(new_plan);
    }

    private static List<String> getTagNames(List<PlanTag> tags) {
        return tags.stream()
                .map(tag -> tag.getTag().getName())
                .collect(Collectors.toList());
    }

    /* 플랜 수정 (제목, 여행 일자, 태그) */
    /* + 예산, 지출, 공개 여부, 지출 여부 수정 */
    @Transactional
    public PlanDetailResponse updatePlan(PlanUpdateRequest request) {
        Plan plan = planRepository.getById(request.getPlanId());
        List<String> tags = request.getTags();

        // 태그 수정
        planTagService.savePlanTag(plan, tags);

        // 제목, 여행 일자
        plan.updatePlan(request.getTitle(), request.getStartDate(), request.getEndDate());

        // 공개 여부, 지출 여부 수정
        plan.updatePublic(request.isPublished());
        plan.updateUseExpense(request.isUseExpense());

        // 예산 수정
        budgetService.updateBudget(plan, request.getBudget());

        if (plan.isUseExpense()) {
            // 지출 수정
            expenseService.updateExpense(plan, request.getExpense());
        }

        return getPlanDetailResponse(plan);
    }

    /* 플랜 삭제 */
    @Transactional
    public void deletePlan(Long planId) {
        Plan plan = planRepository.getById(planId);

//        planTagService.deletePlanTag(plan);
        planRepository.delete(plan);
    }
}
