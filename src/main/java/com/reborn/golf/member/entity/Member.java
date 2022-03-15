package com.reborn.golf.member.entity;

import com.reborn.golf.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"roleSet", "wallet"})
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String phone;

    private String address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_idx")
    private Wallet wallet;

    private boolean fromSocial;

    @Column(name = "is_removed")
    private boolean removed;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Role> roleSet = new HashSet<>();

    public void registerWallet(Wallet wallet){
        this.wallet = wallet;
    }

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

    public void remove(){
        this.removed = true;
    }

}
