package com.reborn.golf.customerservice.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class CustomerserviceDto {
    protected Long idx;

    protected String title;

    protected String email;

    protected String name;

    protected long views;

    protected LocalDateTime regDate;

    protected LocalDateTime modDate;
}
