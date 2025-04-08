package com.example.demo.controller;

import com.example.demo.DTO.AnswerResponse;
import com.example.demo.DTO.QuestionRequest;
import com.example.demo.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/ask")
    public Mono<ResponseEntity<AnswerResponse>> askQuestion(@RequestBody QuestionRequest request) {
        System.out.println(request);
        return questionService.getAnswer(request.getText())
                .map(answer -> ResponseEntity.ok(new AnswerResponse(answer)))
                .onErrorResume(e -> Mono.just(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new AnswerResponse("Error: " + e.getMessage())) // 统一使用AnswerResponse包装错误
                ));
    }
}
