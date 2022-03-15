package com.reborn.golf.customerservice.entity;

import com.reborn.golf.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor
public class Answer extends Customerservice {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Builder
    public Answer(Long idx, String title, String content, Member writer, long views, boolean removed, Question question) {
        super(idx, title, content, writer, views, removed);
        this.question = question;
    }
}
