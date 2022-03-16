package com.reborn.golf.common.entity;

import com.reborn.golf.common.dto.ImageDto;
import lombok.*;
import javax.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Image extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String uuid;

    private String imgName;

    private String path;

    private boolean removed;

    public ImageDto toImageDto() {
        return ImageDto.builder()
                .imgName(getImgName())
                .path(getPath())
                .uuid(getUuid())
                .build();
    }
    public void remove() {
        this.removed = false;
    }

}
