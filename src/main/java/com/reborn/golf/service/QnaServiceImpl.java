package com.reborn.golf.service;


import com.reborn.golf.dto.customerservice.QnaDto;
import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.entity.*;
import com.reborn.golf.entity.Enum.Role;
import com.reborn.golf.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * NoticeController, QnaController 에서 사용
 * */
@Service
@Log4j2
@RequiredArgsConstructor
public class QnaServiceImpl implements QnaService {

    private final QnaRepository qnaRepository;

    @Override
    @Transactional
    public PageResultDto<Object[], QnaDto> getList(PageRequestDto pageRequestDto) {
        Page<Object[]> result = qnaRepository.getQnasWithWriter(pageRequestDto.getPageable(Sort.by("regDate").ascending()));
        Function<Object[], QnaDto> function = (arr -> entityToDto((Qna) arr[0], (Member) arr[1]));
        return new PageResultDto<>(result, function);
    }

    @Override
    @Transactional
    public QnaDto read(Long qnaIdx) {

        Optional<Qna> result = qnaRepository.getQnaByIdx(qnaIdx);

        if (result.isPresent()) {
            Qna qna = result.get();
            qna.addViews();
            QnaDto qnaDto = entityToDto(qna);

            List<Qna> children = qnaRepository.getAnswerByIdx(qnaIdx);
            List<QnaDto> qnaDtoList = children.stream().map(this::entityToDto).collect(Collectors.toList());
            qnaDto.setAnswer(qnaDtoList);

            return qnaDto;
        }
        return null;
    }

    @Override
    public Long register(Integer writerIdx, QnaDto qnaDto) {
        Qna qna = dtoToEntity(qnaDto, writerIdx);
        qna.setParent(qnaDto.getPidx());
        qnaRepository.save(qna);
        return qna.getIdx();
    }


    /*
     * Manager권한이 없는 맴버는 작성한 Qna에 대해 같은 PK만 수정할 수 있고
     * Manager권한이 있는 맴버가 작성하면 다른 Manager가 변경할 수 있다.
     * */
    @Override
    public Long modify(Integer writerIdx, QnaDto qnaDto) {

        Optional<Qna> result = qnaRepository.getQnaByIdx(qnaDto.getIdx());

        if (result.isPresent()) {
            Qna qna = result.get();
            if (qna.getWriter().getIdx().equals(writerIdx)) {
                qna.changeTitle(qnaDto.getTitle());
                qna.changeContent(qnaDto.getContent());
                log.info(qna);
                qnaRepository.save(qna);
            }
            return qna.getIdx();
        }
        return null;
    }

    @Override
    public Long remove(Integer writerIdx, Long qnaIdx) {
        Optional<Qna> result = qnaRepository.getQnaByIdx(qnaIdx);
        if (result.isPresent()) {
            Qna qna = result.get();

            if (qna.getWriter().getIdx().equals(writerIdx)
                    || qna.getWriter().getRoleSet().contains(Role.ROLE_ADMIN)) {

                qna.changeRemoved(true);
                qnaRepository.save(qna);
            }
            return qna.getIdx();
        }
        return null;
    }


}
