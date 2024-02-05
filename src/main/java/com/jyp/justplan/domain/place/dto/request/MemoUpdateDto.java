package com.jyp.justplan.domain.place.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemoUpdateDto {

    private String content;
    private String color;
}
