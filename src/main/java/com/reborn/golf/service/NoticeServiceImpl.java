package com.reborn.golf.service;


import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.MemberRole;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.entity.NoticeFractionation;
import com.reborn.golf.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public PageResultDto<Object[], NoticeDto> getList(PageRequestDto pageRequestDto, NoticeFractionation fractionation) {
        Page<Object[]> result = noticeRepository.getNoticesWithWriter(fractionation, pageRequestDto.getPageable(Sort.by("regDate").ascending()));
        Function<Object[], NoticeDto> function = (arr -> entityToDto((Notice) arr[0], (Member) arr[1]));
        return new PageResultDto<>(result, function);
    }

    @Override
    @Transactional
    public NoticeDto read(Long noticeIdx, NoticeFractionation fractionation) {

        Optional<Notice> result = noticeRepository.getNoticeByIdx(noticeIdx, fractionation);

        if (result.isPresent()) {
            Notice notice = result.get();
            notice.addViews();
            NoticeDto noticeDto = entityToDto(notice);

            if(fractionation == NoticeFractionation.QNA){
                List<Notice> children = noticeRepository.getAnswerByIdx(noticeIdx, fractionation);
                List<NoticeDto> noticeDtoList = children.stream().map(this::entityToDto).collect(Collectors.toList());
                noticeDto.setAnswer(noticeDtoList);
            }

            return noticeDto;
        }
        return null;
    }

    @Override
    public Long register(Integer writerIdx, Long qnaIdx, NoticeDto noticeDto, NoticeFractionation fractionation) {
        Notice notice = dtoToEntity(noticeDto, writerIdx);
        notice.setFractionation(fractionation);
        notice.setParent(qnaIdx);
        noticeRepository.save(notice);
        return notice.getIdx();
    }


    /*
     * Manager권한이 없는 맴버는 작성한 Notice에 대해 같은 PK만 수정할 수 있고
     * Manager권한이 있는 맴버가 작성하면 다른 Manager가 변경할 수 있다.
     * */
    @Override
    public Long modify(Integer writerIdx, NoticeDto noticeDto,NoticeFractionation fractionation) {

        Optional<Notice> result = noticeRepository.getNoticeByIdx(noticeDto.getIdx(), fractionation);

        if (result.isPresent()) {
            Notice notice = result.get();
            log.info(notice.getWriter().getIdx().equals(writerIdx));
            if(notice.getWriter().getRoleSet().contains(MemberRole.ROLE_MANAGER)
                    || notice.getWriter().getIdx().equals(writerIdx)) {

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
    public Long remove(Integer writerIdx, Long noticeIdx, NoticeFractionation fractionation) {
        Optional<Notice> result = noticeRepository.getNoticeByIdx(noticeIdx, fractionation);
        if (result.isPresent()) {
            Notice notice = result.get();

            if(notice.getWriter().getIdx().equals(writerIdx)
                    || notice.getWriter().getRoleSet().contains(MemberRole.ROLE_ADMIN)) {

                notice.changeRemoved(true);
                noticeRepository.save(notice);
            }
            return notice.getIdx();
        }
        return null;
    }


}
