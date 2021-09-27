package com.reborn.golf.repository;

import com.reborn.golf.entity.Notice;
import com.reborn.golf.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void getRepliesByNoticeAndRemovedFalse(){
        Page<Reply> replies = replyRepository.getRepliesByNoticeAndRemovedFalse(Notice.builder().idx(1L).build(), PageRequest.of(0,10));
        replies.forEach( entity -> {
            System.out.println(entity);
            System.out.println(entity.getNotice().getIdx());
            System.out.println(entity.getMember().getEmail());

        });
    }
}