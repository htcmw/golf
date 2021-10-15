package com.reborn.golf.entity.product;


import com.reborn.golf.entity.Category;
import com.reborn.golf.entity.Enum.GolfWearTypes;
import com.reborn.golf.entity.Product;
import com.reborn.golf.entity.ProductImage;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import java.util.List;

@Entity
@NoArgsConstructor
@DiscriminatorValue("golfwear")
public class GolfWear extends Product {
    @Enumerated
    private GolfWearTypes golfWearTypes;

    @Builder
    public GolfWear(Long idx, String title, String brand, Integer quantity, Integer price, String content, Category category, List<ProductImage> productImages, boolean removed, GolfWearTypes golfWearTypes) {
        super(idx, title, brand, quantity, price, content, category, productImages, removed);
        this.golfWearTypes = golfWearTypes;
    }
}
