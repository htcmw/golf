package com.reborn.golf.dto.customerservice;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class QnaDto {

    private Long idx;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private Integer views;

    private String email;

    private String name;

    private List<QnaDto> answer;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}
