package com.jyp.justplan.domain.mbti.domain;

import static javax.persistence.EnumType.STRING;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "mbti_question")
@Entity
@NoArgsConstructor
@Getter
@ToString
public class MbtiQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "question", nullable = false, length = 50)
    private String question;

    @Column(name = "attribute", nullable = false, length = 2)
    @Enumerated(STRING)
    private MbtiQuestionType attribute;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "question_id", updatable = false, nullable = false)
    private List<MbtiAnswer> answers = new ArrayList<>();

    public static List<MbtiQuestion> selectRandomQuestions(List<MbtiQuestion> allQuestions, int count) {
        List<MbtiQuestion> randomQuestions = new ArrayList<>();

        for (MbtiQuestionType type : MbtiQuestionType.values()) {
            List<MbtiQuestion> questionsOfType = allQuestions.stream()
                .filter(question -> question.getAttribute() == type)
                .collect(Collectors.toList());

            Collections.shuffle(questionsOfType);

            randomQuestions.addAll(questionsOfType.stream()
                .limit(count)
                .toList());
        }
        return randomQuestions;
    }
}