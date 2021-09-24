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
    private Long idx;

    @Column(nullable = false)
    private Long pidx;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    //조회수
    @Column
    private Integer views;

    @Enumerated(EnumType.STRING)
    private NoticeFractionation fractionation;

    @Column
    private boolean removed;

    public void setFractionation(NoticeFractionation fractionation){this.fractionation = fractionation;}

    public void addViews() {
        this.views++;
    }

    public void chageWriter(Integer writerIdx) {
        this.writer = Member.builder().idx(writerIdx).build();
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
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
