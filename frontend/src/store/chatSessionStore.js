// src/store/chatSessionsStore.js
import { defineStore } from 'pinia';
import axios from 'axios';

export const useChatSessionsStore = defineStore('chatSessions', {
    state: () => ({
        // 会话列表：每个对象包含 { conversationId, title }
        sessions: [],
        // 当前选中的会话 ID
        selectedSessionId: null,
        // 当前会话的完整聊天记录
        messages: []
    }),
    actions: {
        // 从后端获取会话列表
        async fetchSessions() {
            try {
                const response = await axios.get('/api/v1/chat-sessions');
                // 假设后端返回的每个会话对象包含 conversationId 和 title 字段
                // 为了兼容要求：如果标题超过 10 个字符则截取前 10 个
                this.sessions = response.data.map(session => {
                    let title = session.title ? session.title.trim() : '新会话';
                    if (title.length > 10) {
                        title = title.substring(0, 10);
                    }
                    return {
                        conversationId: session.conversationId,
                        title: title
                    };
                });
            } catch (err) {
                console.error("获取会话列表失败", err);
            }
        },
        // 根据会话ID获取该会话的所有聊天记录
        async fetchMessages(conversationId) {
            try {
                const response = await axios.get(`/api/v1/chat-records?conversationId=${conversationId}`);
                this.messages = response.data;
            } catch (err) {
                console.error("获取聊天记录失败", err);
            }
        },
        // 选中会话后保存会话ID并加载该会话的聊天记录
        selectSession(conversationId) {
            this.selectedSessionId = conversationId;
            this.fetchMessages(conversationId);
        },
        // 当新消息加入当前会话时，追加到 messages 数组中
        addMessage(message) {
            this.messages.push(message);
        }
    }
});


