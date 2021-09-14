package com.reborn.golf.repository;

import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.ReplyDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    @Transactional
    public void getListByNoticeNumTest() {
        Optional<Reply> result = replyRepository.getListByNoticeNum(1L);
        System.out.println(result.get());
    }
}


