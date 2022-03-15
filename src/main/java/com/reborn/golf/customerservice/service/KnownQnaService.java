package com.reborn.golf.customerservice.service;

import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.common.dto.PageResultDto;
import com.reborn.golf.customerservice.dto.*;
import com.reborn.golf.customerservice.entity.*;
import com.reborn.golf.customerservice.repository.*;
import com.reborn.golf.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class KnownQnaService {
    private final CustomerserviceRepository csRepository;
    private final KnownQnaRepository knownQnaRepository;

    public PageResultDto getList(PageRequestDto pageRequestDto) {
        Page<KnownQna> result = csRepository.searchAllKnownQnas(pageRequestDto.getPageable());
        Function<KnownQna, KnownQnaDto> fn = (this::entityToDto);
        return new PageResultDto<>(result, fn);
    }

    @Transactional
    public KnownQnaDto read(Long idx) {
        KnownQna result = knownQnaRepository.findByIdxAndRemovedFalse(idx)
                .orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
        result.addViews();
        return entityToDto(result);
    }


    public Long register(Integer writerIdx, KnownQnaDto knownQnaDto) {
        Customerservice customerservice = dtoToKnownQna(knownQnaDto, writerIdx);
        csRepository.save(customerservice);
        return customerservice.getIdx();
    }

    @Transactional
    public void modify(Integer writerIdx, KnownQnaDto knownQnaDto) {
        KnownQna result = knownQnaRepository.findByIdxAndRemovedFalse(knownQnaDto.getIdx())
                .orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
        result.chageWriter(writerIdx);
        result.changeTitle(knownQnaDto.getTitle());
        result.changeContent(knownQnaDto.getQuestion());
        result.changeSubContent(knownQnaDto.getAnswer());
    }

    @Transactional
    public void remove(Integer writerIdx, Long idx) {
        Customerservice result = csRepository.findByIdxAndRemovedFalse(idx)
                .orElseThrow(() -> new NoSuchElementException("IDX에 해당하는 DB정보가 없습니다"));
        result.remove();
    }

    private KnownQna dtoToKnownQna(KnownQnaDto knownQnaDto, Integer memberIdx) {
        return KnownQna.builder()
                .idx(knownQnaDto.getIdx())
                .title(knownQnaDto.getTitle())
                .content(knownQnaDto.getQuestion())
                .subContent(knownQnaDto.getAnswer())
                .writer(Member.builder().idx(memberIdx).build())
                .build();
    }

    private KnownQnaDto entityToDto(KnownQna knownQna) {
        return KnownQnaDto.builder()
                .idx(knownQna.getIdx())
                .title(knownQna.getTitle())
                .question(knownQna.getContent())
                .answer(knownQna.getSubContent())
                .email(knownQna.getWriter().getEmail())
                .name(knownQna.getWriter().getName())
                .views(knownQna.getViews())
                .build();
    }

}
