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

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;


    private boolean removed;

    public void changeIsRemoved(boolean removed){
        this.removed = removed;
    }
}

