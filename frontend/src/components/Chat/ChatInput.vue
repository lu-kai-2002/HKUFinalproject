<template>
  <!-- fixed-bottom toolbar -->
  <footer class="chat-toolbar">
    <!-- Upload -->
    <label class="upload-btn">
      <svg viewBox="0 0 16 16" class="icon"><path d="M3 10l5-5 5 5H9v5H7v-5z"/></svg>
      <span class="text">Upload Word</span>
      <input type="file" accept=".doc,.docx" @change="handleFile" hidden />
    </label>

    <!-- File status -->
    <span v-if="fileName" class="file-info">
      {{ fileName }} — {{ uploadState }}
    </span>

    <!-- 再次显示但不影响逻辑的模型切换复选框 -->
    <label class="model-toggle">
      <input
          type="checkbox"
          v-model="useFinanceModel"
      />
      Finance-Optimized Mode
    </label>

    <!-- Prompt -->
    <input
        class="prompt"
        type="text"
        v-model="inputText"
        @keyup.enter="emitSend"
        placeholder="Type your message…"
    />

    <!-- Send -->
    <button class="send-btn" @click="emitSend">
      Send
    </button>
  </footer>
</template>

<script setup>
import { ref } from 'vue';
import { uploadWord } from '../../api/file.js';

const emit = defineEmits(['send', 'uploaded']);

const inputText   = ref('');
const fileName    = ref('');
const uploadState = ref('');
// 保留这个状态以便 UI 不崩，但不在 emitSend 中使用它
const useFinanceModel = ref(false);

function handleFile(e) {
  const file = e.target.files[0];
  if (!file) return;
  fileName.value    = file.name;
  uploadState.value = 'Uploading…';
  uploadWord(file)
      .then(({ data }) => {
        uploadState.value = 'Uploaded';
        emit('uploaded', { text: data.text, fileName: file.name });
      })
      .catch(err => {
        console.error('upload failed', err);
        uploadState.value = 'Failed';
      });
}

function emitSend() {
  const prompt = inputText.value.trim();
  if (!prompt) return;
  // 只发文本，不做任何路由分流
  emit('send', prompt);
  inputText.value = '';
}
</script>

<style scoped>
.chat-toolbar {
  position: sticky;
  bottom: 0;
  left: 0;
  width: 100%;
  display: flex;
  gap: 8px;
  padding: 10px;
  background: #fafafa;
  border-top: 1px solid #e0e0e0;
  align-items: center;
  box-shadow: 0 -2px 6px rgba(0,0,0,0.04);
}

.upload-btn {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 6px 10px;
  background: #eef4ff;
  color: #2a65ff;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
  user-select: none;
}
.upload-btn:hover { background:#e2ebff; }
.upload-btn .icon {
  width: 14px; height: 14px; fill: currentColor;
}

.file-info {
  font-size: 12px;
  color: #666;
  max-width: 160px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.model-toggle {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #333;
}
.model-toggle input {
  width: 14px;
  height: 14px;
}

.prompt {
  flex: 1 1 auto;
  padding: 8px 10px;
  border: 1px solid #d0d0d0;
  border-radius: 4px;
  font-size: 14px;
}

.send-btn {
  padding: 8px 18px;
  background: #2a65ff;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}
.send-btn:hover { background:#2154d6; }
</style>








