<template>
  <div class="min-h-screen bg-background flex items-center justify-center p-4">
    <div class="w-full max-w-md">
      <!-- 로고 및 헤더 -->
      <div class="text-center mb-8">
        <div class="inline-flex items-center gap-2 mb-6">
          <div class="w-12 h-12 bg-primary rounded-xl flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 3v18h18"/><path d="m19 9-5 5-4-4-3 3"/></svg>
          </div>
          <span class="text-2xl font-bold text-foreground">ETFinder</span>
        </div>
        
        <h1 class="text-3xl font-bold text-foreground mb-3">
          로그인
        </h1>
        <p class="text-muted-foreground">
          카카오 계정으로 간편하게 로그인하세요
        </p>
      </div>

      <!-- 로그인 카드 -->
      <div class="rounded-xl border border-border bg-card shadow-lg p-8">
        <!-- 서비스 설명 -->
        <div class="mb-6 p-4 bg-primary/5 rounded-lg border border-primary/10">
          <div class="flex items-start gap-3">
            <div class="w-8 h-8 bg-primary/10 rounded-lg flex items-center justify-center flex-shrink-0 mt-0.5">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-primary"><circle cx="12" cy="12" r="10"/><path d="M12 16v-4"/><path d="M12 8h.01"/></svg>
            </div>
            <div>
              <h3 class="text-sm font-semibold text-foreground mb-1">ETFinder 서비스 이용을 위해</h3>
              <p class="text-xs text-muted-foreground leading-relaxed">
                투자 성향 분석, 포트폴리오 관리, 모의투자 등 맞춤형 기능을 이용하려면 로그인이 필요합니다.
              </p>
            </div>
          </div>
        </div>

        <!-- 카카오 로그인 버튼 -->
        <button
          @click="handleKakaoLogin"
          class="w-full flex items-center justify-center gap-3 px-6 py-4 bg-[#FEE500] hover:bg-[#FDD835] text-[#000000] font-semibold rounded-xl transition-all duration-200 shadow-sm hover:shadow-md"
        >
          <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 3C6.477 3 2 6.477 2 10.8c0 2.827 1.875 5.307 4.687 6.707-.195.72-.687 2.507-.788 2.91-.12.48.165.473.345.343.15-.105 2.355-1.605 3.36-2.295.75.105 1.53.165 2.325.165 5.523 0 10-3.477 10-7.8S17.523 3 12 3z"/>
          </svg>
          <span>카카오로 계속하기</span>
        </button>

        <!-- 추가 안내 -->
        <div class="mt-6 pt-6 border-t border-border">
          <p class="text-xs text-muted-foreground text-center leading-relaxed">
            로그인 시 ETFinder의
            <a href="#" class="text-primary hover:underline">이용약관</a>
            및
            <a href="#" class="text-primary hover:underline">개인정보처리방침</a>에
            동의하게 됩니다.
          </p>
        </div>
      </div>

      <!-- 게스트 안내 -->
      <div class="mt-6 text-center">
        <router-link
          to="/"
          class="inline-flex items-center gap-2 text-sm text-muted-foreground hover:text-foreground transition-colors"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m12 19-7-7 7-7"/><path d="M19 12H5"/></svg>
          홈으로 돌아가기
        </router-link>
      </div>
    </div>

    <!-- 배경 장식 -->
    <div class="fixed top-20 right-20 w-72 h-72 bg-primary/5 rounded-full blur-3xl -z-10"></div>
    <div class="fixed bottom-20 left-20 w-96 h-96 bg-primary/3 rounded-full blur-3xl -z-10"></div>
  </div>
</template>

<script setup>
// import.meta.env 객체를 통해 .env 파일의 값을 가져옴
const REST_API_KEY = import.meta.env.VITE_KAKAO_API_KEY
const REDIRECT_URI = import.meta.env.VITE_REDIRECT_URI

const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`

const handleKakaoLogin = () => {
  window.location.href = kakaoAuthUrl
}
</script>

<style scoped>
/* Tailwind로 처리 */
</style>