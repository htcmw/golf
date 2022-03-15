package com.reborn.golf.product.entity;

import com.reborn.golf.category.entity.Category;
import com.reborn.golf.common.entity.BaseEntity;
import com.reborn.golf.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    private Integer quantity;

    private Integer salesVolume;

    private Integer price;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductImage> productImages;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

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

    public void changeSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
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

    public void changeCategory(Integer categoryIdx) {
        this.category = Category.builder().idx(categoryIdx).build();
    }

    @PrePersist
    public void prePersist() {
        this.salesVolume = (this.salesVolume == null ? 0 : this.salesVolume);
    }
}
