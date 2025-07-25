package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequest {
    private String text;
    // 如果前端传递了会话ID，则使用；若为空，则后台生成新的会话ID
    private String conversationId;
}