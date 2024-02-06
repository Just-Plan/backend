package com.jyp.justplan.domain.place.dto.request.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "장소 댓글 생성 요청")
public class PlaceCommentCreateRequest {
    @Schema(description = "구글 장소 ID", example = "1")
    @NotNull(message = "장소 ID는 필수 값입니다.")
    private Long placeId;

    @Schema(description = "댓글 내용", example = "여기 아인슈페너가 맛도리입니다")
    @NotBlank(message = "댓글 내용은 필수 값입니다.")
    @Length(max = 300, message = "댓글 내용은 최대 300자까지 입력 가능합니다.")
    private String content;
}
