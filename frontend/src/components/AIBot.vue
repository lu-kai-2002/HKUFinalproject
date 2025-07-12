<template>
  <div>
    <!-- floating bot icon -->
    <button
        @click="openChat"
        class="fixed bottom-6 right-6 w-12 h-12 rounded-full shadow-lg bg-white flex items-center justify-center hover:shadow-xl transition"
        aria-label="Open chat"
    >
      <img src="@/assets/bot_icon.svg" alt="AI Bot" class="w-8 h-8" />
    </button>

    <!-- chat window -->
    <div
        v-if="chatVisible"
        class="fixed bottom-0 right-0 mb-20 mr-6 w-80 max-h-[60vh] bg-white rounded-xl shadow-2xl flex flex-col"
        role="dialog" aria-modal="true" aria-label="AI Bot Chat"
    >
      <!-- header -->
      <div class="flex justify-between items-center p-3 border-b">
        <h3 class="text-lg font-medium">AI Bot</h3>
        <button @click="closeChat" class="text-gray-500 hover:text-gray-800" aria-label="Close chat">✕</button>
      </div>

      <!-- messages -->
      <div
          ref="chatContent"
          class="flex-1 overflow-y-auto p-3 space-y-2 bg-gray-50"
      >
        <ChatMessage
            v-for="msg in messages"
            :key="msg.id"
            :sender="msg.role"
            :content="msg.content"
        />

        <div v-if="loading" class="self-start text-gray-500 italic">
          Waiting for response...
        </div>
      </div>

      <!-- input area -->
      <div class="p-3 border-t flex space-x-2">
        <input
            v-model="userInput"
            @keyup.enter="sendMessage"
            placeholder="Type your question..."
            class="flex-1 border rounded px-2 py-1 text-sm focus:outline-none"
            aria-label="Type your question"
        />
        <button
            @click="sendMessage"
            class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 transition"
        >
          Send
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import axios from 'axios'
import ChatMessage from '@/components/Chat/ChatMessage.vue'

const props = defineProps({
  metrics: { type: Object, required: true },
  equityCurve: { type: Array, required: true },
  trades: { type: Array, default: () => [] }
})

const chatVisible = ref(false)
const messages = ref([])
const userInput = ref('')
const hasInitialized = ref(false)
const loading = ref(false)
const chatContent = ref(null)

function scrollToBottom() {
  nextTick(() => {
    if (chatContent.value) {
      chatContent.value.scrollTop = chatContent.value.scrollHeight
    }
  })
}

function openChat() {
  chatVisible.value = true
  if (!hasInitialized.value) {
    sendInitialAnalysis()
    hasInitialized.value = true
  }
}

function closeChat() {
  chatVisible.value = false
}

function sendInitialAnalysis() {
  const payload = {
    annualReturn: props.metrics.annual_return,
    maxDrawdown: props.metrics.max_drawdown,
    sharpeRatio: props.metrics.sharpe_ratio,
    equityCurve: props.equityCurve,
    trades: props.trades
  }

  messages.value.push({
    id: Date.now(),
    role: 'user',
    content: `Please analyze the following backtest results:\n${JSON.stringify(payload, null, 2)}`
  })
  scrollToBottom()

  loading.value = true
  axios
      .post('/api/v1/chat', payload)
      .then(res => {
        messages.value.push({
          id: Date.now() + 1,
          role: 'bot',
          content: res.data.answer
        })
      })
      .catch(err => {
        messages.value.push({
          id: Date.now() + 2,
          role: 'bot',
          content: 'Error: ' + err.message
        })
      })
      .finally(() => {
        loading.value = false
        scrollToBottom()
      })
}

function sendMessage() {
  const text = userInput.value.trim()
  if (!text) return

  messages.value.push({ id: Date.now(), role: 'user', content: text })
  scrollToBottom()
  userInput.value = ''

  loading.value = true
  axios
      .post('/api/v1/chat', { message: text })
      .then(res => {
        messages.value.push({
          id: Date.now() + 1,
          role: 'bot',
          content: res.data.answer
        })
      })
      .catch(err => {
        messages.value.push({
          id: Date.now() + 2,
          role: 'bot',
          content: 'Error: ' + err.message
        })
      })
      .finally(() => {
        loading.value = false
        scrollToBottom()
      })
}
</script>

<style scoped>
/* 你可以在这里微调样式，比如调整 loading 文本的间距或字体大小 */
</style>

