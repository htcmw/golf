package com.reborn.golf.service;

import com.reborn.golf.dto.common.PageRequestDto;
import com.reborn.golf.dto.common.PageResultDto;
import com.reborn.golf.dto.common.ReplyDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.entity.Reply;

public interface ReplyService {
    PageResultDto<Reply, ReplyDto> getList(Long noticeIdx, PageRequestDto pageRequestDto);

    Long register(Integer idx, ReplyDto replyDto);

    Long modify(Integer idx, ReplyDto replyDto);

    void remove(Integer idx, Long num);

    default ReplyDto entityToDto(Reply reply){
        return ReplyDto.builder()
                .idx(reply.getIdx())
                .text(reply.getText())
                .noticeIdx(reply.getNotice().getIdx())
                .email(reply.getMember().getEmail())
                .name(reply.getMember().getName())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();
    }

    default Reply dtoToEntity(Integer idx, ReplyDto replyDto){
        return Reply.builder()
                .idx(replyDto.getIdx())
                .text(replyDto.getText())
                .notice(Notice.builder().idx(replyDto.getNoticeIdx()).build())
                .member(Member.builder().idx(idx).build())
                .build();
    }
}
