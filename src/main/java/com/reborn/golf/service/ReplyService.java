package com.reborn.golf.service;

import com.reborn.golf.dto.PageRequestDto;
import com.reborn.golf.dto.PageResultDto;
import com.reborn.golf.dto.ReplyDto;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.Notice;
import com.reborn.golf.entity.Reply;

public interface ReplyService {

    Long register(ReplyDto replyDto);

    PageResultDto<Reply, ReplyDto> getList(Long noticeNum, PageRequestDto pageRequestDto);

    Long modify(ReplyDto replyDto);

    Long remove(ReplyDto replyDto);

    default ReplyDto entityToDto(Reply reply){
        ReplyDto replyDto = ReplyDto.builder()
                .num(reply.getNum())
                .text(reply.getText())
                .noticeNum(reply.getNotice().getNum())
                .email(reply.getMember().getEmail())
                .name(reply.getMember().getName())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();
        return replyDto;
    }

    default Reply dtoToEntity(ReplyDto replyDto){
        Reply reply = Reply.builder()
                .num(replyDto.getNum())
                .text(replyDto.getText())
                .notice(Notice.builder().num(replyDto.getNoticeNum()).build())
                .member(Member.builder().email(replyDto.getEmail()).build())
                .build();
        return reply;
    }
}
