<template>
  <div class="chat-window">
    <div class="message-list">
      <ChatMessage
          v-for="(msg, index) in messages"
          :key="index"
          :sender="msg.sender"
          :content="msg.content"
      />
    </div>
    <ChatInput @send="handleSend" />
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useChatStore } from '../../store/chatstore';
import ChatMessage from './ChatMessage.vue';
import ChatInput from './ChatInput.vue';

const chatStore = useChatStore();
const messages = computed(() => chatStore.messages);

function handleSend(text) {
  // 调用 Pinia 中的 sendQuestion 方法
  chatStore.sendQuestion(text);
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
</style>

