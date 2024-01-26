package com.jyp.justplan.domain.mbti.domain;

import static javax.persistence.EnumType.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "mbti_answer")
@Entity
@NoArgsConstructor
@Getter
public class MbtiAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "answer", nullable = false, length = 100)
    private String answer;

    @Column(name = "attribute", nullable = false, length = 1)
    @Enumerated(STRING)
    private MbtiAnswerType attribute;
}