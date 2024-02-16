package com.jyp.justplan.domain.plan.domain.scrap;

import com.jyp.justplan.domain.plan.application.ScrapStore;
import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.exception.scrap.NoSuchScrapException;
import com.jyp.justplan.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScrapStoreImpl implements ScrapStore {
    private final ScrapRepository scrapRepository;

    @Override
    public long getScrapCount(Plan plan) {
        return scrapRepository.countByPlan(plan);
    }

    @Override
    public Page<Plan> getMyScrapList(int page, int size, String sort, User user) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        return scrapRepository.findAllByUserOrderByCreatedAt(pageable, user);
    }

    @Override
    public void scrapPlan(User user, Plan plan, boolean scrap) {
        if (scrap) {
            if (scrapRepository.existsByUserAndPlan(user, plan)) throw new NoSuchScrapException("이미 스크랩한 일정입니다.");
            scrapRepository.save(new Scrap(user, plan));
        } else {
            Scrap scrapEntity = scrapRepository.findByUserAndPlan(user, plan)
                    .orElseThrow(() -> new NoSuchScrapException("해당 스크랩이 존재하지 않습니다."));
            scrapRepository.delete(scrapEntity);
        }
    }

    @Override
    public boolean isScrapped(User user, Plan plan) {
        return scrapRepository.existsByUserAndPlan(user, plan);
    }
}
