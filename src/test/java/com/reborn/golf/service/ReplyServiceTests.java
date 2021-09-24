package com.reborn.golf.service;

import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.ReplyDto;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@SpringBootTest
public class ReplyServiceTests {
    @Autowired
    ReplyService replyService;

    @Test
    public void registerTest() {
        IntStream.rangeClosed(1, 1000).forEach(i -> {
            ReplyDto replyDto = ReplyDto.builder()
                    .text("text.........." + i)
                    .email("user" + ((i % 10) + 1) + "@naver.com")
                    .name("user" + ((i % 10) + 1))
                    .noticeIdx((long) (i % 100) + 1)
                    .build();
            System.out.println(replyDto);
            replyService.register(((i % 10) + 1),replyDto);

        });

    }
    @Test
    public void getListTest(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(10).build();
        PageResultDto<Reply, ReplyDto> replies = replyService.getList(1L, pageRequestDto);
        System.out.println(replies);
    }

    @Test
    public void modifyTest(){
        ReplyDto replyDto = ReplyDto.builder()
                .idx(1L)
                .text("changed Text ............................")
                .build();

//        replyService.modify(replyDto);
    }
    @Test
    public void remove(){
//        replyService.remove(500L);
    }


}
