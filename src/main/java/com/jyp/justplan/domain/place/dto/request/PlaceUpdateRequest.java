package com.jyp.justplan.domain.place.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlaceUpdateRequest {
    private Long placeId;
    private Integer orderNum;
    private MemoUpdateDto memo;
}
