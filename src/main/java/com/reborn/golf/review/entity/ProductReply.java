package com.reborn.golf.review.entity;

import com.reborn.golf.member.entity.Member;
import com.reborn.golf.product.entity.Product;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductReply extends Reply {
    private int grade;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Builder
    public ProductReply(Long idx, String content, Member member, int grade, Product product) {
        super(idx, content, member);
        this.grade = grade;
        this.product = product;
    }

    public void changeGrade(int grade) {
        this.grade = grade;
    }
}
