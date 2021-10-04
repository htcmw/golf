package com.reborn.golf.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AssociatesDto {

    private Integer idx;

    @Email(message = "이메일을 입력해주세요.")
    private String email;

    private String password;

    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3,4})[.-]?(\\d{4})$")
    private String phone;

    @NotBlank
    @Size(min = 2,max = 170)
    private String name;

    @NotBlank
    private String address;

    //소셜 로그인 표시
    private boolean fromSocial;
}
