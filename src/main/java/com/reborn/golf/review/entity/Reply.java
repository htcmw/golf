package com.reborn.golf.review.entity;

import com.reborn.golf.common.entity.BaseEntity;
import com.reborn.golf.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor
@AllArgsConstructor
@Getter
public abstract class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void changeContent(String content) {
        this.content = content;
    }
}
