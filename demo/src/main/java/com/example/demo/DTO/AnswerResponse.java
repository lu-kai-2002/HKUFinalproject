package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponse {
    private String answer;
    // 返回对应的会话ID，让前端能保存会话状态
    private String conversationId;
}
