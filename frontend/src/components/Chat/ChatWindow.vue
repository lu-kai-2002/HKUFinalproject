<script setup>
import { ref, computed } from 'vue';
import dayjs from 'dayjs';                       // ⭐ 新增
import axios from 'axios';
import { useChatSessionsStore } from '@/store/chatSessionStore';
import ChatInput   from '../Chat/ChatInput.vue';
import ChatMessage from '../Chat/ChatMessage.vue';

const chatStore = useChatSessionsStore();
const messages  = computed(() => chatStore.messages);

// 缓存尚未发送的文件内容
const cachedFileText = ref('');
const cachedFileName = ref('');

// 上传完成 → 缓存
function handleUploaded({ text, fileName }) {
  cachedFileText.value = text;
  cachedFileName.value = fileName;
}

// 发送 prompt
async function handleSend(prompt) {
  const fullPrompt = cachedFileText.value
      ? `${prompt}\n\n---\nAttached Document:\n${cachedFileText.value}`
      : prompt;

  const tempId = Date.now();

  // 推送用户提问
  chatStore.addMessage({
    id: tempId,
    sender: 'user',
    question: prompt,
    answer: null,
    timestamp: new Date().toISOString()
  });

  // 推送占位回答
  chatStore.addMessage({
    id: `${tempId}_bot`,
    sender: 'bot',
    question: null,
    answer: '正在回答…',
    timestamp: new Date().toISOString()
  });

  try {
    const { data } = await axios.post('/api/v1/ask', {
      text: fullPrompt,
      conversationId: chatStore.selectedSessionId
    });

    if (!chatStore.selectedSessionId && data.conversationId) {
      chatStore.selectedSessionId = data.conversationId;
    }
    chatStore.updateMessage(`${tempId}_bot`, data.answer);
  } catch (err) {
    console.error('发送失败', err);
    chatStore.updateMessage(`${tempId}_bot`, '回答失败，请重试');
  }

  // 清空文件缓存
  cachedFileText.value = '';
  cachedFileName.value = '';
}

function fmt(ts) {
  if (!ts) return '';
  const iso = /Z$|[+\-]\d\d:?\d\d$/.test(ts) ? ts : `${ts}Z`;
  const d   = dayjs(iso);
  return d.isValid() ? d.format('YYYY/MM/DD HH:mm:ss') : ts;
}

</script>

<template>
  <div class="chat-window">
    <!-- 消息列表 -->
    <div class="message-list">
      <div v-if="messages.length === 0" class="empty-msg">
        Please Choose Session
      </div>

      <template v-else>
        <div v-for="msg in messages" :key="msg.id" class="message-item">
          <div v-if="msg.question" class="question">Q: {{ msg.question }}</div>
          <ChatMessage
              v-if="msg.answer"
              :sender="msg.sender"
              :content="msg.answer"
          />
          <div class="timestamp">{{ fmt(msg.timestamp) }}</div>
        </div>
      </template>
    </div>

    <!-- 待发送文件提示 -->
    <div v-if="cachedFileName" class="pending-file">
      Attached: {{ cachedFileName }}
    </div>

    <!-- 底部工具栏 -->
    <ChatInput
        @send="handleSend"
        @uploaded="handleUploaded"
    />
  </div>
</template>

<style scoped>
.chat-window { display:flex; flex-direction:column; height:100%; }

/* 消息区滚动 */
.message-list { flex:1 1 auto; overflow-y:auto; padding:12px 14px; }

.empty-msg   { text-align:center; color:#888; margin-top:30px; }

.message-item { margin-bottom:14px; }
.question     { font-weight:600; margin-bottom:4px; }

.timestamp { font-size:12px; color:#666; text-align:right; }

/* 待发送文件提示 */
.pending-file {
  font-size:12px;
  color:#888;
  padding:4px 16px;
  background:#f5f5f5;
  border-top:1px solid #e0e0e0;
}
</style>



