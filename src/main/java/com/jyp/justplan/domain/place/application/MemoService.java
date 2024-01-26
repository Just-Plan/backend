package com.jyp.justplan.domain.place.application;

import com.jyp.justplan.domain.place.domain.MemoRepository;
import com.jyp.justplan.domain.place.domain.Memo;
import com.jyp.justplan.domain.place.dto.request.MemoRequest;
import com.jyp.justplan.domain.place.dto.response.MemoResponse;
import com.jyp.justplan.domain.place.exception.NoSuchMemoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoService {

    private final MemoRepository memoRepository;

    /*CREATE*/
    @Transactional
    public MemoResponse createMemo(MemoRequest memoRequest) {

        Memo memo = memoRequest.toEntity().toBuilder()
                .content(memoRequest.getContent())
                .color(memoRequest.getColor())
                .build();

        Memo savedMemo = memoRepository.save(memo);
        return MemoResponse.of(savedMemo);
    }

    /*READ*/
    public MemoResponse findMemoById(Long memoId) {
        Memo existingMemo = findMemo(memoId);
        return MemoResponse.of(existingMemo);
    }

    /*UPDATE*/
    @Transactional
    public MemoResponse updateMemo(MemoRequest memoRequest, Long memoId) {
        Memo existingMemo = findMemo(memoId);
        existingMemo.update(memoRequest);
        Memo savedMemo = memoRepository.save(existingMemo);
        return MemoResponse.of(savedMemo);
    }

    /*RESET*/
    @Transactional
    public MemoResponse resetMemo(Long memoId) {
        Memo existingMemo = findMemo(memoId);
        existingMemo.reset();
        Memo savedMemo = memoRepository.save(existingMemo);
        return MemoResponse.of(savedMemo);
    }

    /*DELETE*/
    @Transactional
    public void deleteMemo(Long memoId) {
        Memo existingMemo = findMemo(memoId);
        memoRepository.delete(existingMemo);
    }

    private Memo findMemo(Long memoId) {
        return memoRepository.findById(memoId)
                .orElseThrow(NoSuchMemoException::new);
    }

    //TODO
    // 본인만 CRUD 가능하게

}
