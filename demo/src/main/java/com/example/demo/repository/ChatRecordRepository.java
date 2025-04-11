package com.example.demo.repository;

import com.example.demo.entity.ChatRecord;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRecordRepository extends ReactiveCrudRepository<ChatRecord, Long> {
    // 根据会话ID查找所有记录
    Flux<ChatRecord> findByConversationId(String conversationId);
}