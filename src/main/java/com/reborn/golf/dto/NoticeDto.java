package com.reborn.golf.dto;

import com.reborn.golf.entity.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
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

    private String writer;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}
