package com.jyp.justplan.domain.plan.application.tag;

import com.jyp.justplan.domain.plan.domain.tag.Tag;
import com.jyp.justplan.domain.plan.domain.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {
    private final TagRepository tagRepository;

    /* 태그가 존재하면 해당 태그를 반환하고, 존재하지 않으면 생성 후 반환 */
    @Transactional
    public Tag findOrCreateTag(String name) {
        Optional<Tag> existingTag = tagRepository.findByName(name);

        return existingTag.orElseGet(() -> {
            Tag newTag = new Tag(name);
            return tagRepository.save(newTag);
        });
    }

    /* 태그 삭제 */
    @Transactional
    public void deleteTag(Tag tag) {
        tagRepository.delete(tag);
    }
}
