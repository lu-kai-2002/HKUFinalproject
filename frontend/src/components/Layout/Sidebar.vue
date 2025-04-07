<template>
  <div class="sidebar-container">
    <h2 class="logo">My App</h2>
    <nav class="nav">
      <router-link to="/" class="nav-item" exact>首页</router-link>
      <router-link to="/chat" class="nav-item">聊天</router-link>

    </nav>
  </div>
</template>

<script setup>
</script>

<style scoped>
.sidebar-container {
  padding: 20px;
}
.logo {
  font-size: 24px;
  margin-bottom: 20px;
  text-align: center;
}
.nav {
  display: flex;
  flex-direction: column;
}
.nav-item {
  display: block;
  padding: 10px;
  margin-bottom: 10px;
  color: #ecf0f1;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.2s;
}
.nav-item:hover {
  background-color: #34495e;
}
.router-link-active {
  background-color: #1abc9c;
}
</style>
