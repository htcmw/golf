package com.reborn.golf.customerservice.repository;

import com.reborn.golf.customerservice.entity.KnownQna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface KnownQnaRepository extends JpaRepository<KnownQna, Long> {
    Optional<KnownQna> findByIdxAndRemovedFalse(Long idx);
}
