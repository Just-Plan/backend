package com.jyp.justplan.domain.plan.application.tag;

import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.tag.PlanTag;
import com.jyp.justplan.domain.plan.domain.tag.PlanTagRepository;
import com.jyp.justplan.domain.plan.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanTagService {
    private final TagService tagService;
    private final PlanTagRepository planTagRepository;

    /* 태그 조회 */
    public Set<String> findTagsByPlan(Plan plan) {
        Set<String> tags = planTagRepository.findByPlan(plan).stream()
                .map(planTag -> planTag.getTag().getName())
                .collect(Collectors.toSet());
        return tags;
    }

    /* 일정-태그 생성 */
    @Transactional
    public Set<PlanTag> savePlanTag(Plan plan, Set<String> tags) {
        Set<PlanTag> origin_planTags = planTagRepository.findByPlan(plan);
        Set<PlanTag> new_planTags = mapPlanTag(plan, tags);

        if (origin_planTags.equals(new_planTags)) {
            return origin_planTags;
        }

        // 없어진 태그 삭제
        origin_planTags.forEach(tag -> {
            if (!new_planTags.contains(tag)) {
                planTagRepository.delete(tag);
            }
        });

        // 새로운 태그 추가
        new_planTags.forEach(tag -> {
            if (!origin_planTags.contains(tag)) {
                planTagRepository.save(tag);
            }
        });

        // 사용하지 않는 태그 삭제
        origin_planTags.forEach(tag -> {
            if (!planTagRepository.existsByTag(tag.getTag())) {
                tagService.deleteTag(tag.getTag());
            }
        });

        return new_planTags;
    }

    /* 일정에 대한 태그 매핑 */
    private Set<PlanTag> mapPlanTag(Plan plan, Set<String> tags) {
        Set<PlanTag> planTags = tags.stream()
                .map(tag -> {
                    Tag findTag = tagService.findOrCreateTag(tag);
                    return findOrCreatePlanTag(plan, findTag);
                })
                .collect(Collectors.toSet());
        return planTags;
    }

    /* 일정-태그가 존재하면 해당 일정-태그를 반환하고, 존재하지 않으면 생성 후 반환 */
    @Transactional
    public PlanTag findOrCreatePlanTag(Plan plan, Tag findTag) {
        Optional<PlanTag> existingPlanTag = planTagRepository.findByPlanAndTag(plan, findTag);

        return existingPlanTag.orElseGet(() -> {
            PlanTag newPlanTag = new PlanTag(plan, findTag);
            return planTagRepository.save(newPlanTag);
        });
    }

    /* 일정-태그 삭제 */
    @Transactional
    public void deletePlanTag(Plan plan) {
        Set<PlanTag> deletedPlanTags = planTagRepository.deleteByPlan(plan);
        deletedPlanTags.forEach(planTag -> {
            if (!planTagRepository.existsByTag(planTag.getTag())) {
                tagService.deleteTag(planTag.getTag());
            }
        });
    }
}
