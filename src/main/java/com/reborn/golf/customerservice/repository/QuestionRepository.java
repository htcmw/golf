package com.reborn.golf.customerservice.repository;

import com.reborn.golf.customerservice.entity.Question;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByIdxAndRemovedFalse(Long idx);

    @EntityGraph(attributePaths = {"writer"})
    @Query("SELECT q " +
            "FROM Question q " +
            "WHERE q.idx = :idx AND q.removed = false")
    Optional<Question> findQuestionAnswersByIdx(@Param("idx") Long idx);
}
