package com.example.demo.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.demo.service.QuestionService;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final WebClient finbotWebClient;
    private final ObjectMapper objectMapper;

    public QuestionServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.finbotWebClient = WebClient.builder()
                .baseUrl("http://localhost:6006") // Finbot服务地址
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Override
    public Mono<String> getAnswer(String question) {
        return finbotWebClient.post()
                .uri("/query")
                .bodyValue(Map.of("query", question))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        JsonNode root = objectMapper.readTree(response);
                        if (root.path("success").asBoolean()) {
                            JsonNode resultNode = root.path("result");
                            String rawAnswer = resultNode.path("response").asText();

                            // 关键处理：移除思考过程
                            String cleanAnswer = rawAnswer
                                    .replaceAll("<think>[\\s\\S]*?</think>", "")
                                    .trim();

                            return Mono.just(cleanAnswer.isEmpty() ?
                                    "Finbot未返回有效内容" : cleanAnswer);
                        }
                        return Mono.just("Finbot错误: " +
                                root.path("error").asText("未知错误"));
                    } catch (Exception e) {
                        return Mono.just("响应解析失败: " + e.getMessage());
                    }
                });
    }
}
