package com.jyp.justplan.domain.mbti.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "mbti_question")
@Entity
@NoArgsConstructor
@Getter
public class MbtiQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "question", nullable = false, length = 50)
    private String question;

    @Column(name = "attribute", nullable = false, length = 2)
    private String attribute;

    @OneToMany(mappedBy = "question")
    private List<MbtiAnswer> answers = new ArrayList<>();
}
