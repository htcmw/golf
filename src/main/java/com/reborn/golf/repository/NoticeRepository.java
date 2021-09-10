package com.reborn.golf.repository;

import com.reborn.golf.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface NoticeRepository extends JpaRepository<Notice,Long> {

    @Query(value = "select n, m " +
            "from Notice n left join Member m on n.writer = m " +
            "where n.writer.email = :email",
            countQuery = "select count(n) from Notice n")
    Page<Object[]> findByEmail(String email, Pageable pageable);


}
