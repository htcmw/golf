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
public class NoticeService {
    private final CustomerserviceRepository csRepository;
    private final NoticeRepository noticeRepository;

    public PageResultDto getList(PageRequestDto pageRequestDto) {
        Page<Notice> result = csRepository.searchAllNotices(pageRequestDto.getPageable());
        Function<Notice, NoticeDto> fn = (this::entityToDto);
        return new PageResultDto<>(result, fn);
    }

    @Transactional
    public NoticeDto read(Long idx) {
        Notice result = noticeRepository.findByIdxAndRemovedFalse(idx)
                .orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
        result.addViews();
        return entityToDto(result);
    }

    public Long register(Integer writerIdx, NoticeDto noticeDto) {
        Customerservice customerservice = dtoToNotice(noticeDto, writerIdx);
        ;
        csRepository.save(customerservice);
        return customerservice.getIdx();
    }

    @Transactional
    public void modify(Integer writerIdx, NoticeDto noticeDto) {
        Notice result = noticeRepository.findByIdxAndRemovedFalse(noticeDto.getIdx())
                .orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
        result.changeTitle(noticeDto.getTitle());
        result.changeContent(noticeDto.getContent());
        result.chageWriter(writerIdx);

    }

    public void remove(Integer writerIdx, Long idx) {
        Customerservice result = csRepository.findByIdxAndRemovedFalse(idx)
                .orElseThrow(() -> new NoSuchElementException("IDX에 해당하는 DB정보가 없습니다"));
        result.remove();
    }

    private NoticeDto entityToDto(Notice notice) {
        return NoticeDto.builder()
                .idx(notice.getIdx())
                .title(notice.getTitle())
                .content(notice.getContent())
                .modDate(notice.getModDate())
                .regDate(notice.getRegDate())
                .views(notice.getViews())
                .email(notice.getWriter().getEmail())
                .name(notice.getWriter().getName())
                .build();
    }

    private Notice dtoToNotice(NoticeDto noticeDto, Integer memberIdx) {
        return Notice.builder()
                .idx(noticeDto.getIdx())
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .writer(Member.builder().idx(memberIdx).build())
                .views(noticeDto.getViews())
                .build();
    }

}
