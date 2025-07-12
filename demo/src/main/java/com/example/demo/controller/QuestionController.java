package com.example.demo.controller;

import com.example.demo.DTO.AnswerResponse;
import com.example.demo.DTO.ChatResponse;
import com.example.demo.DTO.QuestionRequest;
import com.example.demo.entity.ChatRecord;
import com.example.demo.repository.ChatRecordRepository;
import com.example.demo.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
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
    /**
     * AI 聊天接口（响应式，不做任何持久化）
     * 支持两种 payload：
     *  1) 包含 annualReturn 等字段的“回测数据”分析请求
     *  2) 包含 message 字段的普通文本提问
     */
    @PostMapping("/chat")
    public Mono<ChatResponse> chatNoPersist(@RequestBody Map<String, Object> payload) {
        // 随机 sessionId，仅用于 service 层上下文（无需存库）
        String sessionId = UUID.randomUUID().toString();

        // 拼接 questionText
        String questionText;
        if (payload.containsKey("annualReturn")) {
            questionText = String.format(
                    "请基于以下回测结果给出分析：年化收益率 %s，最大回撤 %s，Sharpe 比率 %s，曲线数据 %s。",
                    payload.get("annualReturn"),
                    payload.get("maxDrawdown"),
                    payload.get("sharpeRatio"),
                    payload.get("equityCurve").toString()
            );
        } else if (payload.containsKey("message")) {
            questionText = Objects.toString(payload.get("message"), "");
        } else {
            questionText = "";
        }

        // 响应式地拿到 answer，然后包装成 ChatResponse
        return questionService.getAnswer(questionText)
                .map(answer -> new ChatResponse(answer));
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

