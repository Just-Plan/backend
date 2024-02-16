package com.jyp.justplan.domain.plan.application;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.city.domain.CityRepository;
import com.jyp.justplan.domain.city.dto.response.CityResponse;
import com.jyp.justplan.domain.mbti.exception.MbtiNotFoundException;
import com.jyp.justplan.domain.place.domain.GooglePlaceStore;
import com.jyp.justplan.domain.place.domain.Place;
import com.jyp.justplan.domain.place.domain.PlaceStore;
import com.jyp.justplan.domain.plan.application.budget.BudgetService;
import com.jyp.justplan.domain.plan.application.expense.ExpenseService;
import com.jyp.justplan.domain.plan.application.tag.PlanTagService;
import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.PlanRepository;
import com.jyp.justplan.domain.plan.domain.UserPlan;
import com.jyp.justplan.domain.plan.domain.tag.PlanTag;
import com.jyp.justplan.domain.plan.dto.request.*;
import com.jyp.justplan.domain.plan.dto.response.*;
import com.jyp.justplan.domain.plan.exception.PlanValidationException;
import com.jyp.justplan.domain.user.UserDetailsImpl;
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
    private final PlaceStore placeStore;
    private final GooglePlaceStore googlePlaceStore;


    /* Plan을 통한 PlanResponse 반환 (origin Plan 정보 포함) */
    private PlanDetailResponse getPlanDetailResponse(Plan plan, User user) {
        // 일정에 해당하는 태그 조회
        List<PlanTag> tags = planTagService.findTagsByPlan(plan);
        List<String> tagNames = getTagNames(tags);

        List<UserPlanResponse> users = getUserPlanResponses(findUsersByPlan(plan));

        Place place = placeStore.getRandomPlaceById(plan.getId());
        String photoUrl = place != null
                ? googlePlaceStore.getGooglePlaceById(place.getGooglePlace().getId()).getPhotoReference()
                : null;

        long scrapCount = scrapStore.getScrapCount(plan);
        Boolean scrapped = user == null ? null : scrapStore.isScrapped(user, plan);

        CityResponse cityResponse = new CityResponse(plan.getRegion());

        if (plan.getOriginPlan() != null) {
            Plan originPlan = plan.getOriginPlan();
            List<UserPlanResponse> originUsers = getUserPlanResponses(findUsersByPlan(originPlan));

            Place originPlace = placeStore.getRandomPlaceById(originPlan.getId());
            String originPhotoUrl = originPlace != null
                    ? googlePlaceStore.getGooglePlaceById(originPlace.getGooglePlace().getId()).getPhotoReference()
                    : null;

            OriginPlanResponse originPlanResponse = OriginPlanResponse.toDto(originPlan, originUsers);
            return PlanDetailResponse.toDto(plan, users, originPhotoUrl, scrapped, scrapCount, tagNames, cityResponse, originPlanResponse);
        } else {
            return PlanDetailResponse.toDto(plan, users, photoUrl, scrapped, scrapCount, tagNames, cityResponse);
        }
    }

    private PlanResponse getPlanResponse(Plan plan, User user) {
        List<PlanTag> tags = planTagService.findTagsByPlan(plan);
        List<String> tagNames = getTagNames(tags);
        List<UserPlanResponse> users = findUsersByPlan(plan).stream()
                .map(UserPlanResponse::toDto)
                .collect(Collectors.toList());

        Place place = placeStore.getRandomPlaceById(plan.getId());
        String photoUrl = place != null
                ? googlePlaceStore.getGooglePlaceById(place.getGooglePlace().getId()).getPhotoReference()
                : null;

        long scrapCount = scrapStore.getScrapCount(plan);
        Boolean scrapped = user == null ? null : scrapStore.isScrapped(user, plan);

        return PlanResponse.toDto(plan, users, photoUrl, scrapped, scrapCount, tagNames);
    }

    private PlansResponse getPlansResponse(Page<Plan> plans, User user) {
        List<PlanResponse> planResponses = plans.stream()
                .map(plan -> getPlanResponse(plan, user))
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
    public PlansResponse getPlans(PlanReadRequest request, long regionId, int page, int size, String sort, UserDetailsImpl userDetails) {
        List<String> mbti = request.getMbti();
        Pageable pageable = PageRequest.of(page, size);
        PlansResponse plansResponse;
        User user = userDetails != null ? userService.findByEmail(userDetails.getUsername()) : null;

        for (int i = 0; i < mbti.size(); i++) {
            String m = mbti.get(i);
            mbti.set(i, m.toUpperCase());
            if (!validateMbti(m)) throw new MbtiNotFoundException("존재하지 않는 MBTI입니다.");
        }

//        if (sort.equals("scrapCnt")) {
            pageable = PageRequest.of(page, size);

            if (mbti.isEmpty() && regionId == 0) {
                Page<Plan> plans = planRepository.findAllOrderByScrapCnt(pageable);
                plansResponse = getPlansResponse(plans, user);
            } else if (mbti.isEmpty()) {
                City region = cityRepository.getById(regionId);
                Page<Plan> plans = planRepository.findAllByRegionOrderByScrapCnt(pageable, region);
                plansResponse = getPlansResponse(plans, user);
            } else if (regionId == 0) {
                Page<Plan> plans = planRepository.findAllByMbtiOrderByScrapCnt(pageable, mbti);
                plansResponse = getPlansResponse(plans, user);
            } else {
                City region = cityRepository.getById(regionId);
                Page<Plan> plans = planRepository.findAllByMbtiAndRegionOrderByScrapCnt(pageable, mbti, region);
                plansResponse = getPlansResponse(plans, user);
            }
//        }
        return plansResponse;
    }

    private boolean validateMbti (String mbti) {
        if (mbti.length() != 4) return false;

        mbti = mbti.toUpperCase();
        char first = mbti.charAt(0);
        char second = mbti.charAt(1);
        char third = mbti.charAt(2);
        char fourth = mbti.charAt(3);

        if ((first != 'I' && first != 'E') ||
                (second != 'N' && second != 'S') ||
                (third != 'T' && third != 'F') ||
                (fourth != 'J' && fourth != 'P')) {
            return false;
        }
        return true;
    }


    /* 플랜 단일 조회 */
    public PlanDetailResponse getPlan(Long planId, UserDetailsImpl userDetails) {
        Plan plan = planRepository.getById(planId);
        User user = userDetails != null ? userService.findByEmail(userDetails.getUsername()) : null;

        return getPlanDetailResponse(plan, user);
    }

    /* 나의 플랜 조회 */
    public PlansResponse getMyPlans(int page, int size, String sort, UserDetailsImpl userDetails) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        User user = userService.findByEmail(userDetails.getUsername());

        Page<Plan> plans = planRepository.findAllByUser(pageable, user);

        return getPlansResponse(plans, user);
    }

    /* 나의 플랜 조회 (가계부 포함) */
    public List<PlanWithAccountBookResponse> getMyPlansWithAccount(String email) {
        User user = userService.findByEmail(email);

        List<Plan> plans = planRepository.findAllByUser(user);

        List<PlanWithAccountBookResponse> planResponses =
                plans.stream()
                .map(plan -> {
                    return PlanWithAccountBookResponse.toDto(plan);
                })
                .collect(Collectors.toList());

        return planResponses;
    }

    /* 나의 스크랩 플랜 조회 */
    public PlansResponse getMyScrapPlans(int page, int size, String sort, String userEmail) {
        User user = userService.findByEmail(userEmail);

        Page<Plan> plans = scrapStore.getMyScrapList(page, size, sort, user);

        return getPlansResponse(plans, user);
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

        return getPlanDetailResponse(plan, user);
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

        return getPlanDetailResponse(new_plan, user);
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

        // 예산 및 지출 수정
        budgetService.updateBudget(plan.getBudget(), request.getBudget());
        expenseService.updateExpense(plan.getExpense(), request.getExpense());

        return getPlanDetailResponse(plan, user);
    }

    /* 플랜 스크랩 */
    @Transactional
    public PlanResponse scrapPlan(PlanScrapRequest request, String userEmail) {
        Plan plan = planRepository.getById(request.getPlanId());
        User user = userService.findByEmail(userEmail);

        scrapStore.scrapPlan(user, plan, request.isScrap());

        return getPlanResponse(plan, user);
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

    private UserPlan findOwnerByPlan(Plan plan) {
        UserPlan owner = plan.getUsers()
                .stream()
                .filter(UserPlan::isOwner)
                .findFirst()
                .orElseThrow(() -> new PlanValidationException("해당 일정의 소유자가 없습니다."));
        return owner;
    }

    private List<UserPlan> findUsersByPlan(Plan plan) {
        return plan.getUsers();
    }

    private List<UserPlanResponse> getUserPlanResponses(List<UserPlan> userPlans) {
        return userPlans.stream()
                .map(UserPlanResponse::toDto)
                .collect(Collectors.toList());
    }

    public void validateOwnerOfPlan (Plan plan, User user) {
        User owner = findOwnerByPlan(plan).getUser();
        if (!owner.equals(user)) {
            throw new PlanValidationException("해당 일정의 소유자가 아닙니다.");
        }
    }

    public void validateUserOfPlan (Plan plan, User user) {
        List<User> users = findUsersByPlan(plan)
                .stream()
                .map(UserPlan::getUser)
                .collect(Collectors.toList());

        if (!users.contains(user)) {
            throw new PlanValidationException("해당 일정의 참여자가 아닙니다.");
        }
    }
}
