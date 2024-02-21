package com.jyp.justplan.domain.place.application;

import com.jyp.justplan.domain.place.domain.GooglePlace;
import com.jyp.justplan.domain.place.domain.GooglePlaceRepository;
import com.jyp.justplan.domain.place.domain.Place;
import com.jyp.justplan.domain.place.domain.PlaceComment;
import com.jyp.justplan.domain.place.domain.PlaceCommentRepository;
import com.jyp.justplan.domain.place.domain.PlaceRepository;
import com.jyp.justplan.domain.place.dto.request.comment.PlaceCommentCreateRequest;
import com.jyp.justplan.domain.place.dto.request.comment.PlaceCommentUpdateRequest;
import com.jyp.justplan.domain.place.dto.response.PlaceCommentResponse;
import com.jyp.justplan.domain.place.dto.response.SchedulePlacesResponse.PlaceResponse;
import com.jyp.justplan.domain.plan.exception.scrap.NoSuchScrapException;
import com.jyp.justplan.domain.user.application.UserService;
import com.jyp.justplan.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceCommentService {
    private final PlaceCommentRepository placeCommentRepository;
    private final GooglePlaceRepository googlePlaceRepository;
    private final UserService userService;
    private final PlaceRepository placeRepository;

    /* 장소 댓글 생성 */
    @Transactional
    public PlaceCommentResponse createPlaceComment(PlaceCommentCreateRequest request, String email) {
        User user = userService.findByEmail(email);
        GooglePlace place = googlePlaceRepository.findById(request.getPlaceId())
                .orElseThrow(() -> new NoSuchScrapException("해당 장소 정보가 존재하지 않습니다."));

        PlaceComment placeComment = new PlaceComment(place, user, request.getContent());
        placeCommentRepository.save(placeComment);

        return PlaceCommentResponse.toDto(placeComment);
    }

    /* 장소 댓글 조회 */
    public List<PlaceCommentResponse> getPlaceComments(Long placeId) {
        Place findPlace = placeRepository.findById(placeId)
            .orElseThrow(() -> new NoSuchScrapException("해당 장소 정보가 존재하지 않습니다."));

        GooglePlace googlePlace = findPlace.getGooglePlace();

        List<PlaceComment> placeComments = placeCommentRepository.findByPlaceOrderByCreatedAtDesc(googlePlace);

        return getPlaceCommentsResponse(placeComments);
    }

    private static List<PlaceCommentResponse> getPlaceCommentsResponse(List<PlaceComment> placeComments) {
        return placeComments.stream()
                .map(PlaceCommentResponse::toDto)
                .toList();
    }

    /* 장소 댓글 수정 */
    @Transactional
    public PlaceCommentResponse updatePlaceComment(PlaceCommentUpdateRequest request, String email) {
        User user = userService.findByEmail(email);
        PlaceComment placeComment = placeCommentRepository.getById(request.getCommentId());

        validatePlaceCommentWriter(placeComment, user);

        placeComment.updateContent(request.getContent());

        return PlaceCommentResponse.toDto(placeComment);
    }

    /* 장소 댓글 삭제 */
    @Transactional
    public void deletePlaceComment(Long commentId, String email) {
        User user = userService.findByEmail(email);
        PlaceComment placeComment = placeCommentRepository.getById(commentId);

        validatePlaceCommentWriter(placeComment, user);

        placeCommentRepository.delete(placeComment);
    }


    public void validatePlaceCommentWriter(PlaceComment placeComment, User user) {
        if (!placeComment.getUser().equals(user)) {
            throw new NoSuchScrapException("해당 댓글의 작성자가 아닙니다.");
        }
    }
}
