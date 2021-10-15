package com.reborn.golf.entity.product;


import com.reborn.golf.entity.Category;
import com.reborn.golf.entity.Enum.GolfEquipmentTypes;
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
@DiscriminatorValue("golfequipment")
public class GolfEquipment extends Product {
    @Enumerated
    private GolfEquipmentTypes golfEquipmentTypes;

    @Builder
    public GolfEquipment(Long idx, String title, String brand, Integer quantity, Integer price, String content, Category category, List<ProductImage> productImages, boolean removed, GolfEquipmentTypes golfEquipmentTypes) {
        super(idx, title, brand, quantity, price, content, category, productImages, removed);
        this.golfEquipmentTypes = golfEquipmentTypes;
    }
}
