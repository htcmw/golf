package com.reborn.golf.dto;

import com.reborn.golf.entity.Product;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductDto {
    private Long Id;
    private String title;
    private String brand;
    private String rank;
    private int quentity;
    private int price;
    private String content;
    private String origFilename;
    private String filename;
    private String filePath;

    public Product toEntity() {
        Product build = Product.builder()
                .Id(Id)
                .title(title)
                .brand(brand)
                .rank(rank)
                .quentity(quentity)
                .price(price)
                .content(content)
                .origFilename(origFilename)
                .filename(filename)
                .filePath(filePath)
                .build();
        return build;
    }

    @Builder
    public ProductDto(Long Id, String title, String brand, String rank, int quentity, int price,
                   String content, String origFilename, String filename, String filePath) {
        this.Id = Id;
        this.title = title;
        this.brand = brand;
        this.rank = rank;
        this.quentity = quentity;
        this.price = price;
        this.content = content;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
    }
}