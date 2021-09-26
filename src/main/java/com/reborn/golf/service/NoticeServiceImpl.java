package com.reborn.golf.service;


import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.entity.NoticeFractionation;
import com.reborn.golf.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public Long register(Integer writerIdx, Long qnaIdx, NoticeDto noticeDto, NoticeFractionation fractionation) {
        Notice notice = dtoToEntity(noticeDto, writerIdx);
        notice.setFractionation(fractionation);
        notice.setParent(qnaIdx);
        noticeRepository.save(notice);
        return notice.getIdx();
    }

    @Override
    @Transactional
    public PageResultDto<Notice, NoticeDto> getList(PageRequestDto pageRequestDto, NoticeFractionation fractionation) {

        Page<Notice> result = noticeRepository.getNoticesByFractionationAndRemovedFalse(fractionation, pageRequestDto.getPageable(Sort.by("regDate").ascending()));

        return new PageResultDto<>(result, this::entityToDto);
    }

    @Override
    public NoticeDto read(Long noticeIdx, NoticeFractionation fractionation) {

        Optional<Notice> result = noticeRepository.getNoticeByIdxAndFractionationAndRemovedFalse(noticeIdx, fractionation);

        if (result.isPresent()) {

            Notice notice = result.get();

            notice.addViews();

            noticeRepository.save(notice);

            return entityToDto(notice);
        }
        return null;
    }

    @Override
    public void modify(Integer writerIdx, NoticeDto noticeDto, NoticeFractionation fractionation) {

        Optional<Notice> result = noticeRepository.getNoticeByIdxAndFractionationAndRemovedFalse(noticeDto.getIdx(), fractionation);

        if (result.isPresent()) {

            Notice notice = result.get();

            notice.chageWriter(writerIdx);

            notice.changeTitle(notice.getTitle());

            notice.changeContent(notice.getContent());

            log.info(notice);

            noticeRepository.save(notice);

        }
    }

    @Override
    public void remove(Long noticeIdx, NoticeFractionation fractionation) {
        Optional<Notice> result = noticeRepository.getNoticeByIdxAndFractionationAndRemovedFalse(noticeIdx, fractionation);
        if (result.isPresent()) {
            Notice notice = result.get();
            notice.changeRemoved(true);
            noticeRepository.save(notice);
        }
    }


}
