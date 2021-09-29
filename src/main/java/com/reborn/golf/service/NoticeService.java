package com.reborn.golf.service;


import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.entity.Associates;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.entity.NoticeFractionation;


public interface NoticeService {

    PageResultDto<Object[],NoticeDto> getList(PageRequestDto pageRequestDto, NoticeFractionation fractionation);

    NoticeDto read(Long noticeIdx, NoticeFractionation fractionation);

    Long register(Integer memberIdx, Long qnaIdx,NoticeDto noticeDto, NoticeFractionation fractionation);

    Long modify(Integer memberIdx, NoticeDto noticeDto, NoticeFractionation fractionation);

    Long remove(Integer writerIdx, Long noticeIdx, NoticeFractionation fractionation);

    default NoticeDto entityToDto(Notice notice){
        return NoticeDto.builder()
                .idx(notice.getIdx())
                .title(notice.getTitle())
                .content(notice.getContent())
                .modDate(notice.getModDate())
                .regDate(notice.getRegDate())
                .views(notice.getViews())
                .email(notice.getMember().getEmail() != null ? notice.getMember().getEmail() : notice.getAssociates().getEmail())
                .name(notice.getMember().getName() != null ? notice.getMember().getName() : notice.getAssociates().getName())
                .build();
    }

    default NoticeDto entityToDto(Notice notice, Member member, Associates associates){
        return NoticeDto.builder()
                .idx(notice.getIdx())
                .title(notice.getTitle())
                .content(notice.getContent())
                .modDate(notice.getModDate())
                .regDate(notice.getRegDate())
                .views(notice.getViews())
                .email(member.getEmail() != null ? member.getEmail() : associates.getEmail())
                .name(member.getName() != null ? member.getName() : associates.getName())
                .build();
    }

    default Notice dtoToEntity(NoticeDto noticeDto, Integer writerIdx){
        return Notice.builder()
                .idx(noticeDto.getIdx())
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .writer(Member.builder().idx(writerIdx).build())
                .writer(Member.builder().idx(writerIdx).build())
                .views(noticeDto.getViews())
                .build();
    }

}
