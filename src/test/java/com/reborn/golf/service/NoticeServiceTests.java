package com.reborn.golf.service;

import com.reborn.golf.dto.customerservice.NoticeDto;
import com.reborn.golf.dto.common.PageRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class NoticeServiceTests {

    @Autowired
    NoticeService noticeService;


    @Test
    public void registerNoticeTest(){
        IntStream.rangeClosed(0, 10).forEach(i -> {
            NoticeDto noticeDto = NoticeDto.builder()
                    .title("title.........." + i)
                    .content("content.........." + i)
                    .views(0)
                    .build();
            noticeService.register(((i % 10) + 1), noticeDto);
        });
    }

    @Test
    public void registerTest() {
        IntStream.rangeClosed(0, 10).forEach(i -> {
/*
            NoticeDto noticeDto = NoticeDto.builder()
                    .title("title.........." + i)
                    .content("content.........." + i)
                    .views(0)
                    .build();

            noticeService.register(((i % 10) + 1), null, noticeDto, NoticeFractionation.NOTICE);
            noticeDto = NoticeDto.builder()
*/
            NoticeDto noticeDto = NoticeDto.builder()
                    .title("Qna.........." + i)
                    .content("Qna.........." + i)
                    .build();
//            noticeService.register((i % 10) + 1, null, noticeDto);
        });
        IntStream.rangeClosed(0, 100).forEach(i -> {
            NoticeDto noticeDto = NoticeDto.builder()
                    .title("Qna..........plus" + i)
                    .content("content..........plus" + i)
                    .build();

//            noticeService.register((i % 10) + 1, (i % 10) + 1L, noticeDto);
        });
    }


    @Test
    public void getListNoticesTest(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(10).build();
        var resultDto= noticeService.getList(pageRequestDto);
        for (var list: resultDto.getDtoList()) {
            System.out.println(list);
        }
    }


    @Test
    public void getListQnaTest(){
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(10).build();
        var resultDto= noticeService.getList(pageRequestDto);
        for (var list: resultDto.getDtoList()) {
            System.out.println(list);
        }
    }

    @Test
    public void readTest(){
        System.out.println(noticeService.read(1L));
    }

    @Test
    public void removeTest(){
//        noticeService.remove(1L, NoticeFractionation.NOTICE);

    }
}
