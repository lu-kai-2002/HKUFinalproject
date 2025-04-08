import axios from 'axios';

/**
 * 上传 Word 文件并解析为字符串
 * @param {File} file 需要上传的Word文件
 * @returns {Promise} 返回 axios 的 Promise 对象
 */
export function uploadWord(file) {
    const formData = new FormData();
    formData.append('file', file);
    return axios.post('/api/v1/upload-word', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}
