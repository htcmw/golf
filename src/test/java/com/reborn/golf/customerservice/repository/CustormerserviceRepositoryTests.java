package com.reborn.golf.customerservice.repository;

import com.querydsl.core.Tuple;
import com.reborn.golf.common.exception.NotExistEntityException;
import com.reborn.golf.customerservice.dto.AnswerDto;
import com.reborn.golf.customerservice.dto.QuestionDto;
import com.reborn.golf.customerservice.entity.Answer;
import com.reborn.golf.customerservice.entity.QAnswer;
import com.reborn.golf.customerservice.entity.QQuestion;
import com.reborn.golf.customerservice.entity.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@SpringBootTest
public class CustormerserviceRepositoryTests {
    @Autowired
    CustomerserviceRepository csRepository;
    @Autowired
    QuestionRepository questionRepository;




    @Test
    @Transactional
    public void readQna1Test() {
        Question question = questionRepository.findQuestionAnswersByIdx(16L)
                .orElseThrow(() -> new NoSuchElementException("해당 정보가 없습니다."));

    }
}
