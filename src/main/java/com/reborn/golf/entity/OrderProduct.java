package com.reborn.golf.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class OrderProduct {

    @Id
    @GeneratedValue( strategy =  GenerationType.IDENTITY)
    private Long idx;

    private int price;

    private int Quantity;

//    필요시 사용
//    @Column(columnDefinition = "TEXT")
//    private String imageUrl;

}
