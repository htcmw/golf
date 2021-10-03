package com.reborn.golf.repository;

import com.reborn.golf.entity.Notice;
import com.reborn.golf.entity.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QnaRepository extends JpaRepository<Qna, Long> {

    //getList 메서드에서 사용, Qna의 리스트를 가져오는 쿼리
    @Query(value =
            "SELECT q, w " +
                    "FROM Qna q " +
                    "LEFT JOIN q.writer w " +
                    "WHERE q.removed = false")
    Page<Object[]> getQnasWithWriter(Pageable pageable);

    @Query(value =
                    "SELECT q, w.email, w.name " +
                    "FROM Qna q " +
                            "LEFT JOIN q.writer w " +
                    "WHERE q.idx = :idx " +
                            "AND q.removed = false ")
    Optional<Qna> getQnaByIdx(Long idx);

    //read 메서드에서 사용, QnA의 답글을 가져오는 쿼리
    @Query(value =
                    "SELECT qc, w.email, w.name " +
                    "FROM Qna q " +
                            "JOIN Qna qc on q = qc.parent " +
                            "LEFT JOIN qc.writer w " +
                    "WHERE q.idx = :idx " +
                            "AND qc.removed = false " +
                    "ORDER BY qc.regDate ASC")
    List<Qna> getAnswerByIdx(Long idx);

}
