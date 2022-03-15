package com.reborn.golf.customerservice.repository;

import com.reborn.golf.customerservice.entity.Notice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @EntityGraph(attributePaths = {"writer"})
    Optional<Notice> findByIdxAndRemovedFalse(Long idx);

}
