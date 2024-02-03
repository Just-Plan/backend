package com.jyp.justplan.domain.place.dto.response;

import com.jyp.justplan.domain.place.domain.Memo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemoResponse {

    private String content;
    private String color;

    public static MemoResponse of(Memo memo) {
        return new MemoResponse(
                memo.getContent(),
                memo.getColor()
        );
    }
}
