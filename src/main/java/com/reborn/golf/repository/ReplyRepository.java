package com.reborn.golf.repository;

import com.reborn.golf.entity.Reply;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    @Query(value = "select r " +
            "from Reply r " +
            "left join Notice n on n.num = r.notice.num " +
            "where n.num = :num")
    Optional<Reply> getListByNoticeNum(Long num);
}
