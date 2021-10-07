package com.reborn.golf.entity;

import com.reborn.golf.entity.Enum.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString (exclude = {"member","orderState","orderProduct","delivery"})
public class Orders extends BaseEntity {

    @Id
    @GeneratedValue ( strategy =  GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private int totalPrice;
    // ORDER, CANCEL
    @Enumerated(EnumType.STRING)
    private OrderStatus orderState;

    @ManyToOne (fetch = FetchType.LAZY)
    private Member member;

    @OneToMany (fetch = FetchType.LAZY)
    @JoinColumn(name = "Order_Product_idx")
    private List<OrderProduct> OrderProduct;

    @OneToOne (fetch = FetchType.LAZY)
    private Delivery delivery;

    private boolean removed;

    public void changeIsRemoved(boolean removed){
        this.removed = removed;
    }
}