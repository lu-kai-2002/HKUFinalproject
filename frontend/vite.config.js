import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    host: '0.0.0.0', // 允许局域网访问
    port: 5173,      // 明确指定端口
    strictPort: true, // 端口占用时直接报错
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '/api'), // 关键修改：保留基础路径
        configure: (proxy) => {
          // 添加调试日志
          proxy.on('proxyReq', (proxyReq) => {
            console.log('[Vite Proxy] 转发请求:',
                proxyReq.method,
                proxyReq.path
            )
          })
          proxy.on('error', (err) => {
            console.error('[Vite Proxy] 错误:', err)
          })
        }
      }
    }
  }
})