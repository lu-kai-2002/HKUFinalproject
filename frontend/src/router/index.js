import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ChatView from '../views/ChatView.vue'
import StockView from '../views/StockView.vue'   // 新增

const routes = [
    { path: '/',    name: 'Home',  component: HomeView },
    { path: '/chat', name: 'Chat', component: ChatView },
   { path: '/stock', name: 'Stock', component: StockView },  // 新增
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router


