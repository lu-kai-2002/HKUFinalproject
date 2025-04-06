package com.example.demo.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.demo.service.QuestionService;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final WebClient dsWebClient;
    private final ObjectMapper objectMapper;

    public QuestionServiceImpl(WebClient dsWebClient, ObjectMapper objectMapper) {
        this.dsWebClient = dsWebClient;
        this.objectMapper = objectMapper; // 注入ObjectMapper
    }

    @Override
    public Mono<String> getAnswer(String question) {
        // 构造请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", List.of(Map.of("role", "user", "content", question)));
        requestBody.put("model", "deepseek-chat");

        return dsWebClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        JsonNode root = objectMapper.readTree(response);
                        String answer = root.path("choices")
                                .get(0)
                                .path("message")
                                .path("content")
                                .asText();
                        return Mono.just(answer);
                    } catch (JsonProcessingException e) {
                        return Mono.error(e); // 将异常包装为响应式错误
                    }
                });
    }
}
