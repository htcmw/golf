package com.reborn.golf.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"writer"})
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;
    //조회수
    @Column
    private Integer views;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_idx")
    private Notice parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent",  orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Notice> children = new ArrayList<>();

    @Column
    private boolean removed;

    @Enumerated(EnumType.STRING)
    private NoticeFractionation fractionation;


    public void setFractionation(NoticeFractionation fractionation) {
        this.fractionation = fractionation;
    }

    public void setParent(Long pIdx){
        if (pIdx != null)
            this.parent = Notice.builder().idx(pIdx).build();
    }

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
