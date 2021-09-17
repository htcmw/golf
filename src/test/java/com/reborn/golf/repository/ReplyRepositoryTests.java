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
    public void getRepliesByNoticeOrderByRegDateAsc(){
        Page<Reply> replies = replyRepository.getRepliesByNoticeOrderByRegDateAsc(Notice.builder().num(1L).build(), PageRequest.of(0,10));
        replies.forEach( entity -> {
            System.out.println(entity);
            System.out.println(entity.getNotice().getNum());
            System.out.println(entity.getMember().getEmail());

        });
    }
}