package com.jyp.justplan.domain.place.domain;

import com.jyp.justplan.domain.BaseEntity;
import com.jyp.justplan.domain.place.dto.request.MemoUpdateDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE memo SET deleted_at = NOW() WHERE MEMO_ID = ?")
@Where(clause = "deleted_at is null")
public class Memo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMO_ID")
    private Long id;
    @Column(length = 100)
    private String content;
    @Column(length = 20)
    private String color;

    public void update(MemoUpdateDto memoRequestDto) {
        this.content = memoRequestDto.getContent();
        this.color = memoRequestDto.getColor();
    }
    public void reset() {
        this.content = "";
    }

    public Memo() {
        this.content = null;
        this.color = "WHITE";
    }
}
