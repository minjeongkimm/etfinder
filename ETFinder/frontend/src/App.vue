<template>
  <div class="flex min-h-screen">
    <!-- 로그인 페이지가 아닐 때만 사이드바 표시 -->
    <TheSideBar v-if="!isLoginPage" />
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

// 로그인 페이지인지 확인
const isLoginPage = computed(() => {
  return route.name === 'login' || route.name === 'kakao-callback'
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