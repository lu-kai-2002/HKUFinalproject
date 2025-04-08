<template>
  <div class="chat-input">
    <!-- 文件上传按钮 -->
    <input type="file" accept=".doc,.docx" @change="handleFileUpload" />
    <!-- 文本输入框 -->
    <input type="text" v-model="inputText" @keyup.enter="send" placeholder="输入消息..." />
    <button @click="send">发送</button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { uploadWord } from '../../api/file.js';  // 或 '../api/file.js'
const emit = defineEmits(['send']);
const inputText = ref('');

function send() {
  if(inputText.value.trim() !== '') {
    emit('send', inputText.value);
    inputText.value = '';
  }
}

function handleFileUpload(event) {
  const file = event.target.files[0];
  if(file) {
    uploadWordFile(file);
  }
}

async function uploadWordFile(file) {
  try {
    const response = await uploadWord(file);
    // 假设后端返回的结构是: { text: "文件解析后的文本" }
    const { text } = response.data;
    // 这里可以将解析后的文本自动填充到输入框，也可以直接调用 send
    inputText.value = text;
    // 如果希望自动发送，取消下面的注释
    // send();
  } catch (error) {
    console.error("上传并解析 Word 文件失败", error);
  }
}
</script>

<style scoped>
.chat-input {
  display: flex;
  align-items: center;
  padding: 10px;
  border-top: 1px solid #ccc;
}
.chat-input input[type="text"] {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.chat-input input[type="file"] {
  margin-right: 10px;
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

