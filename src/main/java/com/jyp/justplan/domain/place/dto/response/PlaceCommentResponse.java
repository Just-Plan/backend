package com.jyp.justplan.domain.place.dto.response;

import com.jyp.justplan.domain.place.domain.PlaceComment;
import com.jyp.justplan.domain.user.dto.response.UserWithMbtiResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceCommentResponse {
    private Long placeCommentId;
    private UserWithMbtiResponse user;
    private String content;
    private ZonedDateTime createdAt;

    public static PlaceCommentResponse toDto(PlaceComment placeComment) {
        return new PlaceCommentResponse(
                placeComment.getId(),
                UserWithMbtiResponse.toDto(placeComment.getUser()),
                placeComment.getContent(),
                placeComment.getCreatedAt()
        );
    }
}
