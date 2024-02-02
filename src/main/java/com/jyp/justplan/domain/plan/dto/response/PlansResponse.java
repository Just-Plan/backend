package com.jyp.justplan.domain.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class PlansResponse<T> {
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int size;
    private List<T> plans;
}
