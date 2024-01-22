package com.jyp.justplan.domain.mbti.domain;

import static javax.persistence.EnumType.STRING;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<MbtiAnswer> answers = new ArrayList<>();


    public static List<MbtiQuestion> selectRandomQuestions(List<MbtiQuestion> allQuestions) {
        Random random = new Random();
        return Arrays.stream(MbtiQuestionType.values())
            .flatMap(type -> allQuestions.stream()
                .filter(question -> question.getAttribute() == type)
                .sorted((q1, q2) -> random.nextInt(2) - 1)
                .limit(3))
            .toList();
    }
}
