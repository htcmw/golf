package com.reborn.golf.dto.customerservice;


import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegisteredQnaDto {

    private Long idx;

    private String title;

    private String question;

    private String answer;

    private String email;

    private String name;

    private Integer views;
}
