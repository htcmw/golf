package com.reborn.golf.dto.common;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReplyDto {

    private Long idx;

    @NotBlank
    private String text;

    @Min(1)
    private Long noticeIdx;

    @Email
    private String email;

    private String name;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}


