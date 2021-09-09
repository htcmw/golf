package com.reborn.golf.dto;

import lombok.*;

import javax.persistence.Id;
import javax.validation.constraints.*;

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

    private boolean fromSocial;

}
