package com.example.demo.controller;

import com.example.demo.DTO.AnswerResponse;
import com.example.demo.DTO.QuestionRequest;
import com.example.demo.entity.ChatRecord;
import com.example.demo.repository.ChatRecordRepository;
import com.example.demo.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class QuestionController {

    private final QuestionService questionService;
    private final ChatRecordRepository chatRecordRepository;

    public QuestionController(QuestionService questionService, ChatRecordRepository chatRecordRepository) {
        this.questionService = questionService;
        this.chatRecordRepository = chatRecordRepository;
    }

    @PostMapping("/ask")
    public Mono<ResponseEntity<AnswerResponse>> askQuestion(@RequestBody QuestionRequest request) {
        System.out.println("收到请求: " + request);
        // 将会话ID处理逻辑放入 final 变量中，保证在lambda表达式中不可变
        final String conversationId = (request.getConversationId() == null || request.getConversationId().trim().isEmpty())
                ? UUID.randomUUID().toString()
                : request.getConversationId();

        return questionService.getAnswer(request.getText())
                .flatMap(answer -> {
                    // 构造一条聊天记录
                    ChatRecord record = new ChatRecord();
                    record.setConversationId(conversationId);
                    record.setQuestion(request.getText());
                    record.setAnswer(answer);
                    record.setTimestamp(LocalDateTime.now());

                    // 将记录保存到 MySQL，并在保存完成后返回 answer 内容
                    return chatRecordRepository.save(record)
                            .thenReturn(answer);
                })
                .map(answer -> ResponseEntity.ok(new AnswerResponse(answer, conversationId)))
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new AnswerResponse("Error: " + e.getMessage(), conversationId)));
                });
    }

    // 根据会话ID查询聊天记录，返回类型统一为 ResponseEntity<Object>
    @GetMapping("/chat-records")
    public Mono<ResponseEntity<Object>> getChatRecords(@RequestParam("conversationId") String conversationId) {
        return chatRecordRepository.findByConversationId(conversationId)
                .collectList()
                .map(records -> ResponseEntity.ok((Object) records))
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body((Object)("Error: " + e.getMessage())));
                });
    }
}

