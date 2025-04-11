import axios from 'axios';

/**
 * 调用后端接口获取回复
 * @param {String} text 用户输入的问题
 * @param {String} conversationId 当前会话ID（若为新会话可传空或 null）
 * @returns Promise 对象，解析为后端返回的数据，格式如 { answer, conversationId }
 */
export function askQuestion(text, conversationId) {
    return axios.post('/api/v1/ask', { text, conversationId });
}
