package com.reborn.golf.customerservice.entity;

import com.reborn.golf.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@Getter
@NoArgsConstructor
public class KnownQna extends Customerservice {
    @Lob
    @Column(name = "answer")
    private String subContent;

    @Builder
    public KnownQna(Long idx, String title, String content, Member writer, long views, boolean removed, String subContent) {
        super(idx, title, content, writer, views, removed);
        this.subContent = subContent;
    }

    public void changeSubContent(String subContent) {
        this.subContent = subContent;
    }
}
