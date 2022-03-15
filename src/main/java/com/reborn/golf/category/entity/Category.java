package com.reborn.golf.category.entity;

import com.reborn.golf.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"parent", "children"})
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;
    @Setter
    private String code;
    private String name;
    private Integer priority;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_idx")
    private Category parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    private boolean removed;

    public void changeName(String name) {
        this.name = name;
    }

    public void changeParent(Category parent) {
        this.parent = parent;
    }

    public void changeCode(String code) {
        this.code = code;
    }

    public void changePriority(Integer priority) {
        this.priority = priority;
    }

    public void removed() {
        this.removed = true;
    }
}
