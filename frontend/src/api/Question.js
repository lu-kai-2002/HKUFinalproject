import axios from 'axios';

/**
 * 调用后端接口获取回复
 * @param {String} text 用户输入的问题
 * @returns Promise 对象，解析为后端返回的数据
 */
export function askQuestion(text) {
    return axios.post('/api/v1/ask', { text });
}
