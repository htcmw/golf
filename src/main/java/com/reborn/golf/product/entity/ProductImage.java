package com.reborn.golf.product.entity;

import com.reborn.golf.product.dto.ProductImageDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString(exclude = "product") //연관 관계시 항상 주의
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;

    private String uuid;

    private String imgName;

    private String path;

    private boolean removed;

    @ManyToOne(fetch = FetchType.LAZY) //무조건 lazy로
    private Product product;

    public ProductImageDto toProductImageDto() {
        return ProductImageDto.builder()
                .imgName(getImgName())
                .path(getPath())
                .uuid(getUuid())
                .build();
    }

    public void changeRemoved(boolean removed) {
        this.removed = removed;
    }
}
