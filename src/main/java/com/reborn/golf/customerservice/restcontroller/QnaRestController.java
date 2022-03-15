package com.reborn.golf.customerservice.restcontroller;

import com.reborn.golf.common.dto.PageRequestDto;
import com.reborn.golf.common.dto.PageResultDto;
import com.reborn.golf.customerservice.dto.AnswerDto;
import com.reborn.golf.customerservice.dto.QuestionDto;
import com.reborn.golf.customerservice.entity.Question;
import com.reborn.golf.customerservice.service.QnaService;
import com.reborn.golf.security.dto.AuthMemeberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/api")
@Validated
@RequiredArgsConstructor
public class QnaRestController {
    private final QnaService qnaService;

    //모든 질문 목록을 출력
    @GetMapping("/questions")
    public ResponseEntity<PageResultDto<Question, QuestionDto>> getList(PageRequestDto pageRequestDto) throws IllegalAccessException {
        return ResponseEntity.ok(qnaService.getList(pageRequestDto));
    }

    //조회
    @GetMapping("/questions/{idx}")
    public ResponseEntity<QuestionDto> read(@PathVariable @Min(1) Long idx) {
        return ResponseEntity.ok(qnaService.read(idx));
    }

    //등록
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    @PostMapping("/questions")
    public ResponseEntity<Long> registerQuestion(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid QuestionDto questionDto) {
        return ResponseEntity.ok(qnaService.register(authMemeberDto.getIdx(), questionDto));
    }

    //등록
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    @PostMapping("/questions/{questionIdx}/answers")
    public ResponseEntity<Long> registerAnswer(@AuthenticationPrincipal AuthMemeberDto authMemeberDto,
                                               @PathVariable @NotNull @Min(1) Long questionIdx,
                                               @RequestBody @Valid AnswerDto answerDto) {
        return ResponseEntity.ok(qnaService.registerAnswer(authMemeberDto.getIdx(), questionIdx, answerDto));
    }

    //수정
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    @PutMapping("/questions")
    public ResponseEntity<Long> modifyQuestion(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid QuestionDto questionDto) {
        qnaService.modify(authMemeberDto.getIdx(), questionDto);
        return ResponseEntity.ok(questionDto.getIdx());
    }

    //수정
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    @PutMapping("/answers")
    public ResponseEntity<Long> modifyAnswer(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @RequestBody @Valid AnswerDto answerDto) {
        qnaService.modify(authMemeberDto.getIdx(), answerDto);
        return ResponseEntity.ok(answerDto.getIdx());
    }

    //삭제
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER')")
    @DeleteMapping(value = {"/questions/{idx}", "/answers/{idx}"})
    public ResponseEntity<Long> remove(@AuthenticationPrincipal AuthMemeberDto authMemeberDto, @PathVariable @Min(1) Long idx) {
        qnaService.remove(authMemeberDto.getIdx(), idx);
        return ResponseEntity.ok(idx);
    }
}

