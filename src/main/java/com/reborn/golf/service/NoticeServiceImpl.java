package com.reborn.golf.service;


import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

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
    public PageResultDto<Notice, NoticeDto> getListByEmail(PageRequestDto pageRequestDto, String email) {
//        Page<Notice> result = noticeRepository.findbyEmail(email, pageRequestDto.getPageable(Sort.by("regDate").ascending()));
//        return new PageResultDto<>(result,this::entityToDto);
        return null;
    }


    @Override
    public NoticeDto read(Long num) {
        Optional<Notice> result = noticeRepository.findById(num);
        return result.map(this::entityToDto).orElse(null);
    }

    @Override
    public void modify(NoticeDto noticeDto) {
        Optional<Notice> result = noticeRepository.findById(noticeDto.getNum());
        if(result.isPresent()){
            Notice notice = result.get();
            notice.changeTitle(notice.getTitle());
            notice.changeContent(notice.getContent());
            noticeRepository.save(notice);
        }
    }

    @Override
    public void remove(Long num) {
        noticeRepository.deleteById(num);
    }
}
