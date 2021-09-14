package com.reborn.golf.service;

import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.ReplyDto;
import com.reborn.golf.entity.Reply;
import com.reborn.golf.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;

    @Override
    public Long register(ReplyDto replyDto) {
        Reply reply = dtoToEntity(replyDto);
        replyRepository.save(reply);
        return reply.getNum();
    }

    @Override
    public PageResultDto<Reply, ReplyDto> getList(Long noticeNum, PageRequestDto pageRequestDto) {
//        Page<Reply> result = replyRepository.getListByNoticeNum(noticeNum, pageRequestDto.getPageable(Sort.by("regDate").ascending()));
//        return new PageResultDto<>(result, this::entityToDto);
        return null;
    }

    @Override
    public Long modify(ReplyDto replyDto) {
        Optional<Reply> result = replyRepository.findById(replyDto.getNum());
        if(result.isPresent()){
            Reply reply = result.get();
            reply.changeText(replyDto.getText());
            replyRepository.save(reply);
            return reply.getNum();
        }
        return null;
    }

    @Override
    public Long remove(ReplyDto replyDto) {
        replyRepository.deleteById(replyDto.getNum());
        return replyDto.getNum();
    }
}
