package com.jyp.justplan.domain.place.dto.response;

import com.jyp.justplan.domain.place.domain.Memo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemoResponse {

    private Long id;
    private String content;
    private String color;
    private ZonedDateTime updatedAt;

    public static MemoResponse of(Memo memo) {
        return new MemoResponse(
                memo.getId(),
                memo.getContent(),
                memo.getColor(),
                memo.getUpdatedAt()
        );
    }

}
