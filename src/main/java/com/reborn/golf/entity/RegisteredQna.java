package com.reborn.golf.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredQna extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;
    //조회수
    @Column
    private Integer views;

    @Column
    private boolean removed;

    public void addViews() {
        this.views++;
    }

    public void chageWriter(Integer writerIdx) {
        this.writer = Member.builder().idx(writerIdx).build();
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeQuestion(String question) {
        this.question = question;
    }

    public void changeAnswer(String answer) {
        this.answer = answer;
    }

    public void changeRemoved(Boolean removed) {
        this.removed = removed;
    }

    //view가 null일 경우 0으로 대체
    @PrePersist
    public void prePersist() {
        this.views = (this.views == null ? 0 : this.views);
    }
}
