package com.reborn.golf.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Member extends BaseEntity{
    @Id
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String phone;
    private String address;

    private boolean fromSocial;

    public void changeName(String name){
        this.name = name;
    }
    public void changeAddress(String address){
        this.address = address;
    }
    //본인인증 후 변경가능
    public void changePassword(String password){
        this.password = password;
    }
    //본인증 후 변경가능
    public void changePhone(String phone){
        this.phone = phone;
    }
    //소셜 본인인증 후 변경가능
    public void changeFromSocial(boolean fromSocial){
        this.fromSocial = fromSocial;
    }


    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void addMemberAuthority(MemberRole memberRole){
        roleSet.add(memberRole);
    }
}
