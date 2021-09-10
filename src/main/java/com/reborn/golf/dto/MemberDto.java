package com.reborn.golf.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDto {

    private String email;

    private String password;

    private String phone;

    private String name;

    private String address;
    //소셜 로그인 표시
    private boolean fromSocial;

}
