package com.jyp.justplan.domain.plan.application.tag;

import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.tag.PlanTag;
import com.jyp.justplan.domain.plan.domain.tag.PlanTagRepository;
import com.jyp.justplan.domain.plan.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
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
    public List<PlanTag> findTagsByPlan(Plan plan) {
        return planTagRepository.findByPlanOrderByUpdatedAtAsc(plan);
    }

    /* 일정-태그 생성 */
    @Transactional
    public List<PlanTag> savePlanTag(Plan plan, List<String> tags) {
        List<PlanTag> origin_planTags = planTagRepository.findByPlan(plan);
        List<PlanTag> new_planTags = mapPlanTag(plan, tags);

        if (origin_planTags.equals(new_planTags)) {
            return origin_planTags;
        }

        // 없어진 태그 삭제
        Set<Tag> deleted_plan = new HashSet<>();
        origin_planTags.forEach(tag -> {
            if (!new_planTags.contains(tag)) {
                deleted_plan.add(tag.getTag());
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
        deleted_plan.forEach(tag -> {
            if (!planTagRepository.existsByTag(tag)) {
                tagService.deleteTag(tag);
            }
        });

        return planTagRepository.findByPlanOrderByUpdatedAtAsc(plan);
    }

    /* 일정에 대한 태그 매핑 */
    private List<PlanTag> mapPlanTag(Plan plan, List<String> tags) {
        List<PlanTag> planTags = tags.stream()
                .map(tag -> {
                    Tag findTag = tagService.findOrCreateTag(tag);
                    return findOrCreatePlanTag(plan, findTag);
                })
                .collect(Collectors.toList());
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
        List<PlanTag> deletedPlanTags = planTagRepository.deleteByPlan(plan);
        deletedPlanTags.forEach(planTag -> {
            if (!planTagRepository.existsByTag(planTag.getTag())) {
                tagService.deleteTag(planTag.getTag());
            }
        });
    }
}
