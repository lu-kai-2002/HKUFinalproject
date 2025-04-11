<template>
  <div class="chat-window">
    <div class="message-list">
      <div v-if="messages.length === 0" class="empty-msg">
        请从左侧选择一个会话以查看聊天记录
      </div>
      <div v-else>
        <div v-for="(msg, index) in messages" :key="msg.id || index" class="message-item">
          <div v-if="msg.question" class="question">Q: {{ msg.question }}</div>
          <div v-if="msg.answer" class="answer">A: {{ msg.answer }}</div>
          <div class="timestamp">{{ formatTimestamp(msg.timestamp) }}</div>
        </div>
      </div>
    </div>
    <!-- 聊天输入区域 -->
    <ChatInput @send="handleSend" />
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useChatSessionsStore } from '@/store/chatSessionStore';
import ChatInput from '../Chat/ChatInput.vue';
import axios from 'axios';

const chatSessionsStore = useChatSessionsStore();
// 当前会话完整的聊天记录
const messages = computed(() => chatSessionsStore.messages);

// 发送消息，调用 /ask 接口获取真实回复
async function handleSend(text) {
  // 从全局 store 中获取当前选中的会话ID
  const conversationId = chatSessionsStore.selectedSessionId;
  try {
    // 调用 /ask 接口，发送用户提问和会话ID
    const response = await axios.post('/api/v1/ask', {
      text: text,
      conversationId: conversationId
    });
    // 后端返回的响应数据中包含 answer 和 conversationId
    const { answer, conversationId: respConversationId } = response.data;
    // 更新 store 中的会话ID（如果本次请求生成了新会话ID）
    if (!conversationId && respConversationId) {
      chatSessionsStore.selectedSessionId = respConversationId;
    }
    // 构造一条新的聊天记录，根据需要生成一个临时 id
    const newMessage = {
      id: Date.now(), // 临时生成一个唯一 id（可以用后端返回的 id 替换）
      question: text,
      answer: answer,
      timestamp: new Date().toISOString()
    };
    // 追加该条消息到 store 中当前会话的消息记录
    chatSessionsStore.addMessage(newMessage);
  } catch (error) {
    console.error("发送消息失败", error);
  }
}

function formatTimestamp(ts) {
  if (!ts) return '';
  return new Date(ts).toLocaleString();
}
</script>

<style scoped>
.chat-window {
  display: flex;
  flex-direction: column;
  height: 100%;
}
.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
}
.empty-msg {
  text-align: center;
  color: #888;
  margin-top: 20px;
}
.message-item {
  margin-bottom: 15px;
  padding: 8px;
  border-radius: 4px;
  background-color: #f0f0f0;
}
.question {
  font-weight: bold;
  margin-bottom: 4px;
}
.answer {
  margin-left: 20px;
  margin-bottom: 4px;
}
.timestamp {
  font-size: 12px;
  color: #666;
  text-align: right;
}
</style>


