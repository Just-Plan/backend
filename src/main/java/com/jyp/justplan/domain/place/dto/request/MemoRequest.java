package com.jyp.justplan.domain.place.dto.request;

import com.jyp.justplan.domain.place.domain.Memo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemoRequest {

    private String content;
    private String color;

    public Memo toEntity() {
        return Memo.builder()
                .content(content)
                .color(color)
                .build();
    }
}
