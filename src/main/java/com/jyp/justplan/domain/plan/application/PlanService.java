package com.jyp.justplan.domain.plan.application;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.city.domain.CityRepository;
import com.jyp.justplan.domain.city.dto.response.CityResponse;
import com.jyp.justplan.domain.plan.application.budget.BudgetService;
import com.jyp.justplan.domain.plan.application.expense.ExpenseService;
import com.jyp.justplan.domain.plan.application.tag.PlanTagService;
import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.PlanRepository;
import com.jyp.justplan.domain.plan.domain.scrap.Scrap;
import com.jyp.justplan.domain.plan.domain.scrap.ScrapStore;
import com.jyp.justplan.domain.plan.domain.tag.PlanTag;
import com.jyp.justplan.domain.plan.dto.request.PlanIdRequest;
import com.jyp.justplan.domain.plan.dto.request.PlanCreateRequest;
import com.jyp.justplan.domain.plan.dto.request.PlanScrapRequest;
import com.jyp.justplan.domain.plan.dto.request.PlanUpdateRequest;
import com.jyp.justplan.domain.plan.dto.response.*;
import com.jyp.justplan.domain.plan.exception.PlanValidationException;
import com.jyp.justplan.domain.user.application.UserService;
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
@Transactional(readOnly = true)
public class PlanService {
    private final PlanRepository planRepository;
    private final CityRepository cityRepository;
    private final PlanTagService planTagService;
    private final BudgetService budgetService;
    private final ExpenseService expenseService;
    private final UserPlanService userPlanService;
    private final UserService userService;
    private final ScrapStore scrapStore;

    /* Plan을 통한 PlanResponse 반환 (origin Plan 정보 포함) */
    private PlanDetailResponse getPlanDetailResponse(Plan plan) {
        // 일정에 해당하는 태그 조회
        List<PlanTag> tags = planTagService.findTagsByPlan(plan);
        List<String> tagNames = getTagNames(tags);

        List<UserPlanResponse> users = userPlanService.findUserPlanResponsesByPlan(plan);

        long scrapCount = scrapStore.getScrapCount(plan);

        CityResponse cityResponse = new CityResponse(plan.getRegion());

        // 일정에 해당하는 예산 조회
        BudgetResponse budgetResponse = budgetService.getBudget(plan);

        // 일정에 해당하는 지출 조회
        ExpenseResponse expenseResponse = expenseService.getExpense(plan);

        if (plan.getOriginPlan() != null) {
            Plan originPlan = plan.getOriginPlan();
            List<UserPlanResponse> originUsers = userPlanService.findUserPlanResponsesByPlan(originPlan);

            OriginPlanResponse originPlanResponse = OriginPlanResponse.toDto(originPlan, originUsers);
            return PlanDetailResponse.toDto(plan, users, scrapCount, tagNames, cityResponse, originPlanResponse, budgetResponse, expenseResponse);
        } else {
            return PlanDetailResponse.toDto(plan, users, scrapCount, tagNames, cityResponse, budgetResponse, expenseResponse);
        }
    }

    private PlanResponse getPlanResponse(Plan plan) {
        List<PlanTag> tags = planTagService.findTagsByPlan(plan);
        List<String> tagNames = getTagNames(tags);
        List<UserPlanResponse> users = userPlanService.findUserPlanResponsesByPlan(plan);
        long scrapCount = scrapStore.getScrapCount(plan);
        BudgetResponse budgetResponse = budgetService.getBudget(plan);

        return PlanResponse.toDto(plan, users, scrapCount, budgetResponse, tagNames);
    }

    private PlansResponse getPlansResponse(Page<Plan> plans) {
        List<PlanResponse> planResponses = plans.stream()
                .map(this::getPlanResponse)
                .collect(Collectors.toList());

        PlansResponse plansResponse = new PlansResponse(
                plans.getTotalElements(),
                plans.getTotalPages(),
                plans.getNumber(),
                plans.getSize(),
                planResponses
        );

        return plansResponse;
    }

    /* 전체 플랜 조회 */
    public PlansResponse getPlans(String type, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<Plan> plans = planRepository.findAll(pageable);

        return getPlansResponse(plans);
    }


    /* 플랜 단일 조회 */
    public PlanDetailResponse getPlan(Long planId) {
        Plan plan = planRepository.getById(planId);
        return getPlanDetailResponse(plan);
    }

    /* 나의 플랜 조회 */
    public PlansResponse getMyPlans(int page, int size, String sort, String userEmail) {
        User user = userService.findByEmail(userEmail);

        Page<Plan> plans = userPlanService.findPlansByUser(page, size, sort, user);

        return getPlansResponse(plans);
    }

    /* 나의 스크랩 플랜 조회 */
    public PlansResponse getMyScrapPlans(int page, int size, String sort, String userEmail) {
        User user = userService.findByEmail(userEmail);

        Page<Plan> plans = scrapStore.getMyScrapList(page, size, sort, user);

        return getPlansResponse(plans);
    }

    /* 플랜 생성 */
    @Transactional
    public PlanDetailResponse savePlan(PlanCreateRequest request, String userEmail) {
        Plan plan = planRepository.save(request.toEntity());
        User user = userService.findByEmail(userEmail);

        City city = cityRepository.getById(request.getRegionId());
        plan.setRegion(city);

        userPlanService.saveUserPlan(user, plan, true);

        budgetService.createBudget(plan);
        expenseService.createExpense(plan);

        planTagService.savePlanTag(plan, request.getTags());

        return getPlanDetailResponse(plan);
    }

    /* 플랜 복제 (가져오기) */
    @Transactional
    public PlanDetailResponse copyPlan(PlanIdRequest request, String userEmail) {
        Plan origin_plan = planRepository.getById(request.getPlanId());
        User user = userService.findByEmail(userEmail);

        String title = origin_plan.getTitle() + " 복사본";

        Plan new_plan =  planRepository.save(new Plan(
                title,
                origin_plan.getStartDate(),
                origin_plan.getEndDate()
                ));

        new_plan.setRegion(origin_plan.getRegion());
        new_plan.setOriginPlan(origin_plan);

        userPlanService.saveUserPlan(user, new_plan, true);

        List<PlanTag> origin_tags = planTagService.findTagsByPlan(origin_plan);
        planTagService.savePlanTag(new_plan, getTagNames(origin_tags));

        budgetService.createBudget(new_plan);
        expenseService.createExpense(new_plan);

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
    public PlanDetailResponse updatePlan(PlanUpdateRequest request, String userEmail) {
        Plan plan = planRepository.getById(request.getPlanId());
        User user = userService.findByEmail(userEmail);

        validateUserOfPlan(plan, user);

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

    /* 플랜 스크랩 */
    @Transactional
    public void scrapPlan(PlanScrapRequest request, String userEmail) {
        Plan plan = planRepository.getById(request.getPlanId());
        User user = userService.findByEmail(userEmail);

        scrapStore.scrapPlan(user, plan, request.isScrap());
    }

    /* 플랜 삭제 */
    @Transactional
    public void deletePlan(Long planId, String userEmail) {
        Plan plan = planRepository.getById(planId);
        User user = userService.findByEmail(userEmail);

        validateOwnerOfPlan(plan, user);

//        planTagService.deletePlanTag(plan);
        planRepository.delete(plan);
    }

    public void validateOwnerOfPlan (Plan plan, User user) {
        if (!userPlanService.findOwnerByPlan(plan).equals(user)) {
            throw new PlanValidationException("해당 일정의 소유자가 아닙니다.");
        }
    }

    public void validateUserOfPlan (Plan plan, User user) {
        if (!userPlanService.findUsersByPlan(plan).contains(user)) {
            throw new PlanValidationException("해당 일정의 참여자가 아닙니다.");
        }
    }
}
