package com.example.demo.service;

import reactor.core.publisher.Mono;

public interface QuestionService {
    public Mono<String> getAnswer(String question);
}
