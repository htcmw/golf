package com.reborn.golf.service;

import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.ReplyDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.entity.Reply;

public interface ReplyService {
    PageResultDto<Reply, ReplyDto> getList(Long noticeNum, PageRequestDto pageRequestDto);

    Long register(String email, ReplyDto replyDto);

    Long modify(String email, ReplyDto replyDto);

    void remove(String email, Long num);

    default ReplyDto entityToDto(Reply reply){
        return ReplyDto.builder()
                .num(reply.getNum())
                .text(reply.getText())
                .noticeNum(reply.getNotice().getNum())
                .email(reply.getMember().getEmail())
                .name(reply.getMember().getName())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();
    }

    default Reply dtoToEntity(String email, ReplyDto replyDto){
        return Reply.builder()
                .num(replyDto.getNum())
                .text(replyDto.getText())
                .notice(Notice.builder().num(replyDto.getNoticeNum()).build())
                .member(Member.builder().email(email).build())
                .build();
    }
}
