<template>
  <div class="layout">
    <Sidebar class="sidebar" />
    <div class="main-content">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import Sidebar from './Sidebar.vue';
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
}
.sidebar {
  width: 240px;
  background-color: #2c3e50;
  color: #ecf0f1;
}
.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f5f5f5;
}
</style>
