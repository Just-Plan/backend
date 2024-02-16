package com.jyp.justplan.domain.plan.application;

import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.user.domain.User;
import org.springframework.data.domain.Page;

public interface ScrapStore {
    long getScrapCount (Plan plan);
    Page<Plan> getMyScrapList (int page, int size, String sort, User user);
    void scrapPlan (User user, Plan plan, boolean scrap);

    boolean isScrapped (User user, Plan plan);
}
