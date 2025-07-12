<!-- StockPage.vue – “美化版” -->
<!-- StockPage.vue -->
<template>
  <div class="px-6 py-8 space-y-8 max-w-6xl mx-auto">
    <h1 class="text-3xl font-bold">
      Stock Backtest <span class="text-gray-400"></span>
    </h1>

    <!-- ① 指标卡 -->
    <div class="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
      <StatCard
          title="Annual Return"
          :value="pct(metrics.annual_return)"
          icon="📈"
          gradient="from-green-400 to-emerald-600"
      />
      <StatCard
          title="Max Drawdown"
          :value="pct(metrics.max_drawdown)"
          icon="📉"
          gradient="from-rose-400 to-rose-600"
      />
      <StatCard
          title="Sharpe Ratio"
          :value="metrics.sharpe_ratio.toFixed(2)"
          icon="⚖️"
          gradient="from-indigo-400 to-violet-600"
      />
    </div>

    <!-- ② 净值曲线 -->
    <div class="card flex flex-col">
      <h2 class="card-title">Equity Curve</h2>
      <VChart :option="chartOption" class="flex-1" style="height:300px;" />
    </div>

    <!-- ③ 交易表 -->
    <div class="card">
      <h2 class="card-title">Trades</h2>
      <div class="overflow-x-auto">
        <table class="min-w-full text-sm">
          <thead>
          <tr class="bg-gray-100 text-left">
            <th
                v-for="th in tableHeaders"
                :key="th"
                class="px-3 py-2 font-semibold whitespace-nowrap"
            >
              {{ th }}
            </th>
          </tr>
          </thead>
          <tbody>
          <tr
              v-for="t in trades"
              :key="t.date + t.action"
              :class="[
                'hover:bg-gray-50',
                t.action === 'buy' ? 'bg-green-50/30' : 'bg-rose-50/30'
              ]"
          >
            <td class="px-3 py-2 whitespace-nowrap">{{ t.date }}</td>
            <td class="px-3 py-2 capitalize">{{ t.action }}</td>
            <td class="px-3 py-2">{{ t.price }}</td>
            <td class="px-3 py-2">{{ t.volume }}</td>
            <td class="px-3 py-2">{{ t.asset }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- ④ 引入解耦后的 AIBot 组件 -->
    <AIBot
        :metrics="metrics"
        :equityCurve="equityCurve"
        :trades="trades"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import VChart from 'vue-echarts'
import StatCard from '@/components/StatCard.vue'
import AIBot from '@/components/AIBot.vue'      // ← 新增

/* ---------- 假数据 ---------- */
const metrics = ref({
  annual_return: 0.12,
  max_drawdown: 0.05,
  sharpe_ratio: 1.5
})

const equityCurve = [
  { date: '2023-01-01', net_value: 1.0 },
  { date: '2023-01-02', net_value: 1.02 },
  { date: '2023-01-03', net_value: 1.01 },
  { date: '2023-01-04', net_value: 1.05 }
]

const trades = ref([
  { date: '2023-01-02', action: 'buy', price: 100, volume: 10, asset: 'AAPL' },
  { date: '2023-01-03', action: 'sell', price: 105, volume: 10, asset: 'AAPL' }
])

/* ---------- 折线图配置 ---------- */
const chartOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: equityCurve.map(e => e.date) },
  yAxis: { type: 'value' },
  series: [
    {
      type: 'line',
      smooth: true,
      name: 'Net Value',
      data: equityCurve.map(e => e.net_value)
    }
  ]
}))

const pct = v => (v * 100).toFixed(2) + '%'
const tableHeaders = ['Date', 'Action', 'Price', 'Volume', 'Asset']
</script>

<style scoped>
.card {
  @apply bg-white rounded-xl shadow-md p-5 space-y-4;
}
.card-title {
  @apply text-lg font-semibold mb-2 text-gray-700;
}
</style>





