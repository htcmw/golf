package com.reborn.golf.customerservice.service;

import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.common.dto.PageResultDto;
import com.reborn.golf.customerservice.dto.*;
import com.reborn.golf.customerservice.entity.*;
import com.reborn.golf.customerservice.repository.*;
import com.reborn.golf.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class QnaService {
    private final CustomerserviceRepository csRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public PageResultDto getList(PageRequestDto pageRequestDto) {
        Page<Question> result = csRepository.searchAllQuestions(pageRequestDto.getPageable());
        Function<Question, QuestionDto> fn = (this::entityToDto);
        return new PageResultDto<>(result, fn);
    }

    @Transactional
    public QuestionDto read(Long idx) {
        Question question = questionRepository.findQuestionAnswersByIdx(idx)
                .orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
        List<Answer> answers = answerRepository.findByQuestionIdxAndRemovedFalse(idx);
        question.addViews();
        return makeQuestionAnswer(question, answers);
    }

    private QuestionDto makeQuestionAnswer(Question question, List<Answer> answers) {
        QuestionDto questionDto = entityToDto(question);
        List<AnswerDto> answerDtos = new ArrayList<>();
        for (Answer answer : answers) {
            answerDtos.add(entityToDto(answer));
        }
        questionDto.setAnswer(answerDtos);
        return questionDto;
    }

    public Long register(Integer writerIdx, QuestionDto questionDto) {
        Customerservice customerservice = dtoToQuestion(questionDto, writerIdx);
        csRepository.save(customerservice);
        return customerservice.getIdx();
    }

    public Long registerAnswer(Integer writerIdx, Long questionIdx, AnswerDto answerDto) {
        Answer answer = dtoToAnswer(answerDto, questionIdx, writerIdx);
        csRepository.save(answer);
        return answer.getIdx();
    }

    @Transactional
    public void modify(Integer writerIdx, CustomerserviceDto customerserviceDto) {
        if (customerserviceDto instanceof QuestionDto) {
            Question result = questionRepository.findByIdxAndRemovedFalse(customerserviceDto.getIdx())
                    .orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
            QuestionDto questionDto = (QuestionDto) customerserviceDto;
            result.changeTitle(questionDto.getTitle());
            result.changeContent(questionDto.getQuestion());
        } else if (customerserviceDto instanceof AnswerDto) {
            Answer result = answerRepository.findByIdxAndRemovedFalse(customerserviceDto.getIdx())
                    .orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
            AnswerDto answerDto = (AnswerDto) customerserviceDto;
            result.changeContent(answerDto.getAnswer());
        } else {
            throw new IllegalArgumentException("Wrong Input");
        }
    }

    @Transactional
    public void remove(Integer writerIdx, Long idx) {
        Customerservice result = csRepository.findByIdxAndRemovedFalse(idx)
                .orElseThrow(() -> new NoSuchElementException("IDX에 해당하는 DB정보가 없습니다"));
        result.remove();
    }

    private Answer dtoToAnswer(AnswerDto answerDto, Long questionIdx, Integer memberIdx) {
        return Answer.builder()
                .idx(answerDto.getIdx())
                .question(Question.builder().idx(questionIdx).build())
                .title("[답글]" + answerDto.getTitle())
                .content(answerDto.getAnswer())
                .writer(Member.builder().idx(memberIdx).build())
                .build();
    }

    private QuestionDto entityToDto(Question question) {
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


    private AnswerDto entityToDto(Answer answer) {
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

    private Question dtoToQuestion(QuestionDto questionDto, Integer memberIdx) {
        return Question.builder()
                .title(questionDto.getTitle())
                .content(questionDto.getQuestion())
                .writer(Member.builder().idx(memberIdx).build())
                .build();
    }

}
