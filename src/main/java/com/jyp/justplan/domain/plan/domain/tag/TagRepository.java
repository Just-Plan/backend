package com.jyp.justplan.domain.plan.domain.tag;

import com.jyp.justplan.domain.plan.exception.tag.NoSuchTagException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    default Tag getByName(String name) {
        return findByName(name).orElseThrow(() -> new NoSuchTagException("해당 이름의 태그가 존재하지 않습니다."));
    }
}
