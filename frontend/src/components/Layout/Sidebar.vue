<!-- src/components/Layout/Sidebar.vue -->
<template>
  <div class="sidebar-container">
    <h2 class="logo">My App</h2>
    <nav class="nav">
      <router-link to="/" class="nav-item" exact>Home</router-link>
      <router-link to="/stock" class="nav-item" exact>Stock</router-link>
      <!-- 嵌套一个区块用于聊天导航和下一级聊天记录 -->
      <div class="chat-menu">
        <router-link to="/chat" class="nav-item">Chat</router-link>
        <!-- 如果当前路由为 /chat，则显示下一级的聊天记录侧边栏 -->
        <ChatSessionsSidebar v-if="showNestedSidebar" class="nested-sidebar" />
      </div>
      <!-- 如果有其他导航项，可继续添加 -->
    </nav>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import ChatSessionsSidebar from './ChatSessionSidebar.vue';

const route = useRoute();
const showNestedSidebar = computed(() => route.path === '/chat');
</script>

<style scoped>
.sidebar-container {
  padding: 20px;
}
.logo {
  font-size: 24px;
  margin-bottom: 20px;
  text-align: center;
}
.nav {
  display: flex;
  flex-direction: column;
}
.nav-item {
  display: block;
  padding: 10px;
  margin-bottom: 10px;
  color: #ecf0f1;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.2s;
}
.nav-item:hover {
  background-color: #34495e;
}
.router-link-active {
  background-color: #1abc9c;
}
/* 专门为嵌套侧边栏设置样式 */
.chat-menu {
  margin-top: 10px;
}
.nested-sidebar {
  margin-left: 10px;
}
</style>

