<template>
  <div>
    <!-- 左上角配置按钮 -->
    <button
        @click="open"
        class="fixed top-6 right-6 bg-white hover:bg-gray-100 border rounded p-2 shadow"
        aria-label="Configure stocks"
    >
      ⚙️ Configure
    </button>

    <!-- 侧边栏 / Slide-over -->
    <div
        v-if="visible"
        class="fixed inset-0 z-50 flex"
        aria-modal="true"
        role="dialog"
    >
      <!-- 半透明背景 -->
      <div
          class="fixed inset-0 bg-black bg-opacity-30"
          @click="close"
      ></div>

      <!-- 面板本体 -->
      <div
          class="relative ml-auto w-80 h-full bg-white shadow-xl flex flex-col"
          @click.stop
      >
        <header class="p-4 border-b flex justify-between items-center">
          <h2 class="text-lg font-semibold">Select Stocks</h2>
          <button @click="close" aria-label="Close">&times;</button>
        </header>

        <section class="p-4 flex-1 overflow-y-auto space-y-2">
          <label
              v-for="ticker in allTickers"
              :key="ticker"
              class="flex items-center space-x-2"
          >
            <input
                type="checkbox"
                :value="ticker"
                v-model="selected"
                class="form-checkbox"
            />
            <span>{{ ticker }}</span>
          </label>
        </section>

        <footer class="p-4 border-t flex justify-end space-x-2">
          <button
              @click="close"
              class="px-3 py-1 border rounded hover:bg-gray-100"
          >
            Cancel
          </button>
          <button
              @click="save"
              class="px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600"
          >
            Save
          </button>
        </footer>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, defineEmits } from 'vue'

// 1) 本次可选的 13 只股票列表
const allTickers = [
  'ARBK','BITF','BTBT','CIFR','CLSK',
  'DGHI','HIVE','HUT','IREN','MARA',
  'RIOT','SDIG','WULF'
]

const visible = ref(false)
const selected = ref([])

// 2) 向父组件发事件：更新了哪些股票
const emit = defineEmits(['update:config'])

/** 打开侧边栏 */
function open() {
  visible.value = true
}

/** 关闭侧边栏 */
function close() {
  visible.value = false
}

/** 保存并 emit */
function save() {
  // 存 localStorage
  localStorage.setItem('stockConfig', JSON.stringify(selected.value))
  // 发给父组件
  emit('update:config', selected.value)
  close()
}

// 3) 页面加载时尝试读取上次保存的配置
onMounted(() => {
  const saved = localStorage.getItem('stockConfig')
  if (saved) {
    try {
      selected.value = JSON.parse(saved)
    } catch {}
  }
})
</script>

<style scoped>
/* 如果需要，还可以加一些 scrollbar 样式等等 */
</style>
