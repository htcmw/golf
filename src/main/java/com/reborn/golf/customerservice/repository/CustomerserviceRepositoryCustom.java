package com.reborn.golf.customerservice.repository;

import com.querydsl.core.Tuple;
import com.reborn.golf.customerservice.entity.KnownQna;
import com.reborn.golf.customerservice.entity.Notice;
import com.reborn.golf.customerservice.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomerserviceRepositoryCustom {
    Page<Question> searchAllQuestions(Pageable pageable);

    Page<KnownQna> searchAllKnownQnas(Pageable pageable);

    Page<Notice> searchAllNotices(Pageable pageable);
}
