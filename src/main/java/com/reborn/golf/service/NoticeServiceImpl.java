package com.reborn.golf.service;


import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public Long register(NoticeDto noticeDto) {
        Notice notice = dtoToEntity(noticeDto);
        noticeRepository.save(notice);
        return notice.getNum();
    }

    @Override
    public PageResultDto<Notice, NoticeDto> getList(PageRequestDto pageRequestDto) {
        Page<Notice> result = noticeRepository.findAll(pageRequestDto.getPageable(Sort.by("regDate").ascending()));
        return new PageResultDto<>(result, this::entityToDto);
    }

    @Override
    public PageResultDto<Object[], NoticeDto> getListByEmail(PageRequestDto pageRequestDto, String email) {
        Page<Object[]> result = noticeRepository.findByEmail(email, pageRequestDto.getPageable(Sort.by("regDate").ascending()));
        Function<Object[], NoticeDto> fn = (en -> entityToDto((Notice) en[0], (Member) en[1]));
        return new PageResultDto<>(result, fn);
    }


    @Override
    public NoticeDto read(Long num) {
        Optional<Notice> result = noticeRepository.findById(num);
        if (result.isPresent()) {
            Notice notice = result.get();
            notice.addViews();
            noticeRepository.save(notice);
            return entityToDto(notice);
        }
        return null;
    }

    @Override
    public void modify(NoticeDto noticeDto) throws Exception {
        Optional<Notice> result = noticeRepository.findById(noticeDto.getNum());
        if (result.isPresent()) {
            Notice notice = result.get();
            if (notice.getWriter().getEmail().equals(noticeDto.getEmail())) {
                notice.changeTitle(notice.getTitle());
                notice.changeContent(notice.getContent());
                log.info(notice);
                noticeRepository.save(notice);
            }
            else{
                throw new Exception();
            }
        }
    }

    @Override
    public void remove(Long num) {
        noticeRepository.deleteById(num);
    }
}
