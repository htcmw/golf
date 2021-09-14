package com.reborn.golf.service;

import com.reborn.golf.dto.ReplyDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class ReplyServiceTests {
    @Autowired
    ReplyService replyService;
    @Test
    public void register() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            ReplyDto replyDto = ReplyDto.builder()
                    .text("text.........." + i)
                    .email("user" + ((i % 10) + 1) + "@naver.com")
                    .name("user" + ((i % 10) + 1))
                    .noticeNum((long) (i % 100) + 1)
                    .build();
            System.out.println(replyDto);
            replyService.register(replyDto);

        });

    }
}
