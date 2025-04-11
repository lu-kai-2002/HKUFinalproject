package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("chat_records")
public class ChatRecord {

    @Id
    private Long id;

    private String conversationId;  // 用于标识会话（例如 UUID）
    private String question;
    private String answer;
    private LocalDateTime timestamp;

    public ChatRecord() {}

    public ChatRecord(String conversationId, String question, String answer, LocalDateTime timestamp) {
        this.conversationId = conversationId;
        this.question = question;
        this.answer = answer;
        this.timestamp = timestamp;
    }

    // Getters 和 Setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getConversationId() {
        return conversationId;
    }
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}