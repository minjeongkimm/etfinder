<template>
  <aside class="w-64 bg-card border-r border-border sticky top-0 h-screen flex flex-col">
    <!-- 로고 -->
    <div class="p-6 border-b border-border flex-shrink-0">
      <router-link to="/" class="flex items-center gap-2">
        <div class="w-8 h-8 bg-primary rounded-lg flex items-center justify-center">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 3v18h18"/><path d="m19 9-5 5-4-4-3 3"/></svg>
        </div>
        <span class="text-xl font-bold text-foreground">ETFinder</span>
      </router-link>
    </div>

    <!-- 네비게이션 -->
    <nav class="flex-1 overflow-y-auto p-4 space-y-1">
      <!-- 홈 -->
      <router-link
        to="/"
        class="flex items-center gap-3 px-3 py-2.5 text-sm font-medium rounded-lg transition-all duration-200 hover:bg-accent hover:text-accent-foreground relative overflow-hidden"
        :class="$route.path === '/' ? 'bg-gradient-to-r from-primary/[0.08] to-primary/[0.12] text-primary font-semibold shadow-[inset_0_1px_0_0_rgba(255,255,255,0.1),inset_0_-1px_2px_0_rgba(0,0,0,0.05)] before:absolute before:left-0 before:top-0 before:bottom-0 before:w-1 before:bg-gradient-to-b before:from-primary before:to-primary/70 before:rounded-r' : 'text-muted-foreground'"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>
        <span>홈</span>
      </router-link>

      <!-- ETF 탐색 -->
      <router-link
        :to="{ name: 'etfSearch' }"
        class="flex items-center gap-3 px-3 py-2.5 text-sm font-medium rounded-lg transition-all duration-200 hover:bg-accent hover:text-accent-foreground relative overflow-hidden"
        :class="$route.path.startsWith('/etfs') ? 'bg-gradient-to-r from-primary/[0.08] to-primary/[0.12] text-primary font-semibold shadow-[inset_0_1px_0_0_rgba(255,255,255,0.1),inset_0_-1px_2px_0_rgba(0,0,0,0.05)] before:absolute before:left-0 before:top-0 before:bottom-0 before:w-1 before:bg-gradient-to-b before:from-primary before:to-primary/70 before:rounded-r' : 'text-muted-foreground'"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
        <span>ETF 탐색</span>
      </router-link>

      <!-- 투자 성향 테스트 -->
      <router-link
        :to="{ name: 'propensityTest' }"
        class="flex items-center gap-3 px-3 py-2.5 text-sm font-medium rounded-lg transition-all duration-200 hover:bg-accent hover:text-accent-foreground relative overflow-hidden"
        :class="$route.path === '/propensity/test' ? 'bg-gradient-to-r from-primary/[0.08] to-primary/[0.12] text-primary font-semibold shadow-[inset_0_1px_0_0_rgba(255,255,255,0.1),inset_0_-1px_2px_0_rgba(0,0,0,0.05)] before:absolute before:left-0 before:top-0 before:bottom-0 before:w-1 before:bg-gradient-to-b before:from-primary before:to-primary/70 before:rounded-r' : 'text-muted-foreground'"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 3v18h18"/><path d="M18 17V9"/><path d="M13 17V5"/><path d="M8 17v-3"/></svg>
        <span>투자 성향 테스트</span>
      </router-link>

      <!-- 나의 포트폴리오 -->
      <router-link
        :to="{ name: 'portfolio' }"
        class="flex items-center gap-3 px-3 py-2.5 text-sm font-medium rounded-lg transition-all duration-200 hover:bg-accent hover:text-accent-foreground relative overflow-hidden"
        :class="$route.path === '/portfolios' ? 'bg-gradient-to-r from-primary/[0.08] to-primary/[0.12] text-primary font-semibold shadow-[inset_0_1px_0_0_rgba(255,255,255,0.1),inset_0_-1px_2px_0_rgba(0,0,0,0.05)] before:absolute before:left-0 before:top-0 before:bottom-0 before:w-1 before:bg-gradient-to-b before:from-primary before:to-primary/70 before:rounded-r' : 'text-muted-foreground'"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 12h-4l-3 9L9 3l-3 9H2"/></svg>
        <span>나의 포트폴리오</span>
      </router-link>

      <!-- 찜한 ETF -->
      <router-link
        :to="{ name: 'like' }"
        class="flex items-center gap-3 px-3 py-2.5 text-sm font-medium rounded-lg transition-all duration-200 hover:bg-accent hover:text-accent-foreground relative overflow-hidden"
        :class="$route.path === '/likes' ? 'bg-gradient-to-r from-primary/[0.08] to-primary/[0.12] text-primary font-semibold shadow-[inset_0_1px_0_0_rgba(255,255,255,0.1),inset_0_-1px_2px_0_rgba(0,0,0,0.05)] before:absolute before:left-0 before:top-0 before:bottom-0 before:w-1 before:bg-gradient-to-b before:from-primary before:to-primary/70 before:rounded-r' : 'text-muted-foreground'"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z"/></svg>
        <span>찜한 ETF</span>
      </router-link>

      <div class="my-4 border-t border-border"></div>

      <!-- 모의투자 -->
      <router-link
        to="/mock-investment"
        class="flex items-center gap-3 px-3 py-2.5 text-sm font-medium rounded-lg transition-all duration-200 hover:bg-accent hover:text-accent-foreground relative overflow-hidden"
        :class="$route.path === '/mock-investment' ? 'bg-gradient-to-r from-primary/[0.08] to-primary/[0.12] text-primary font-semibold shadow-[inset_0_1px_0_0_rgba(255,255,255,0.1),inset_0_-1px_2px_0_rgba(0,0,0,0.05)] before:absolute before:left-0 before:top-0 before:bottom-0 before:w-1 before:bg-gradient-to-b before:from-primary before:to-primary/70 before:rounded-r' : 'text-muted-foreground'"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="18" height="18" x="3" y="3" rx="2"/><path d="M7 7h10"/><path d="M10 7v10"/><path d="M17 7v10"/></svg>
        <span>모의투자</span>
      </router-link>
    </nav>

    <!-- 하단: 로그인/사용자 정보 (sticky footer) -->
    <div class="mt-auto p-4 border-t border-border flex-shrink-0 bg-card">
      <template v-if="!authStore.isAuthenticated">
        <router-link
          :to="{ name: 'login' }"
          class="flex items-center gap-3 px-3 py-2.5 text-sm font-medium rounded-lg bg-primary text-primary-foreground hover:bg-primary/90 transition-all duration-200 justify-center shadow-sm"
        >
          <span>로그인</span>
        </router-link>
      </template>

      <template v-else>
        <div class="space-y-2">
          <router-link
            :to="{ name: 'myPage' }"
            class="flex items-center gap-3 px-3 py-2.5 text-sm font-medium rounded-lg transition-all duration-200 hover:bg-accent hover:text-accent-foreground text-muted-foreground"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
            <span>마이페이지</span>
          </router-link>
          <button
            @click="handleLogout"
            class="w-full flex items-center gap-3 px-3 py-2.5 text-sm font-medium rounded-lg transition-all duration-200 hover:bg-accent hover:text-accent-foreground text-muted-foreground"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" x2="9" y1="12" y2="12"/></svg>
            <span>로그아웃</span>
          </button>
        </div>
      </template>
    </div>
  </aside>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

const handleLogout = () => {
  authStore.logout()
  alert('로그아웃 되었습니다.')
  router.push('/')
}
</script>

<style scoped>
/* Tailwind로 처리 */
</style>