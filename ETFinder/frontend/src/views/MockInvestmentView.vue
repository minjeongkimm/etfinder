<template>
  <div class="flex min-h-screen bg-background">
    <main class="flex-1 p-6 lg:p-8">
      <div class="container max-w-4xl mx-auto">
        <!-- 준비 중 상태 -->
        <div class="rounded-xl border border-border bg-card shadow-sm p-12 text-center">
          <!-- 아이콘 -->
          <div class="w-24 h-24 bg-primary/10 rounded-full mx-auto mb-6 flex items-center justify-center">
            <svg 
              class="w-12 h-12 text-primary" 
              fill="none" 
              viewBox="0 0 24 24" 
              stroke="currentColor"
            >
              <path 
                stroke-linecap="round" 
                stroke-linejoin="round" 
                stroke-width="2" 
                d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z" 
              />
            </svg>
          </div>

          <!-- 메인 메시지 -->
          <h1 class="text-3xl font-bold text-foreground mb-4">
            🚧 모의투자 기능 준비 중
          </h1>
          
          <!-- 설명 -->
          <p class="text-lg text-muted-foreground mb-8 leading-relaxed">
            모의투자 기능은 현재 개발 중입니다.<br>
            향후 업데이트를 통해 제공될 예정입니다.
          </p>

          <!-- 구분선 -->
          <div class="my-8 border-t border-border"></div>

          <!-- 기능 설명 카드 -->
          <div class="bg-muted/50 rounded-lg p-6 mb-8 text-left">
            <h2 class="text-lg font-bold text-foreground mb-4 flex items-center gap-2">
              <svg class="w-5 h-5 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              모의투자란?
            </h2>
            <p class="text-sm text-muted-foreground leading-relaxed mb-4">
              모의투자는 실제 돈을 사용하지 않고 가상 자금으로 ETF 매매를 체험할 수 있는 게임형 투자 시뮬레이터입니다.
            </p>
            <ul class="space-y-2 text-sm text-muted-foreground">
              <li class="flex items-start gap-2">
                <svg class="w-5 h-5 text-chart-1 flex-shrink-0 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
                <span>가상 자금으로 ETF 매수/매도 연습</span>
              </li>
              <li class="flex items-start gap-2">
                <svg class="w-5 h-5 text-chart-1 flex-shrink-0 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
                <span>실시간 수익률 추적 및 포트폴리오 관리</span>
              </li>
              <li class="flex items-start gap-2">
                <svg class="w-5 h-5 text-chart-1 flex-shrink-0 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
                <span>투자 전략 테스트 및 학습</span>
              </li>
            </ul>
          </div>

          <!-- 대안 안내 -->
          <div class="bg-primary/5 rounded-lg p-6 text-left border border-primary/20">
            <h3 class="text-md font-semibold text-foreground mb-2 flex items-center gap-2">
              <svg class="w-5 h-5 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
              </svg>
              대신 시뮬레이션을 이용해보세요!
            </h3>
            <p class="text-sm text-muted-foreground mb-4">
              포트폴리오 기반으로 과거 1년 수익률을 계산할 수 있습니다.
            </p>
            <router-link 
              to="/portfolios"
              class="inline-flex items-center gap-2 px-5 py-2.5 bg-primary text-primary-foreground rounded-lg font-semibold hover:bg-primary/90 transition-all duration-200 shadow-sm hover:shadow-md"
            >
              <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-6l-2-2H5a2 2 0 00-2 2z" />
              </svg>
              포트폴리오로 이동 →
            </router-link>
          </div>

          <!-- 추가 액션 버튼 -->
          <div class="mt-8 flex justify-center gap-4">
            <router-link 
              to="/"
              class="px-6 py-3 border border-border rounded-lg font-medium text-foreground hover:bg-accent transition-all duration-200"
            >
              홈으로 돌아가기
            </router-link>
            <router-link 
              to="/etfs"
              class="px-6 py-3 border border-border rounded-lg font-medium text-foreground hover:bg-accent transition-all duration-200"
            >
              ETF 탐색하기
            </router-link>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
// 준비 중 페이지이므로 특별한 로직 없음
</script>

<style scoped>
/* Tailwind로 처리 */
</style>
