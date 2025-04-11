<template>
  <div class="chat-input">
    <!-- 自定义上传按钮 -->
    <label class="upload-label">
      Upload Word
      <input type="file" accept=".doc,.docx" @change="handleFileUpload" hidden />
    </label>

    <!-- 文本输入框 -->
    <input
        type="text"
        v-model="inputText"
        @keyup.enter="handleSend"
        placeholder="Input Message..."
    />
    <button @click="handleSend">Send</button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { askQuestion } from '../../api/Question.js';
import { useChatSessionsStore } from '@/store/chatSessionStore';
import { uploadWord } from '../../api/file.js';

const inputText = ref('');
const chatStore = useChatSessionsStore();

async function handleSend() {
  const text = inputText.value.trim();
  if (!text) return;

  const tempId = Date.now();
  const optimisticMessage = {
    id: tempId,
    question: text,
    answer: '正在回答…',
    timestamp: new Date().toISOString()
  };
  chatStore.addMessage(optimisticMessage);
  inputText.value = '';

  try {
    const response = await askQuestion(text, chatStore.selectedSessionId);
    const { answer, conversationId } = response.data;

    if (!chatStore.selectedSessionId && conversationId) {
      chatStore.selectedSessionId = conversationId;
    }

    chatStore.updateMessage(tempId, answer);
  } catch (error) {
    console.error('发送消息失败', error);
    chatStore.updateMessage(tempId, '回答失败，请重试');
  }
}

function handleFileUpload(event) {
  const file = event.target.files[0];
  if (file) {
    uploadWordFile(file);
  }
}

async function uploadWordFile(file) {
  try {
    const response = await uploadWord(file);
    const { text } = response.data;
    inputText.value = text;
    // handleSend(); // 可选：是否自动发送
  } catch (error) {
    console.error('上传并解析 Word 文件失败', error);
  }
}
</script>

<style scoped>
.chat-input {
  display: flex;
  align-items: center;
  padding: 10px;
  border-top: 1px solid #ccc;
}

.upload-label {
  background-color: #409eff;
  color: white;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 10px;
  white-space: nowrap;
  font-size: 14px;
}

.chat-input input[type='text'] {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.chat-input button {
  margin-left: 10px;
  padding: 8px 16px;
  border: none;
  background-color: #409eff;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}
</style>



