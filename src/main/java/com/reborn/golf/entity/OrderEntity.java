package com.reborn.golf.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString (exclude = {"member","orderStatus","orderProduct","delivery"})
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue ( strategy =  GenerationType.IDENTITY)
    private Long idx;

    private int totalPrice;

    // ORDER, CANCEL
    @Enumerated(EnumType.STRING)
    private OrderStatus orderState;

    @ManyToOne (fetch = FetchType.LAZY)
    private Member member;

    @OneToOne (fetch = FetchType.LAZY)
    private OrderProduct orderProduct;

    @OneToOne (fetch = FetchType.LAZY)
    private Delivery delivery;




}
