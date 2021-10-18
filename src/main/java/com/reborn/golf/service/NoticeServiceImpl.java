package com.reborn.golf.service;

import com.reborn.golf.dto.customerservice.NoticeDto;
import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.exception.NotExistEntityException;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.function.Function;

/*
 * NoticeController, QnaController 에서 사용
 * */
@Service
@Log4j2
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    @Transactional
    public PageResultDto<Object[], NoticeDto> getList(PageRequestDto pageRequestDto) {
        Page<Object[]> result = noticeRepository.getNoticesWithWriter(pageRequestDto.getPageable(Sort.by("regDate").ascending()));
        Function<Object[], NoticeDto> function = (arr -> entityToDto((Notice) arr[0], (Member) arr[1]));
        return new PageResultDto<>(result, function);
    }

    @Override
    @Transactional
    public NoticeDto read(Long noticeIdx) {
        Notice notice = noticeRepository.getNoticeByIdx(noticeIdx)
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 공지사항이 DB에 없습니다"));
        notice.addViews();
        noticeRepository.save(notice);
        log.info(notice + " : 조회수 증가");
        return entityToDto(notice);
    }

    @Override
    public Long register(Integer writerIdx, NoticeDto noticeDto) {
        Notice notice = dtoToEntity(noticeDto, writerIdx);
        noticeRepository.save(notice);
        log.info(notice + " : 등록");
        return notice.getIdx();
    }

    @Override
    public Long modify(Integer writerIdx, NoticeDto noticeDto) {

        Notice notice = noticeRepository.getNoticeByIdx(noticeDto.getIdx())
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 공지사항이 DB에 없습니다"));

        notice.chageWriter(writerIdx);
        notice.changeTitle(noticeDto.getTitle());
        notice.changeContent(noticeDto.getContent());
        noticeRepository.save(notice);
        log.info(notice + " : 수정");
        return notice.getIdx();
    }

    @Override
    public void remove(Integer writerIdx, Long noticeIdx) {
        Notice notice = noticeRepository.getNoticeByIdx(noticeIdx)
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 공지사항이 DB에 없습니다"));

        notice.changeRemoved(true);
        noticeRepository.save(notice);
        log.info(notice + " : 삭제");
    }
}
