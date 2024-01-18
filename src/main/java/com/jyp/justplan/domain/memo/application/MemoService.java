package com.jyp.justplan.domain.memo.application;

import com.jyp.justplan.domain.memo.domain.MemoRepository;
import com.jyp.justplan.domain.memo.domain.Memo;
import com.jyp.justplan.domain.memo.dto.request.MemoRequestDto;
import com.jyp.justplan.domain.memo.dto.response.MemoResponseDto;
import com.jyp.justplan.domain.memo.exception.NoSuchMemoException;
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
    public MemoResponseDto createMemo(MemoRequestDto memoRequestDto) {

        Memo memo = memoRequestDto.toEntity().toBuilder()
                .content(memoRequestDto.getContent())
                .build();

        Memo savedMemo = memoRepository.save(memo);
        return MemoResponseDto.of(savedMemo);
    }

    /*READ*/
    public MemoResponseDto findMemoById(Long memoId) {
        Memo existingMemo = findMemo(memoId);
        return MemoResponseDto.of(existingMemo);
    }

    /*UPDATE*/
    @Transactional
    public MemoResponseDto updateMemo(MemoRequestDto memoRequestDto, Long memoId) {
        Memo existingMemo = findMemo(memoId);
        existingMemo.update(memoRequestDto);
        Memo savedMemo = memoRepository.save(existingMemo);
        return MemoResponseDto.of(savedMemo);
    }

    /*RESET*/
    @Transactional
    public MemoResponseDto resetMemo(Long memoId) {
        Memo existingMemo = findMemo(memoId);
        existingMemo.reset();
        Memo savedMemo = memoRepository.save(existingMemo);
        return MemoResponseDto.of(savedMemo);
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
