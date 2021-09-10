package com.reborn.golf.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NoticeDto {

    private Long num;

    private String title;

    private String content;

    private Integer views;

    private String email;

    private String name;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}
