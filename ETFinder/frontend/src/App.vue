<template>
  <div class="flex min-h-screen">
    <TheSideBar />
    <div class="flex-1">
      <router-view></router-view>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import TheSideBar from './components/common/TheSideBar.vue'
import { useAuthStore } from './stores/auth'

const authStore = useAuthStore()

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