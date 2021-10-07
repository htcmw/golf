package com.reborn.golf.entity;

import com.reborn.golf.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String title;

    private String brand;

    private String rank;

    private Integer quantity;

    private Integer price;

    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean removed;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeBrand(String brand) {
        this.brand = brand;
    }

    public void changeRank(String rank) {
        this.rank = rank;
    }
    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void changePrice(int price) {
        this.price = price;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeRemoved(Boolean removed) {
        this.removed = removed;
    }
}
