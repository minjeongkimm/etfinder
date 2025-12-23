<template>
  <div class="min-h-screen bg-background flex items-center justify-center p-4">
    <div class="text-center">
      <!-- 로고 -->
      <div class="inline-flex items-center gap-2 mb-8">
        <div class="w-12 h-12 bg-primary rounded-xl flex items-center justify-center">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 3v18h18"/><path d="m19 9-5 5-4-4-3 3"/></svg>
        </div>
        <span class="text-2xl font-bold text-foreground">ETFinder</span>
      </div>

      <!-- 로딩 스피너 -->
      <div class="mb-6">
        <div class="animate-spin rounded-full h-16 w-16 border-b-4 border-primary mx-auto"></div>
      </div>

      <!-- 메시지 -->
      <h2 class="text-2xl font-bold text-foreground mb-2">로그인 처리 중입니다</h2>
      <p class="text-muted-foreground">잠시만 기다려주세요...</p>
    </div>

    <!-- 배경 장식 -->
    <div class="fixed top-20 right-20 w-72 h-72 bg-primary/5 rounded-full blur-3xl -z-10"></div>
    <div class="fixed bottom-20 left-20 w-96 h-96 bg-primary/3 rounded-full blur-3xl -z-10"></div>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
import { onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

onMounted(() => {
  // 1. 주소창의 ?code=... 값 가져오기
  const code = route.query.code
  
  if (code) {
    // 2. Pinia 액션 호출
    console.log("인가 코드 확인:", code)
    authStore.kakaoLogin(code).then((data) => {
      // data가 없을 수도 있으므로 체크
      if (data && data.isNewMember) {
        // 신규 회원이면 온보딩 페이지로 이동 (닉네임 router state로 전달)
        router.push({ 
          name: 'onboarding', 
          state: { nickname: data.nickname } 
        })
      } else {
        // 기존 회원이면 대시보드로 이동
        router.replace({ name: 'dashboard' })
      }
    })
  } else {
    alert("인가 코드가 없습니다.")
    // 로그인 페이지로 리다이렉트
    window.location.href = "/login"
  }
})
</script>

<style scoped>
/* Tailwind로 처리 */
</style>