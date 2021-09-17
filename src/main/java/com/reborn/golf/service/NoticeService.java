package com.reborn.golf.service;


import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Notice;


public interface NoticeService {

    PageResultDto<Notice,NoticeDto> getList(PageRequestDto pageRequestDto);

    NoticeDto read(Long num);

    Long register(String email, NoticeDto noticeDto);

    void modify(String email, NoticeDto noticeDto);

    void remove(Long num);

    default NoticeDto entityToDto(Notice notice){
        return NoticeDto.builder()
                .num(notice.getNum())
                .title(notice.getTitle())
                .content(notice.getContent())
                .modDate(notice.getModDate())
                .regDate(notice.getRegDate())
                .views(notice.getViews())
                .email(notice.getWriter().getEmail())
                .name(notice.getWriter().getName())
                .build();
    }

    default NoticeDto entityToDto(Notice notice, Member member){
        return NoticeDto.builder()
                .num(notice.getNum())
                .title(notice.getTitle())
                .content(notice.getContent())
                .modDate(notice.getModDate())
                .regDate(notice.getRegDate())
                .views(notice.getViews())
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }

    default Notice dtoToEntity(NoticeDto noticeDto, String email){
        return Notice.builder()
                .num(noticeDto.getNum())
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .writer(new Member(email))
                .views(noticeDto.getViews())
                .build();
    }

}
