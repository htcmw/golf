package com.reborn.golf.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "writer")
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long num;

    private String title;

    private String content;
    //조회수
    private Integer views;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    public void addViews() {
        this.views++;
    }

    public void chageWriter(String email){
        this.writer =  new Member(email);
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    //view가 null일 경우 0으로 대체
    @PrePersist
    public void prePersist() {
        this.views = (this.views == null ? 0 : this.views);
    }
}
