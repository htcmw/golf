package com.reborn.golf.entity;

import lombok.*;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@ToString (exclude = {"order"})
@AllArgsConstructor
@NoArgsConstructor
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
    private Set<MemberRole> roleSet = new HashSet<>();

//    @OneToMany(mappedBy = "member")
//    private List<Order> order = new ArrayList<>();

    public void addMemberAuthority(MemberRole memberRole){
        roleSet.add(memberRole);
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
