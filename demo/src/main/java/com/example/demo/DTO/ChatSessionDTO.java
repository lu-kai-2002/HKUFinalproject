package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatSessionDTO {
    private String conversationId;
    private String title; // 例如第一个问题（如果超过 10 个字符则截取前 10 个）
}
