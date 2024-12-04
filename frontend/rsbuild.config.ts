import { defineConfig } from '@rsbuild/core';
import { pluginReact } from '@rsbuild/plugin-react';
import { pluginTailwindCSS } from "rsbuild-plugin-tailwindcss";
export default defineConfig({
  plugins: [pluginReact(),pluginTailwindCSS()],
  tools: {
    lightningcssLoader: false,
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端服务器地址
        changeOrigin: true, // 确保请求头中的来源与目标一致
        // 不需要重写路径，因为后端接口同样有 /api 前缀
      },
    },
  },
});
