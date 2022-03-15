package com.reborn.golf.order.entity;

import com.reborn.golf.product.entity.Product;
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
    //상품 토탈 금액
    private int price;
    //상품 개수
    private int quantity;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private boolean removed;

    public void changeIsRemoved(boolean removed){
        this.removed = removed;
    }
}

