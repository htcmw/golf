package com.reborn.golf.entity;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String title;

    private String brand;

    private Integer quantity;

    private Integer price;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductImage> productImages;

    private boolean removed;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeBrand(String brand) {
        this.brand = brand;
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
