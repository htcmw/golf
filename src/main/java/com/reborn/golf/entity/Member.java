package com.reborn.golf.entity;

import com.reborn.golf.entity.Enum.Role;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity{
    //PK를 따로 만들어줘야 될 수 있음
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String phone;

    private String address;

    private boolean fromSocial;

    @Column(name = "is_removed")
    private boolean removed;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Role> roleSet = new HashSet<>();

    @OneToOne(mappedBy = "member")
    private Wallet wallet;

    public void addMemberAuthority(Role role){
        roleSet.add(role);
    }

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

    public void changeIsRemoved(boolean removed){
        this.removed = removed;
    }

}
