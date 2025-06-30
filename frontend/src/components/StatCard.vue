<template>
  <div :class="['rounded-xl shadow-lg text-white p-5 bg-gradient-to-br', gradient]">
    <div class="flex items-center justify-between">
      <span class="text-4xl">{{ icon }}</span>
      <div class="text-right">
        <h3 class="text-sm uppercase opacity-90">{{ title }}</h3>
        <p class="text-2xl font-semibold mt-1">{{ value }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
/* 纯 JavaScript 项目 → 直接声明 props；若以后切 TS 再加类型 */
defineProps({
  title: String,
  value: String,
  icon: String,
  gradient: String
})
</script>
