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
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aid;

    private String address;

    private String pubKey;

    private String pvKey;

    @OneToOne
    private Member member;
}

