package com.reborn.golf.service;

import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.ReplyDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.entity.Reply;
import com.reborn.golf.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    @Override
    public Long register(String email, ReplyDto replyDto) {
        Reply reply = dtoToEntity(email, replyDto);
        replyRepository.save(reply);
        return reply.getNum();
    }

    @Override
    @Transactional
    public PageResultDto<Reply, ReplyDto> getList(Long noticeNum, PageRequestDto pageRequestDto) {
        Page<Reply> result = replyRepository.getRepliesByNoticeOrderByRegDateAsc(Notice.builder().num(noticeNum).build(), pageRequestDto.getPageable(Sort.by("regDate").ascending()));
        return new PageResultDto<>(result, this::entityToDto);
    }

    @Override
    public Long modify(String email, ReplyDto replyDto) {
        Optional<Reply> result = replyRepository.getReplyByMemberAndNum(Member.builder().email(email).build(), replyDto.getNum());
        if (result.isPresent()) {
            Reply reply = result.get();
            reply.changeText(replyDto.getText());
            replyRepository.save(reply);
            return reply.getNum();
        }
        return null;
    }

    @Override
    @Transactional
    public void remove(String email, Long num) {
        replyRepository.deleteReplyByMemberAndNum(Member.builder().email(email).build(), num);
    }
}
