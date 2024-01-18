package com.jyp.justplan.domain.memo.dto.request;

import com.jyp.justplan.domain.memo.domain.Memo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemoRequestDto {

    private String content;

    public Memo toEntity() {
        return Memo.builder()
                .content(content)
                .build();
    }
}
