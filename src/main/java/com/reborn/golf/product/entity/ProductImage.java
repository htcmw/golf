package com.reborn.golf.product.entity;

import com.reborn.golf.common.entity.Image;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = "product")
public class ProductImage extends Image {

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Builder
    public ProductImage(Long idx, String uuid, String imgName, String path, boolean removed, Product product) {
        super(idx, uuid, imgName, path, removed);
        this.product = product;
    }
}
