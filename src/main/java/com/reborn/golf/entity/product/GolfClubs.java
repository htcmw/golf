package com.reborn.golf.entity.product;

import com.reborn.golf.entity.Category;
import com.reborn.golf.entity.Enum.GolfClubTypes;
import com.reborn.golf.entity.Product;
import com.reborn.golf.entity.ProductImage;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import java.util.List;


@Entity
@DiscriminatorValue("golfclubs")
@NoArgsConstructor
public class GolfClubs extends Product {
    @Enumerated
    private GolfClubTypes golfClubTypes;

    @Builder
    public GolfClubs(Long idx, String title, String brand, Integer quantity, Integer price, String content, Category category, List<ProductImage> productImages, boolean removed, GolfClubTypes golfClubTypes) {
        super(idx, title, brand, quantity, price, content, category, productImages, removed);
        this.golfClubTypes = golfClubTypes;
    }
}
