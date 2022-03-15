package com.reborn.golf.customerservice.entity;

import com.reborn.golf.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Question extends Customerservice {
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answer;

    @Builder
    public Question(Long idx, String title, String content, Member writer, long views, boolean removed, List<Answer> answer) {
        super(idx, title, content, writer, views, removed);
        this.answer = answer;
    }
}
