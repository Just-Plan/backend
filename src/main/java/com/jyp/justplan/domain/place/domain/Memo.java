package com.jyp.justplan.domain.place.domain;

import com.jyp.justplan.domain.BaseEntity;
import com.jyp.justplan.domain.place.dto.request.MemoRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE memo SET deleted_at = NOW() WHERE id = ?")
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

    public void update(MemoRequest memoRequestDto) {
        this.content = memoRequestDto.getContent();
        this.color = memoRequestDto.getColor();
    }
    public void reset() {
        this.content = "";
    }
}
