package com.reborn.golf.service;


import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Notice;

import java.util.List;

public interface NoticeService {

    PageResultDto<Notice,NoticeDto> getList(PageRequestDto pageRequestDto);

    PageResultDto<Notice, NoticeDto> getListByEmail(PageRequestDto pageRequestDto, String email);

    Long register(NoticeDto noticeDto);

    NoticeDto read(Long num);

    void modify(NoticeDto noticeDto);

    void remove(Long num);

    default NoticeDto entityToDto(Notice notice){
        return NoticeDto.builder()
                .num(notice.getNum())
                .title(notice.getTitle())
                .content(notice.getContent())
                .modDate(notice.getModDate())
                .regDate(notice.getRegDate())
                .views(notice.getViews())
                .writer(notice.getWriter().getEmail())
                .build();
    }

    default Notice dtoToEntity(NoticeDto noticeDto){
        return Notice.builder()
                .num(noticeDto.getNum())
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .writer(Member.builder().email(noticeDto.getWriter()).build())
                .views(noticeDto.getViews())
                .build();
    }

}
