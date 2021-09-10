package com.reborn.golf.service;


import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Notice;


public interface NoticeService {

    PageResultDto<Notice,NoticeDto> getList(PageRequestDto pageRequestDto);

    PageResultDto<Object[], NoticeDto> getListByEmail(PageRequestDto pageRequestDto, String email);

    Long register(NoticeDto noticeDto);

    NoticeDto read(Long num);

    void modify(NoticeDto noticeDto) throws Exception;

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
                .name(notice.getWriter().getEmail())
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

    default Notice dtoToEntity(NoticeDto noticeDto){
        return Notice.builder()
                .num(noticeDto.getNum())
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .writer(Member.builder().email(noticeDto.getEmail()).name(noticeDto.getName()).build())
                .views(noticeDto.getViews())
                .build();
    }

}
