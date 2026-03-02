<template>
  <aside class="w-64 glass-panel border-r border-border/50 sticky top-0 h-screen flex flex-col z-50">
    <!-- 로고 -->
    <div class="p-6 border-b border-border/50 flex-shrink-0">
      <router-link to="/" class="flex items-center gap-3 group">
        <div class="relative w-9 h-9 flex items-center justify-center">
          <div class="absolute inset-0 bg-primary/20 rounded-xl blur-sm group-hover:blur-md transition-all"></div>
          <div class="relative w-full h-full bg-gradient-to-br from-primary to-primary/80 rounded-xl flex items-center justify-center shadow-inner border border-white/20">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M3 3v18h18"/><path d="m19 9-5 5-4-4-3 3"/></svg>
          </div>
        </div>
        <div class="flex flex-col">
          <span class="text-xl font-bold text-foreground leading-none tracking-tight">ETFinder</span>
          <span class="text-[10px] text-muted-foreground font-medium tracking-wider">SMART INVESTING</span>
        </div>
      </router-link>
    </div>

    <!-- 네비게이션 -->
    <nav class="flex-1 overflow-y-auto p-4 space-y-1.5 scrollbar-hide">
      <!-- 홈 -->
      <router-link
        to="/"
        class="flex items-center gap-3 px-3.5 py-3 text-sm font-medium rounded-xl transition-all duration-200 group relative overflow-hidden"
        :class="$route.path === '/' 
          ? 'text-primary font-semibold bg-primary/10 shadow-sm' 
          : 'text-muted-foreground hover:text-foreground hover:bg-muted/50'"
      >
        <div class="absolute left-0 top-1/2 -translate-y-1/2 w-1 h-8 bg-primary rounded-r-full transition-transform duration-200"
             :class="$route.path === '/' ? 'scale-y-100 opacity-100' : 'scale-y-0 opacity-0'"></div>
        
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="transition-colors" :class="$route.path === '/' ? 'text-primary' : 'group-hover:text-foreground'"><path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>
        <span>홈</span>
      </router-link>

      <!-- ETF 탐색 -->
      <router-link
        :to="{ name: 'etfSearch' }"
        class="flex items-center gap-3 px-3.5 py-3 text-sm font-medium rounded-xl transition-all duration-200 group relative overflow-hidden"
        :class="$route.path.startsWith('/etfs') 
          ? 'text-primary font-semibold bg-primary/10 shadow-sm' 
          : 'text-muted-foreground hover:text-foreground hover:bg-muted/50'"
      >
        <div class="absolute left-0 top-1/2 -translate-y-1/2 w-1 h-8 bg-primary rounded-r-full transition-transform duration-200"
             :class="$route.path.startsWith('/etfs') ? 'scale-y-100 opacity-100' : 'scale-y-0 opacity-0'"></div>

        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="transition-colors" :class="$route.path.startsWith('/etfs') ? 'text-primary' : 'group-hover:text-foreground'"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
        <span>ETF 탐색</span>
      </router-link>

      <!-- 투자 성향 테스트 -->
      <router-link
        :to="{ name: 'propensityTest' }"
        class="flex items-center gap-3 px-3.5 py-3 text-sm font-medium rounded-xl transition-all duration-200 group relative overflow-hidden"
        :class="$route.path === '/propensity/test' 
          ? 'text-primary font-semibold bg-primary/10 shadow-sm' 
          : 'text-muted-foreground hover:text-foreground hover:bg-muted/50'"
      >
        <div class="absolute left-0 top-1/2 -translate-y-1/2 w-1 h-8 bg-primary rounded-r-full transition-transform duration-200"
             :class="$route.path === '/propensity/test' ? 'scale-y-100 opacity-100' : 'scale-y-0 opacity-0'"></div>

        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="transition-colors" :class="$route.path === '/propensity/test' ? 'text-primary' : 'group-hover:text-foreground'"><path d="M3 3v18h18"/><path d="M18 17V9"/><path d="M13 17V5"/><path d="M8 17v-3"/></svg>
        <span>투자 성향 테스트</span>
      </router-link>

      <!-- 나의 포트폴리오 -->
      <router-link
        :to="{ name: 'portfolio' }"
        class="flex items-center gap-3 px-3.5 py-3 text-sm font-medium rounded-xl transition-all duration-200 group relative overflow-hidden"
        :class="$route.path === '/portfolios' 
          ? 'text-primary font-semibold bg-primary/10 shadow-sm' 
          : 'text-muted-foreground hover:text-foreground hover:bg-muted/50'"
      >
        <div class="absolute left-0 top-1/2 -translate-y-1/2 w-1 h-8 bg-primary rounded-r-full transition-transform duration-200"
             :class="$route.path === '/portfolios' ? 'scale-y-100 opacity-100' : 'scale-y-0 opacity-0'"></div>

        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="transition-colors" :class="$route.path === '/portfolios' ? 'text-primary' : 'group-hover:text-foreground'"><path d="M22 12h-4l-3 9L9 3l-3 9H2"/></svg>
        <span>나의 포트폴리오</span>
      </router-link>

      <!-- 찜한 ETF -->
      <router-link
        :to="{ name: 'like' }"
        class="flex items-center gap-3 px-3.5 py-3 text-sm font-medium rounded-xl transition-all duration-200 group relative overflow-hidden"
        :class="$route.path === '/likes' 
          ? 'text-primary font-semibold bg-primary/10 shadow-sm' 
          : 'text-muted-foreground hover:text-foreground hover:bg-muted/50'"
      >
        <div class="absolute left-0 top-1/2 -translate-y-1/2 w-1 h-8 bg-primary rounded-r-full transition-transform duration-200"
             :class="$route.path === '/likes' ? 'scale-y-100 opacity-100' : 'scale-y-0 opacity-0'"></div>

        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="transition-colors" :class="$route.path === '/likes' ? 'text-primary' : 'group-hover:text-foreground'"><path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z"/></svg>
        <span>찜한 ETF</span>
      </router-link>

      <div class="my-3 border-t border-border/50 mx-2"></div>

      <!-- 모의투자 -->
      <router-link
        to="/mock-investment"
        class="flex items-center gap-3 px-3.5 py-3 text-sm font-medium rounded-xl transition-all duration-200 group relative overflow-hidden"
        :class="$route.path === '/mock-investment' 
          ? 'text-primary font-semibold bg-primary/10 shadow-sm' 
          : 'text-muted-foreground hover:text-foreground hover:bg-muted/50'"
      >
        <div class="absolute left-0 top-1/2 -translate-y-1/2 w-1 h-8 bg-primary rounded-r-full transition-transform duration-200"
             :class="$route.path === '/mock-investment' ? 'scale-y-100 opacity-100' : 'scale-y-0 opacity-0'"></div>

        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="transition-colors" :class="$route.path === '/mock-investment' ? 'text-primary' : 'group-hover:text-foreground'"><rect width="18" height="18" x="3" y="3" rx="2"/><path d="M7 7h10"/><path d="M10 7v10"/><path d="M17 7v10"/></svg>
        <span>모의투자</span>
      </router-link>
    </nav>

    <!-- 하단: 로그인/사용자 정보 (sticky footer) -->
    <div class="mt-auto p-4 border-t border-border/50 flex-shrink-0 bg-transparent">
      <template v-if="!authStore.isAuthenticated">
        <router-link
          :to="{ name: 'login' }"
          class="flex items-center gap-3 px-3 py-3 w-full text-sm font-semibold rounded-xl bg-primary text-primary-foreground hover:bg-primary/90 transition-all duration-200 justify-center shadow-lg shadow-primary/20 hover:shadow-primary/30"
        >
          <span>시작하기</span>
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12h14"/><path d="m12 5 7 7-7 7"/></svg>
        </router-link>
      </template>

      <template v-else>
        <div class="p-3 rounded-xl bg-muted/30 border border-border/50">
          <div class="flex items-center gap-3 mb-3">
             <div class="w-8 h-8 rounded-full bg-gradient-to-tr from-primary/20 to-secondary flex items-center justify-center text-xs font-bold text-primary">
                {{ authStore.user?.nickname?.substring(0,1) || 'U' }}
             </div>
             <div class="flex-1 min-w-0">
               <p class="text-sm font-semibold text-foreground truncate">{{ authStore.user?.nickname || '사용자' }}</p>
               <p class="text-[10px] text-muted-foreground truncate">{{ authStore.user?.email || '이메일 없음' }}</p>
             </div>
          </div>
          
          <div class="grid grid-cols-2 gap-2">
            <router-link
              :to="{ name: 'myPage' }"
              class="flex items-center justify-center gap-1.5 px-2 py-1.5 text-xs font-medium rounded-lg bg-background border border-border/50 hover:bg-accent hover:text-accent-foreground transition-colors"
            >
              <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
              마이페이지
            </router-link>
            <button
              @click="handleLogout"
              class="flex items-center justify-center gap-1.5 px-2 py-1.5 text-xs font-medium rounded-lg bg-background border border-border/50 hover:bg-destructive/10 hover:text-destructive hover:border-destructive/20 transition-colors"
            >
              <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" x2="9" y1="12" y2="12"/></svg>
              로그아웃
            </button>
          </div>
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
.scrollbar-hide::-webkit-scrollbar {
    display: none;
}
.scrollbar-hide {
    -ms-overflow-style: none;
    scrollbar-width: none;
}
</style>