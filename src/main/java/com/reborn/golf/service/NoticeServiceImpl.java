package com.reborn.golf.service;


import com.reborn.golf.dto.customerservice.NoticeDto;
import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.entity.Enum.Role;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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

        Optional<Notice> result = noticeRepository.getNoticeByIdx(noticeIdx);

        if (result.isPresent()) {
            Notice notice = result.get();
            notice.addViews();
            return entityToDto(notice);
        }
        return null;
    }

    @Override
    public Long register(Integer writerIdx, NoticeDto noticeDto) {
        Notice notice = dtoToEntity(noticeDto, writerIdx);
        noticeRepository.save(notice);
        return notice.getIdx();
    }


    /*
     * Manager권한이 없는 맴버는 작성한 Notice에 대해 같은 PK만 수정할 수 있고
     * Manager권한이 있는 맴버가 작성하면 다른 Manager가 변경할 수 있다.
     * */
    @Override
    public Long modify(Integer writerIdx, NoticeDto noticeDto) {

        Optional<Notice> result = noticeRepository.getNoticeByIdx(noticeDto.getIdx());

        if (result.isPresent()) {
            Notice notice = result.get();

            if(notice.getWriter().getRoleSet().contains(Role.ROLE_MANAGER) || notice.getWriter().getIdx().equals(writerIdx)) {

                notice.chageWriter(writerIdx);
                notice.changeTitle(noticeDto.getTitle());
                notice.changeContent(noticeDto.getContent());

                log.info(notice);
                noticeRepository.save(notice);
            }
            return notice.getIdx();
        }
        return null;
    }

    @Override
    public Long remove(Integer writerIdx, Long noticeIdx) {
        Optional<Notice> result = noticeRepository.getNoticeByIdx(noticeIdx);
        if (result.isPresent()) {
            Notice notice = result.get();

            if(notice.getWriter().getIdx().equals(writerIdx)
                    || notice.getWriter().getRoleSet().contains(Role.ROLE_ADMIN)) {

                notice.changeRemoved(true);
                noticeRepository.save(notice);
            }
            return notice.getIdx();
        }
        return null;
    }


}
