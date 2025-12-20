<template>
  <div class="flex min-h-screen">
    <!-- 전체 화면 페이지(로그인, 온보딩 등)가 아닐 때만 사이드바 표시 -->
    <TheSideBar v-if="!isFullScreenPage" />
    <div class="flex-1">
      <router-view></router-view>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import TheSideBar from './components/common/TheSideBar.vue'
import { useAuthStore } from './stores/auth'

const route = useRoute()
const authStore = useAuthStore()

// 사이드바를 숨길 페이지 목록
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