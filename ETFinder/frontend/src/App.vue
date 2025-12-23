<template>
  <div class="relative min-h-screen font-sans bg-background text-foreground selection:bg-indigo-500/30">
     <!-- Global Background Decoration -->
    <div class="fixed inset-0 z-0 pointer-events-none overflow-hidden bg-white">
      <!-- Mesh Gradients: Static, no blur filter -->
      <div class="absolute top-[-10%] right-[-5%] w-[70%] h-[70%] rounded-full opacity-40 mix-blend-multiply filter blur-3xl animate-none"
           style="background: radial-gradient(circle, rgba(129, 140, 248, 0.8) 0%, rgba(129, 140, 248, 0) 70%);"></div>
      <div class="absolute bottom-[-10%] left-[-10%] w-[70%] h-[70%] rounded-full opacity-40 mix-blend-multiply filter blur-3xl animate-none"
           style="background: radial-gradient(circle, rgba(167, 139, 250, 0.8) 0%, rgba(167, 139, 250, 0) 70%);"></div>
      <div class="absolute top-[40%] left-[30%] w-[50%] h-[50%] rounded-full opacity-30 mix-blend-multiply filter blur-3xl animate-none"
           style="background: radial-gradient(circle, rgba(251, 113, 133, 0.8) 0%, rgba(251, 113, 133, 0) 70%);"></div>
    </div>

    <!-- Header (Sticky) -->
    <TheHeader v-if="!isFullScreenPage" />

    <!-- Main Content -->
    <main class="relative z-10">
      <router-view></router-view>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import TheHeader from './components/common/TheHeader.vue'
import { useAuthStore } from './stores/auth'

const route = useRoute()
const authStore = useAuthStore()

// 헤더와 푸터를 숨길 페이지 목록
const isFullScreenPage = computed(() => {
  return ['login', 'kakao-callback', 'onboarding'].includes(route.name)
})

onMounted(async () => {
  if (authStore.token && !authStore.user) {
    console.log("새로고침 감지! 내 정보 다시 불러오는 중...")
    await authStore.getMyInfo()
  }
})
</script>

<style>
/* Global styles are handled in index.css */
</style>