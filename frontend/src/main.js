import './index.css'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'

/* ---------- 1. ECharts 按需注册 ---------- */
import * as echarts from 'echarts/core'
import { LineChart } from 'echarts/charts'
import {
    GridComponent,
    TooltipComponent,
    LegendComponent,
    TitleComponent
} from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'

echarts.use([
    LineChart,
    GridComponent,
    TooltipComponent,
    LegendComponent,
    TitleComponent,
    CanvasRenderer
])

/* ---------- 2. vue-echarts 组件 ---------- */
import VueECharts from 'vue-echarts'

/* ---------- 3. 只创建一次应用 ---------- */
const app = createApp(App)

app.component('v-chart', VueECharts)   // 全局注册图表组件
app.use(createPinia())                 // 状态管理
app.use(router)                        // 路由
app.mount('#app')                      // 挂载


