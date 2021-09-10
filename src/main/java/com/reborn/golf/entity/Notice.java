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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer views;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    public void addViews() {
        this.views++;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    @PrePersist
    public void prePersist() {
        this.views = (this.views == null ? 0 : this.views);
    }
}
