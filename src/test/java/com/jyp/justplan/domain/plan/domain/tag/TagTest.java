package com.jyp.justplan.domain.plan.domain.tag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TagTest {
    @Test
    @DisplayName("태그를 생성한다.")
    void 태그를_생성한다 () {
        /* given */
        String name = "태그 이름";
        Tag tag = new Tag(name);

        /* when */
        /* then */
        assertEquals(name, tag.getName());
    }
}