<template>
  <div class="chat-input">
    <input type="text" v-model="inputText" @keyup.enter="send" placeholder="输入消息..." />
    <button @click="send">发送</button>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const emit = defineEmits(['send']);
const inputText = ref('');

function send() {
  if(inputText.value.trim() !== '') {
    emit('send', inputText.value);
    inputText.value = '';
  }
}
</script>

<style scoped>
.chat-input {
  display: flex;
  padding: 10px;
  border-top: 1px solid #ccc;
}
.chat-input input {
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
