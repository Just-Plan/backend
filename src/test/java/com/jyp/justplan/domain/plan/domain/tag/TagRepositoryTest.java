package com.jyp.justplan.domain.plan.domain.tag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TagRepositoryTest {
    @Autowired
    private TagRepository tagRepository;

    private final String 태그_이름 = "태그 이름";

    @Test
    @DisplayName("태그를_저장한다.")
    void 태그를_저장한다 () {
        /* given */
        Tag tag = new Tag(태그_이름);

        /* when */
        Tag savedTag = tagRepository.save(tag);

        /* then */
        assertEquals(태그_이름, savedTag.getName());
    }
    
    @Test
    @DisplayName("태그를_조회한다.")
    void 태그를_조회한다 () {
        /* given */
        Tag tag = new Tag(태그_이름);

        /* when */
        Tag savedTag = tagRepository.save(tag);
        Tag foundTag = tagRepository.getById(savedTag.getId());
        
        /* then */
        assertEquals(태그_이름, foundTag.getName());
    }

    @Test
    @DisplayName("이름을 통해 태그를 조회한다.")
    void 이름을_통해_태그를_조회한다 () {
        /* given */
        Tag tag = new Tag(태그_이름);

        /* when */
        Tag savedTag = tagRepository.save(tag);
        Tag foundTag = tagRepository.findByName(태그_이름).get();

        /* then */
        assertEquals(태그_이름, foundTag.getName());
    }
}