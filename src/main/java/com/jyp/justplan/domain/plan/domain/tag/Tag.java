package com.jyp.justplan.domain.plan.domain.tag;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    private static final int MAX_NAME_LENGTH = 20;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "tag_name", nullable = false)
    private String name;

    public Tag(String name) {
        this.name = name;
    }
}
