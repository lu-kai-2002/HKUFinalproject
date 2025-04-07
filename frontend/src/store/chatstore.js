import { defineStore } from 'pinia';
import { askQuestion } from '../api/question';

export const useChatStore = defineStore('chat', {
    state: () => ({
        messages: []  // { sender: 'user'|'bot', content: '...' }
    }),
    actions: {
        async sendQuestion(text) {
            this.messages.push({ sender: 'user', content: text });
            try {
                const response = await askQuestion(text);
                // 根据你给出的AnswerResponse结构进行调整
                const answer = response.data.answer;
                this.messages.push({ sender: 'bot', content: answer });
            } catch (error) {
                this.messages.push({ sender: 'bot', content: `Error: ${error.message}` });
            }
        }
    }
});


