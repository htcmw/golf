package com.reborn.golf.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReplyDto {
    private Long num;

    private String text;

    private Long noticeNum;

    private String name;

    private String email;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}


