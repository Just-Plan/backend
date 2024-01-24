package com.jyp.justplan.domain.mbti.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "mbti")
@Entity
@NoArgsConstructor
@Getter
public class Mbti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "mbti", nullable = false, length = 4)
    private String mbti;

    @Column(name = "title", nullable = false, length = 15)
    private String title;

    @Column(name = "description", nullable = false, length = 150)
    private String description;

    public Mbti(String mbti, String title, String description) {
        this.mbti = mbti;
        this.title = title;
        this.description = description;
    }
}