package com.reborn.golf.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Associates extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String name;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column
    private String address;

    @Column(name = "is_removed")
    private boolean removed;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void addMemberAuthority(MemberRole memberRole) {
        roleSet.add(memberRole);
    }

    public Associates(String email) {
        this.email = email;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeAddress(String address) {
        this.address = address;
    }

    //본인인증 후 변경가능
    public void changePassword(String password) {
        this.password = password;
    }

    //본인증 후 변경가능
    public void changePhone(String phone) {
        this.phone = phone;
    }

    public void changeIsRemoved(boolean removed) {
        this.removed = removed;
    }
}
