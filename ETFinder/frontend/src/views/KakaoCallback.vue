<template>
  <div>
    <h3>로그인 처리 중입니다... 잠시만 기다려주세요 🚀</h3>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const route = useRoute();
const authStore = useAuthStore();

onMounted(() => {
  // 1. 주소창의 ?code=... 값 가져오기
  const code = route.query.code;
  
  if (code) {
    // 2. Pinia 액션 호출
    console.log("인가 코드 확인:", code);
    authStore.kakaoLogin(code);
  } else {
    alert("인가 코드가 없습니다.");
  }
});
</script>