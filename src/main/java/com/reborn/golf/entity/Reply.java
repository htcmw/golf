package com.reborn.golf.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"member", "notice"})
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(columnDefinition = "TEXT")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Notice notice;

    @Column(nullable = false)
    private boolean removed;

    public void changeText(String text) {
        this.text = text;
    }

    public void changeRemoved(boolean removed) {
        this.removed = removed;
    }
}
