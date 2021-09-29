package com.reborn.golf.dto;


<<<<<<< Updated upstream
import javax.persistence.*;

@Getter
@Setter
@ToString
=======
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
>>>>>>> Stashed changes
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long pno;

    private String title;

    private String brand;

    private String rank;

    private int quentity;

    private int price;

    private String content;

    // 이미지 생성
    @Builder.Default
    private List<ProductImageDto> imageDtoList = new ArrayList<>();

    //영화의 평균 평점
    private double avg;

    //리뷰 수 jpa의 count( )
    private int reviewCnt;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}
