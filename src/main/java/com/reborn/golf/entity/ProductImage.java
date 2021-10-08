package com.reborn.golf.entity;

import com.reborn.golf.dto.ProductImageDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString(exclude = "product") //연관 관계시 항상 주의
public class ProductImage  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;

    private String uuid;

    private String imgName;

    private String path;

    public ProductImageDto ProductImageDto(){
        return ProductImageDto.builder()
                .uuid(this.getUuid())
                .imgName(this.getImgName())
                .path(this.getPath())
                .build();
    }

    @ManyToOne(fetch = FetchType.LAZY) //무조건 lazy로
    private Product product;



}
