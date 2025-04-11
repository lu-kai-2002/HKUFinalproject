import { defineStore } from 'pinia';
import axios from 'axios';

export const useChatSessionsStore = defineStore('chatSessions', {
    state: () => ({
        // 后端返回的会话数组，每个会话对象包含 { conversationId, title }
        sessions: [],
        // 当前选中的会话ID
        selectedSessionId: null,
        // 当前会话的完整聊天记录，每条记录格式为 { id, question, answer, timestamp }
        messages: []
    }),
    actions: {
        // 从后端获取会话列表
        async fetchSessions() {
            try {
                const response = await axios.get('/api/v1/chat-sessions');
                // 假设后端返回的会话对象包含 conversationId 和 title（如果标题超过10个字符则截取前10个）
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
        // 添加新消息到当前会话消息数组中
        addMessage(message) {
            this.messages.push(message);
        },
        // 更新指定消息的回答内容
        updateMessage(id, newAnswer) {
            const index = this.messages.findIndex(msg => msg.id === id);
            if (index !== -1) {
                this.messages[index].answer = newAnswer;
            }
        }
    }
});


