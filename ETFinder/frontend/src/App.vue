<template>
  <div class="relative min-h-screen font-sans bg-background text-foreground selection:bg-blue-500/30">
    <!-- Global Background Decoration -->
    <div v-if="isDecorativeBackgroundPage" class="fixed inset-0 z-0 pointer-events-none overflow-hidden bg-white">
      <!-- Mesh Gradients: Blue & Sky tones -->
      <div class="absolute top-[-10%] right-[-5%] w-[70%] h-[70%] rounded-full opacity-40 mix-blend-multiply filter blur-3xl animate-none"
           style="background: radial-gradient(circle, rgba(59, 130, 246, 0.8) 0%, rgba(59, 130, 246, 0) 70%);"></div>
      <div class="absolute bottom-[-10%] left-[-10%] w-[70%] h-[70%] rounded-full opacity-40 mix-blend-multiply filter blur-3xl animate-none"
           style="background: radial-gradient(circle, rgba(14, 165, 233, 0.8) 0%, rgba(14, 165, 233, 0) 70%);"></div>
      <div class="absolute top-[40%] left-[30%] w-[50%] h-[50%] rounded-full opacity-30 mix-blend-multiply filter blur-3xl animate-none"
           style="background: radial-gradient(circle, rgba(6, 182, 212, 0.8) 0%, rgba(6, 182, 212, 0) 70%);"></div>
    </div>

    <!-- Header (Sticky) -->
    <TheHeader v-if="!isFullScreenPage" />

    <!-- Main Content -->
    <main class="relative z-10" :class="{ 'pt-16': !isFullScreenPage && !isLandingPage }">
      <router-view></router-view>
    </main>

    <!-- Footer -->
    <TheFooter v-if="!isFullScreenPage" />
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import TheHeader from './components/common/TheHeader.vue'
import TheFooter from './components/common/TheFooter.vue'
import { useAuthStore } from './stores/auth'

const route = useRoute()
const authStore = useAuthStore()

// 헤더와 푸터를 숨길 페이지 목록
const isFullScreenPage = computed(() => {
  return ['login', 'kakao-callback', 'onboarding'].includes(route.name)
})

// 랜딩 페이지인지 확인 (헤더와 겹쳐야 함)
const isLandingPage = computed(() => {
  return route.name === 'home'
})

// 배경 데코레이션(Mesh Gradient)이 필요한 페이지인지 확인
const isDecorativeBackgroundPage = computed(() => {
  return ['home', 'dashboard'].includes(route.name)
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