package com.jyp.justplan.domain.memo.domain;

import com.jyp.justplan.domain.memo.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoRepository extends JpaRepository<Memo,Long> {
}
