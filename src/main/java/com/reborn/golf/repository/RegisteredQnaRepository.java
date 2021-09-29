package com.reborn.golf.repository;

import com.reborn.golf.entity.RegisteredQna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;


public interface RegisteredQnaRepository extends JpaRepository<RegisteredQna, Long> {
    @Query(value =
            "SELECT r.idx, r.title, r.question, r.answer, r.views, r.regDate, r.modDate, w.idx, w.email, w.name " +
                    "FROM RegisteredQna r " +
                    "LEFT JOIN r.writer w " +
                    "WHERE r.removed = false ")
    Page<Object[]> getRegisteredQnasWithMember(Pageable pageable);

    @Query(value =
            "SELECT r.idx, r.title, r.question, r.answer, r.views, r.regDate, r.modDate, w.idx, w.email, w.name " +
                    "FROM RegisteredQna r " +
                    "LEFT JOIN r.writer w " +
                    "WHERE r.removed = false " +
                    "AND r.idx = :idx")
    Optional<RegisteredQna> getRegisteredQnaByIdx(Long idx);
}
