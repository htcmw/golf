package com.reborn.golf.repository;

import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Notice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class NoticeRepositoryTests {
    @Autowired
    NoticeRepository noticeRepository;

    @Test
    public void register() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Notice notice = Notice.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .writer(Member.builder().email("user" + i + "@naver.com").build())
                    .views(0)
                    .build();
            System.out.println(notice);
            noticeRepository.save(notice);
        });
    }

    @Test
    public void getListByEmailTest() {
        PageRequestDto pageRequestDto = PageRequestDto.builder().page(1).size(10).build();
        Page<Notice> page = noticeRepository.findbyEmail("user1@naver.com", pageRequestDto.getPageable(Sort.by("regDate").ascending()));
        System.out.println(page.stream().collect(Collectors.toList()));
    }
}
