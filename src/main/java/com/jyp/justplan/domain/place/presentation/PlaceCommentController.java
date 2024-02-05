package com.jyp.justplan.domain.place.presentation;

import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.place.application.PlaceCommentService;
import com.jyp.justplan.domain.place.dto.request.comment.PlaceCommentCreateRequest;
import com.jyp.justplan.domain.place.dto.request.comment.PlaceCommentUpdateRequest;
import com.jyp.justplan.domain.place.dto.response.PlaceCommentResponse;
import com.jyp.justplan.domain.user.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PlaceComment", description = "장소 댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place/comment")
public class PlaceCommentController {
    private final PlaceCommentService placeCommentService;

    /* 장소 댓글 생성 */
    @Operation(summary = "장소 댓글 생성", description = "장소에 대한 댓글을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @PostMapping
    public ApiResponseDto<PlaceCommentResponse> createPlaceComment(
            @Parameter(description = "장소 댓글 생성을 위한 데이터", required = true)
            @RequestBody PlaceCommentCreateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        PlaceCommentResponse response = placeCommentService.createPlaceComment(request, userDetails.getUsername());
        return ApiResponseDto.successResponse(response);
    }

    /* 장소 댓글 조회 */
    @Operation(summary = "장소 댓글 조회", description = "장소에 대한 댓글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @GetMapping("/{placeId}")
    public ApiResponseDto<List<PlaceCommentResponse>> getPlaceComments(
            @Parameter(description = "장소 ID", required = true)
            @PathVariable Long placeId
    ) {
        List<PlaceCommentResponse> response = placeCommentService.getPlaceComments(placeId);
        return ApiResponseDto.successResponse(response);
    }

    /* 장소 댓글 수정 */
    @Operation(summary = "장소 댓글 수정", description = "장소에 대한 댓글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @PatchMapping
    public ApiResponseDto<PlaceCommentResponse> updatePlaceComment(
            @Parameter(description = "장소 댓글 수정을 위한 데이터", required = true)
            @RequestBody PlaceCommentUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        PlaceCommentResponse response = placeCommentService.updatePlaceComment(request, userDetails.getUsername());
        return ApiResponseDto.successResponse(response);
    }

    /* 장소 댓글 삭제 */
    @Operation(summary = "장소 댓글 삭제", description = "장소에 대한 댓글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @DeleteMapping("/{placeCommentId}")
    public ApiResponseDto<?> deletePlaceComment(
            @Parameter(description = "장소 댓글 ID", required = true)
            @PathVariable Long placeCommentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        placeCommentService.deletePlaceComment(placeCommentId, userDetails.getUsername());
        return ApiResponseDto.successWithoutDataResponse();
    }
}
