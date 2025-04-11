<template>
  <div class="chat-message" :class="sender">
    <!-- 将解析后的 HTML 显示出来 -->
    <div class="message-content" v-html="renderedContent"></div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import MarkdownIt from 'markdown-it';

// 定义组件接收的属性
const props = defineProps({
  sender: {
    type: String,
    required: true
  },
  // 此处 content 就是从数据库中获得的 Markdown 格式字符串
  content: {
    type: String,
    required: true
  }
});

// 创建 markdown-it 实例
const md = new MarkdownIt({
  html: true,         // 允许解析 HTML 标签
  linkify: true,      // 自动识别链接
  typographer: true   // 启用排版替换
});

// 利用计算属性将 Markdown 字符串转换为 HTML
const renderedContent = computed(() => md.render(props.content));
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
.message-content {
  /* 可根据需要定制样式 */
}
</style>
