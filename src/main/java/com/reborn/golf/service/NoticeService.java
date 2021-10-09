package com.reborn.golf.service;


import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.QnaDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.entity.NoticeFractionation;
import com.reborn.golf.entity.Qna;


public interface NoticeService {

    PageResultDto<Object[],NoticeDto> getList(PageRequestDto pageRequestDto);

    NoticeDto read(Long noticeIdx);

    Long register(Integer writerIdx, QnaDto qnaDto);

    Long modify(Integer writerIdx, QnaDto qnaDto);

    Long remove(Integer memberIdx, Long qnaIdx);

    default NoticeDto entityToDto(Qna qna){
        return NoticeDto.builder()
                .idx(qna.getIdx())
                .title(qna.getTitle())
                .content(qna.getContent())
                .modDate(qna.getModDate())
                .regDate(qna.getRegDate())
                .views(qna.getViews())
                .email(qna.getWriter().getEmail())
                .name(qna.getWriter().getName())
                .build();
    }

    default QnaDto entityToDto(Qna qna, Member member){
        return QnaDto.builder()
                .idx(qna.getIdx())
                .title(qna.getTitle())
                .content(qna.getContent())
                .modDate(qna.getModDate())
                .regDate(qna.getRegDate())
                .views(qna.getViews())
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }

    default Qna dtoToEntity(QnaDto qnaDto, Integer writerIdx){
        return Qna.builder()
                .idx(qnaDto.getIdx())
                .title(qnaDto.getTitle())
                .content(qnaDto.getContent())
                .writer(Member.builder().idx(writerIdx).build())
                .views(qnaDto.getViews())
                .build();
    }

}
