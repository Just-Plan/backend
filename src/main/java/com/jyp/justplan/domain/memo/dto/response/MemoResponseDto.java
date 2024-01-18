package com.jyp.justplan.domain.memo.dto.response;

import com.jyp.justplan.domain.memo.domain.Memo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemoResponseDto {

    private Long id;
    private String content;
    private String color;
    private ZonedDateTime updatedAt;

    public static MemoResponseDto of(Memo memo) {
        return new MemoResponseDto(
                memo.getId(),
                memo.getContent(),
                memo.getColor(),
                memo.getUpdatedAt()
        );
    }

}
