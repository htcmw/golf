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
    @Transactional
    public PageResultDto<Reply, ReplyDto> getList(Long noticeIdx, PageRequestDto pageRequestDto) {
        Page<Reply> result = replyRepository.getRepliesByNoticeAndRemovedFalse(Notice.builder().idx(noticeIdx).build(), pageRequestDto.getPageable(Sort.by("regDate").ascending()));
        return new PageResultDto<>(result, this::entityToDto);
    }

    @Override
    public Long register(Integer writerIdx, ReplyDto replyDto) {
        Reply reply = dtoToEntity(writerIdx, replyDto);
        replyRepository.save(reply);
        return reply.getIdx();
    }

    @Override
    public Long modify(Integer writerIdx, ReplyDto replyDto) {
        Optional<Reply> result = replyRepository.getReplyByMemberAndIdxAndRemovedFalse(Member.builder().idx(writerIdx).build(), replyDto.getIdx());
        if (result.isPresent()) {
            Reply reply = result.get();
            reply.changeText(replyDto.getText());
            replyRepository.save(reply);
            return reply.getIdx();
        }
        return null;
    }

    @Override
    @Transactional
    public void remove(Integer writerIdx, Long replyIdx) {
        Optional<Reply> result = replyRepository.getReplyByMemberAndIdxAndRemovedFalse(Member.builder().idx(writerIdx).build(), replyIdx);
        if(result.isPresent()){
            Reply reply = result.get();
            reply.changeRemoved(true);
            replyRepository.save(reply);
        }
    }
}
