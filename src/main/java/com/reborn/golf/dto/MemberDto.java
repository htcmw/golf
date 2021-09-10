package com.reborn.golf.dto;

import com.reborn.golf.entity.MemberRole;
import lombok.*;

import javax.persistence.Id;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

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
