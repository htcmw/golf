package com.reborn.golf.customerservice.service;

import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.common.dto.PageResultDto;
import com.reborn.golf.customerservice.dto.*;
import com.reborn.golf.customerservice.entity.Answer;
import com.reborn.golf.customerservice.entity.KnownQna;
import com.reborn.golf.customerservice.entity.Notice;
import com.reborn.golf.customerservice.entity.Question;
import com.reborn.golf.member.entity.Member;


public interface CustomerserviceService {

    PageResultDto getList(PageRequestDto pageRequestDto, CustomerserviceType csType) throws IllegalAccessException;

    CustomerserviceDto read(Long idx, CustomerserviceType csType);

    Long register(Integer writerIdx, CustomerserviceDto customerserviceDto);

    Long registerAnswer(Integer writerIdx, Long questionIdx, AnswerDto answerDto);

    void modify(Integer writerIdx, CustomerserviceDto customerserviceDto);

    void remove(Integer writerIdx, Long idx);

    default NoticeDto entityToDto(Notice notice) {
        return NoticeDto.builder()
                .idx(notice.getIdx())
                .title(notice.getTitle())
                .content(notice.getContent())
                .modDate(notice.getModDate())
                .regDate(notice.getRegDate())
                .views(notice.getViews())
                .email(notice.getWriter().getEmail())
                .name(notice.getWriter().getName())
                .build();
    }

    default QuestionDto entityToDto(Question question) {
        return QuestionDto.builder()
                .idx(question.getIdx())
                .title(question.getTitle())
                .question(question.getContent())
                .modDate(question.getModDate())
                .regDate(question.getRegDate())
                .views(question.getViews())
                .email(question.getWriter().getEmail())
                .name(question.getWriter().getName())
                .build();
    }

    default AnswerDto entityToDto(Answer answer) {
        return AnswerDto.builder()
                .idx(answer.getIdx())
                .title(answer.getTitle())
                .answer(answer.getContent())
                .modDate(answer.getModDate())
                .regDate(answer.getRegDate())
                .views(answer.getViews())
                .email(answer.getWriter().getEmail())
                .name(answer.getWriter().getName())
                .build();
    }
    default KnownQnaDto entityToDto(KnownQna knownQna) {
        return KnownQnaDto.builder()
                .idx(knownQna.getIdx())
                .title(knownQna.getTitle())
                .question(knownQna.getContent())
                .answer(knownQna.getSubContent())
                .email(knownQna.getWriter().getEmail())
                .name(knownQna.getWriter().getName())
                .views(knownQna.getViews())
                .build();
    }

    default KnownQna dtoToKnownQna(KnownQnaDto knownQnaDto, Integer memberIdx) {
        return KnownQna.builder()
                .idx(knownQnaDto.getIdx())
                .title(knownQnaDto.getTitle())
                .content(knownQnaDto.getQuestion())
                .subContent(knownQnaDto.getAnswer())
                .writer(Member.builder().idx(memberIdx).build())
                .build();
    }

    default Notice dtoToNotice(NoticeDto noticeDto, Integer memberIdx) {
        return Notice.builder()
                .idx(noticeDto.getIdx())
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .writer(Member.builder().idx(memberIdx).build())
                .views(noticeDto.getViews())
                .build();
    }

    default Question dtoToQuestion(QuestionDto questionDto, Integer memberIdx) {
        return Question.builder()
                .title(questionDto.getTitle())
                .content(questionDto.getQuestion())
                .writer(Member.builder().idx(memberIdx).build())
                .build();
    }

    default Answer dtoToAnswer(AnswerDto answerDto, Long questionIdx,Integer memberIdx) {
        return Answer.builder()
                .idx(answerDto.getIdx())
                .question(Question.builder().idx(questionIdx).build())
                .title("[답글]"+ answerDto.getTitle())
                .content(answerDto.getAnswer())
                .writer(Member.builder().idx(memberIdx).build())
                .build();
    }
}
