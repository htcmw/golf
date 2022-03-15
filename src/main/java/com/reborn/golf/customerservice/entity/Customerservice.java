package com.reborn.golf.customerservice.entity;

import com.reborn.golf.common.entity.BaseEntity;
import com.reborn.golf.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"writer"})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public abstract class Customerservice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    @Lob
    private String content;
    //유저 정보
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;
    //조회수
    @Column
    private long views;
    @Column
    private boolean removed;

    public void addViews() {
        this.views++;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void remove() {
        this.removed = true;
    }

    public void chageWriter(Integer writerIdx) {
        writer = Member.builder().idx(writerIdx).build();
    }

}
