package com.reborn.golf.repository;

import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
    Page<Reply> getRepliesByNoticeOrderByRegDateAsc(Notice notice, Pageable pageable);

    Optional<Reply> getReplyByMemberAndNum(Member member, Long num);

    void deleteReplyByMemberAndNum(Member member, Long num);
}