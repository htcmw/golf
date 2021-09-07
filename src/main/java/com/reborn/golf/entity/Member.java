package com.reborn.golf.entity;

import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
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
    private String password;
    private String name;
    private String phone;
    private String address;
    private String grade;
    private boolean fromSocial;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void addMemberAuthority(MemberRole memberRole){
        roleSet.add(memberRole);
    }
}
