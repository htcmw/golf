package com.reborn.golf.customerservice.service;

import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.common.dto.PageResultDto;
import com.reborn.golf.customerservice.dto.*;
import com.reborn.golf.customerservice.entity.*;
import com.reborn.golf.customerservice.repository.*;
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
public class CustomerserviceServiceImpl implements CustomerserviceService {
    private final CustomerserviceRepository csRepository;
    private final NoticeRepository noticeRepository;
    private final KnownQnaRepository knownQnaRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResultDto getList(PageRequestDto pageRequestDto, CustomerserviceType csType) {
        if (csType == CustomerserviceType.NOTICE) {
            Page<Notice> result = csRepository.searchAllNotices(pageRequestDto.getPageable());
            Function<Notice, NoticeDto> fn = (this::entityToDto);
            return new PageResultDto<>(result, fn);
        } else if (csType == CustomerserviceType.KNOWN_QNA) {
            Page<KnownQna> result = csRepository.searchAllKnownQnas(pageRequestDto.getPageable());
            Function<KnownQna, KnownQnaDto> fn = (this::entityToDto);
            return new PageResultDto<>(result, fn);
        } else if (csType == CustomerserviceType.QNA) {
            Page<Question> result = csRepository.searchAllQuestions(pageRequestDto.getPageable());
            Function<Question, QuestionDto> fn = (this::entityToDto);
            return new PageResultDto<>(result, fn);
        }
        throw new IllegalArgumentException("Wrong Input");
    }

    @Override
    @Transactional
    public CustomerserviceDto read(Long idx, CustomerserviceType csType) {
        if (csType == CustomerserviceType.NOTICE) {
            Notice result = noticeRepository.findByIdxAndRemovedFalse(idx)
                    .orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
            result.addViews();
            return entityToDto(result);
        } else if (csType == CustomerserviceType.KNOWN_QNA) {
            KnownQna result = knownQnaRepository.findByIdxAndRemovedFalse(idx)
                    .orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
            result.addViews();
            return entityToDto(result);
        } else if (csType == CustomerserviceType.QNA) {
            Question question = questionRepository.findQuestionAnswersByIdx(idx)
                    .orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
            List<Answer> answers = answerRepository.findByQuestionIdxAndRemovedFalse(idx);
            question.addViews();

            return makeQuestionAnswer(question, answers);
        }
        throw new IllegalArgumentException("Wrong Input");
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

    @Override
    public Long register(Integer writerIdx, CustomerserviceDto customerserviceDto) {
        Customerservice customerservice;
        if (customerserviceDto instanceof NoticeDto) {
            customerservice = dtoToNotice((NoticeDto) customerserviceDto, writerIdx);
        } else if (customerserviceDto instanceof KnownQnaDto) {
            customerservice = dtoToKnownQna((KnownQnaDto) customerserviceDto, writerIdx);
        } else if (customerserviceDto instanceof QuestionDto) {
            customerservice = dtoToQuestion((QuestionDto) customerserviceDto, writerIdx);
        } else {
            throw new IllegalArgumentException("Wrong Input");
        }
        csRepository.save(customerservice);
        return customerservice.getIdx();
    }

    @Override
    public Long registerAnswer(Integer writerIdx, Long questionIdx, AnswerDto answerDto) {
        Answer answer = dtoToAnswer(answerDto, questionIdx, writerIdx);
        csRepository.save(answer);
        return answer.getIdx();
    }

    @Transactional
    @Override
    public void modify(Integer writerIdx, CustomerserviceDto customerserviceDto) {
        Customerservice customerservice;
        if (customerserviceDto instanceof NoticeDto) {
            Notice result = noticeRepository.findByIdxAndRemovedFalse(customerserviceDto.getIdx())
                    .orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
            NoticeDto noticeDto = (NoticeDto) customerserviceDto;
            result.changeTitle(noticeDto.getTitle());
            result.changeContent(noticeDto.getContent());
            result.chageWriter(writerIdx);
        } else if (customerserviceDto instanceof KnownQnaDto) {
            KnownQna result = knownQnaRepository.findByIdxAndRemovedFalse(customerserviceDto.getIdx())
                    .orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));
            KnownQnaDto knownQnaDto = (KnownQnaDto) customerserviceDto;
            result.chageWriter(writerIdx);
            result.changeTitle(knownQnaDto.getTitle());
            result.changeContent(knownQnaDto.getQuestion());
            result.changeSubContent(knownQnaDto.getAnswer());
        } else if (customerserviceDto instanceof QuestionDto) {
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

    @Override
    @Transactional
    public void remove(Integer writerIdx, Long idx) {
        Customerservice result = csRepository.findByIdxAndRemovedFalse(idx)
                .orElseThrow(() -> new NoSuchElementException("IDX에 해당하는 DB정보가 없습니다"));
        result.remove();
    }
}
