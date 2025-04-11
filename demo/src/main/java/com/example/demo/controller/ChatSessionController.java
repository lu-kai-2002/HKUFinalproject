package com.example.demo.controller;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import com.example.demo.DTO.ChatSessionDTO;

@RestController
@RequestMapping("/api/v1")
public class ChatSessionController {

    private final R2dbcEntityTemplate template;

    public ChatSessionController(R2dbcEntityTemplate template) {
        this.template = template;
    }

    @GetMapping("/chat-sessions")
    public Flux<ChatSessionDTO> getChatSessions() {
        // 示例 SQL：按 conversation_id 分组并取最小 question 作为标题（如有需要可以根据需求修改 SQL）
        String sql = "SELECT conversation_id, " +
                "CASE WHEN LENGTH(MIN(question)) > 10 THEN LEFT(MIN(question), 10) ELSE MIN(question) END AS title " +
                "FROM chat_records " +
                "GROUP BY conversation_id";
        return template.getDatabaseClient().sql(sql)
                .map((row, meta) -> new ChatSessionDTO(
                        row.get("conversation_id", String.class),
                        row.get("title", String.class)
                ))
                .all();
    }
}
