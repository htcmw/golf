package com.reborn.golf.customerservice.entity;

import com.reborn.golf.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
public class Notice extends Customerservice {
    @Builder
    public Notice(Long idx, String title, String content, Member writer, long views, boolean removed) {
        super(idx, title, content, writer, views, removed);
    }
}
