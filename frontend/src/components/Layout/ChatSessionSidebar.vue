<template>
  <div class="chat-sessions-sidebar">
    <h3 class="title">Session List</h3>
    <ul class="sessions-list">
      <li
          v-for="session in sessions"
          :key="session.conversationId"
          :class="{ active: session.conversationId === selectedSessionId }"
          @click="selectSession(session.conversationId)"
      >
        {{ session.title }}
      </li>
    </ul>
    <div v-if="errorMsg" class="error">{{ errorMsg }}</div>
  </div>
</template>

<script setup>
import { onMounted, computed, ref } from 'vue';
import { useChatSessionsStore } from '@/store/chatSessionStore';

const chatSessionsStore = useChatSessionsStore();
const sessions = computed(() => chatSessionsStore.sessions);
const selectedSessionId = computed(() => chatSessionsStore.selectedSessionId);
const errorMsg = ref('');

// 点击某个会话调用 store 的 selectSession 方法
function selectSession(conversationId) {
  chatSessionsStore.selectSession(conversationId);
}

// 组件挂载时加载会话列表
onMounted(() => {
  chatSessionsStore.fetchSessions();
});
</script>

<style scoped>
.chat-sessions-sidebar {
  padding: 10px;
  background-color: #2c3e50;
  color: #ecf0f1;
  height: 100%;
}
.title {
  margin-bottom: 10px;
  font-size: 18px;
  text-align: center;
}
.sessions-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.sessions-list li {
  padding: 8px;
  margin-bottom: 8px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}
.sessions-list li:hover {
  background-color: #34495e;
}
.sessions-list li.active {
  background-color: #1abc9c;
}
.error {
  color: #e74c3c;
  margin-top: 10px;
  text-align: center;
}
</style>


