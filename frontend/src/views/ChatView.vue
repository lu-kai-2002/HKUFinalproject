<template>
  <div class="chat-view">
    <ChatWindow />
  </div>
</template>

<script setup>
import ChatWindow from '../components/Chat/ChatWindow.vue';
</script>

<style scoped>
.chat-view {
  height: 100vh;
  display: flex;
  flex-direction: column;
}
</style>
