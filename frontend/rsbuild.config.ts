import { defineConfig } from '@rsbuild/core';
import { pluginReact } from '@rsbuild/plugin-react';
import { pluginTailwindCSS } from "rsbuild-plugin-tailwindcss";
export default defineConfig({
  plugins: [pluginReact(),pluginTailwindCSS()],
  tools: {
    lightningcssLoader: false,
  }
});
