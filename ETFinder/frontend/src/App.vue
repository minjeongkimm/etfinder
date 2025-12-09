<template>
  <div>
    <h2>ETFinder</h2>
    <TheSideBar/>
    <router-view></router-view>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import TheSideBar from './components/common/TheSideBar.vue';
import { useAuthStore } from './stores/auth';

const authStore = useAuthStore()

onMounted(async () => {
  // 토큰은 있는데 유저 정보가 비어있다면 -> 다시 서버에서 가져오기
  if (authStore.token && !authStore.user) {
    console.log("새로고침 감지! 내 정보 다시 불러오는 중...");
    await authStore.getMyInfo();
  }
});


</script>

<style scoped>

</style>