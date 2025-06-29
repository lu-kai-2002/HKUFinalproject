<template>
  <div class="chat-sessions-sidebar">
    <h3 class="title">Session List</h3>

    <!-- 新建会话按钮 -->
    <button class="new-btn" @click="createSession">
      ＋ New Session
    </button>

    <!-- 会话列表 -->
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

const chatStore          = useChatSessionsStore();
const sessions           = computed(() => chatStore.sessions);
const selectedSessionId  = computed(() => chatStore.selectedSessionId);
const errorMsg           = ref('');

/* 选择会话 */
function selectSession(id) {
  chatStore.selectSession(id);
}

/* 新建会话 */
function createSession() {
  const id    = Date.now().toString();          // 临时前端 ID
  const title = 'New Session';
  chatStore.addSession({ conversationId: id, title });
  chatStore.selectSession(id);
}

/* 首次加载 */
onMounted(() => chatStore.fetchSessions());
</script>

<style scoped>
.chat-sessions-sidebar {
  padding: 12px;
  background:#2c3e50;
  color:#ecf0f1;
  height:100%;
}
.title { margin-bottom:10px;font-size:18px;text-align:center; }

.new-btn {
  width:100%;
  padding:6px 0;
  margin-bottom:12px;
  background:#1abc9c;
  color:#fff;
  border:none;
  border-radius:4px;
  cursor:pointer;
  font-size:14px;
}
.new-btn:hover { background:#17a887; }

.sessions-list {
  list-style:none;
  padding:0;
  margin:0;
}
.sessions-list li {
  padding:8px;
  margin-bottom:8px;
  border-radius:4px;
  cursor:pointer;
  transition:background-color .2s;
}
.sessions-list li:hover  { background:#34495e; }
.sessions-list li.active { background:#1abc9c; }

.error { color:#e74c3c;margin-top:10px;text-align:center; }
</style>



