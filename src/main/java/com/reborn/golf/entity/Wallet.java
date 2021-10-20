package com.reborn.golf.entity;

import lombok.*;

import javax.persistence.*;


@ToString (exclude = {"member"})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table
@Builder
@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    //지갑 주소
    private String address;
    //지갑 public key
    private String pubKey;
    //지갑 private key
    private String pvKey;

    @OneToOne
    @JoinColumn(name = "member_idx")
    private Member member;
}

