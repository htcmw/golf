package com.reborn.golf.service;

import com.reborn.golf.dto.NoticeDto;
import com.reborn.golf.dto.PageRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class NoticeServiceTests {

    @Autowired
    NoticeService noticeService;


    @Test
    public void registerTest() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            NoticeDto noticeDto = NoticeDto.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .writer("user" + i +"@naver.com")
                    .views(0)
                    .build();
            System.out.println(noticeDto);
            noticeService.register(noticeDto);
        });

    }

    @Test
    public void getListTest(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(10).build();
        var resultDto= noticeService.getList(pageRequestDto);
        for (var list: resultDto.getDtoList()) {
            System.out.println(list);
        }
    }

    @Test
    public void getListByEmailTest(){
        var v = noticeService.getListByEmail(PageRequestDto.builder().page(1).size(10).build(),"user1@naver.com");
        for (var a : v.getDtoList()){
            System.out.println(a);
        }
    }
}
