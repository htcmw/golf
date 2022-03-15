package com.reborn.golf.customerservice.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reborn.golf.customerservice.entity.KnownQna;
import com.reborn.golf.customerservice.entity.Notice;
import com.reborn.golf.customerservice.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;

import static com.reborn.golf.customerservice.entity.QKnownQna.knownQna;
import static com.reborn.golf.customerservice.entity.QNotice.notice;
import static com.reborn.golf.customerservice.entity.QQuestion.question;
import static com.reborn.golf.member.entity.QMember.member;

public class CustomerserviceRepositoryImpl implements CustomerserviceRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public CustomerserviceRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Question> searchAllQuestions(Pageable pageable) {
        QueryResults<Question> result = queryFactory
                .select(question)
                .from(question).leftJoin(question.writer, member).fetchJoin()
                .where(question.removed.eq(false))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.regDate.asc())
                .fetchResults();
        List<Question> list = result.getResults();
        long count = result.getTotal();
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<KnownQna> searchAllKnownQnas(Pageable pageable) {
        QueryResults<KnownQna> result = queryFactory
                .select(knownQna)
                .from(knownQna).leftJoin(knownQna.writer, member).fetchJoin()
                .where(knownQna.removed.eq(false))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(knownQna.regDate.asc())
                .fetchResults();
        List<KnownQna> list = result.getResults();
        long count = result.getTotal();
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<Notice> searchAllNotices(Pageable pageable) {
        QueryResults<Notice> result = queryFactory
                .select(notice)
                .from(notice).leftJoin(notice.writer, member).fetchJoin()
                .where(notice.removed.eq(false))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(notice.regDate.asc())
                .fetchResults();
        List<Notice> list = result.getResults();
        long count = result.getTotal();
        return new PageImpl<>(list, pageable, count);
    }

}
