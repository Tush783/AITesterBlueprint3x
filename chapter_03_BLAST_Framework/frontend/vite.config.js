import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'node:path'

// envDir points at the project root so the frontend reads the same
// .env file the backend uses (single source of truth for config).
export default defineConfig({
  plugins: [react()],
  envDir: path.resolve(__dirname, '..'),
  server: {
    port: 5173,
  },
})
