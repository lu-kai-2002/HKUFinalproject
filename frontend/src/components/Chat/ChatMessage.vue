<template>
  <div :class="['chat-message', sender]">
    <p>{{ content }}</p>
  </div>
</template>

<script setup>
const props = defineProps({
  sender: {
    type: String,
    required: true
  },
  content: {
    type: String,
    required: true
  }
});
</script>

<style scoped>
.chat-message {
  margin: 5px 0;
  padding: 8px;
  border-radius: 4px;
}
.chat-message.user {
  background-color: #d0f0fd;
  align-self: flex-end;
}
.chat-message.bot {
  background-color: #f0f0f0;
  align-self: flex-start;
}
</style>
