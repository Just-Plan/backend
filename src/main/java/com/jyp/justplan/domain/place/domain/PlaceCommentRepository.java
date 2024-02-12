package com.jyp.justplan.domain.place.domain;

import com.jyp.justplan.domain.place.exception.NoSuchPlaceCommentException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceCommentRepository extends JpaRepository<PlaceComment, Long> {
    List<PlaceComment> findByPlaceOrderByCreatedAtDesc(GooglePlace place);

    default PlaceComment getById(Long id) {
        return findById(id).orElseThrow(() -> new NoSuchPlaceCommentException("해당 댓글 정보가 존재하지 않습니다."));
    }

    List<PlaceComment> findAllByPlaceId(Long findGooglePlaceId);
}
