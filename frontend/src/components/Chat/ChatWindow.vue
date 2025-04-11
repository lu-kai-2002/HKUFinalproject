<template>
  <div class="chat-window">
    <div class="message-list">
      <div v-if="messages.length === 0" class="empty-msg">
        请从左侧选择一个会话以查看聊天记录
      </div>
      <div v-else>
        <!-- 遍历消息数组 -->
        <div v-for="(msg, index) in messages" :key="msg.id || index" class="message-item">
          <!-- 显示用户问题 -->
          <div v-if="msg.question" class="question">
            Q: {{ msg.question }}
          </div>
          <!-- 使用 ChatMessage 组件渲染答案（Markdown 格式转换为 HTML） -->
          <ChatMessage v-if="msg.answer" :sender="msg.sender" :content="msg.answer" />
          <!-- 显示时间戳 -->
          <div class="timestamp">{{ formatTimestamp(msg.timestamp) }}</div>
        </div>
      </div>
    </div>
    <ChatInput @send="handleSend" />
  </div>
</template>

<script setup>
import { computed } from 'vue';
import axios from 'axios';
import { useChatSessionsStore } from '@/store/chatSessionStore';
import ChatInput from '../Chat/ChatInput.vue';
import ChatMessage from '../Chat/ChatMessage.vue';

const chatStore = useChatSessionsStore();
const messages = computed(() => chatStore.messages);

// 发送消息处理逻辑：使用 /ask 接口获取真实回答，然后追加消息到 store 中
async function handleSend(text) {
  const conversationId = chatStore.selectedSessionId;
  // 发送用户问题时先乐观更新，可选择先显示“正在回答…”后更新答案
  const tempId = Date.now();
  // 先添加占位消息到 store 中
  chatStore.addMessage({
    id: tempId,
    sender: 'bot',  // 假设回答来自机器人端
    question: text,
    answer: "正在回答…",
    timestamp: new Date().toISOString()
  });
  try {
    const response = await axios.post('/api/v1/ask', { text, conversationId });
    const { answer, conversationId: respConversationId } = response.data;
    if (!conversationId && respConversationId) {
      chatStore.selectedSessionId = respConversationId;
    }
    // 更新先前添加的消息，将占位答案替换为真实答案
    chatStore.updateMessage(tempId, answer);
  } catch (error) {
    console.error("发送消息失败", error);
    // 出错时更新回答为错误提示
    chatStore.updateMessage(tempId, "回答失败，请重试");
  }
}

// 格式化时间戳
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
.timestamp {
  font-size: 12px;
  color: #666;
  text-align: right;
}
</style>



