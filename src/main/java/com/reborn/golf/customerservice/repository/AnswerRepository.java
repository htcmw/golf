package com.reborn.golf.customerservice.repository;

import com.reborn.golf.customerservice.entity.Answer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByIdxAndRemovedFalse(Long idx);

    @EntityGraph(attributePaths = {"writer"})
    @Query("SELECT a " +
            "FROM Answer a " +
            "WHERE a.question.idx = :quetionIdx AND a.removed = false")
    List<Answer> findByQuestionIdxAndRemovedFalse(@Param("quetionIdx") Long quetionIdx);
}
