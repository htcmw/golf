package com.reborn.golf.customerservice.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class QuestionDto extends CustomerserviceDto {
    private String question;
    List<AnswerDto> answer;
}
