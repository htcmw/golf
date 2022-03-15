package com.reborn.golf.customerservice.repository;


import com.reborn.golf.customerservice.entity.Customerservice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerserviceRepository extends JpaRepository<Customerservice, Long>, CustomerserviceRepositoryCustom {

    Optional<Customerservice> findByIdxAndRemovedFalse(Long idx);
}
