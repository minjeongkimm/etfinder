<template>
  <header class="fixed top-0 left-0 right-0 z-50 transition-all duration-300" :class="[isScrolled ? 'bg-white/90 border-b border-slate-100 shadow-sm' : 'bg-transparent']">
    <div class="container mx-auto max-w-7xl px-6 h-16 flex items-center justify-between">
      <!-- Logo -->
      <router-link to="/" class="flex items-center gap-2 group">
        <div class="w-8 h-8 rounded-lg bg-gradient-to-br from-indigo-600 to-violet-600 flex items-center justify-center text-white font-bold text-lg shadow-lg group-hover:scale-105 transition-transform">
          E
        </div>
        <span class="text-xl font-extrabold tracking-tight text-slate-900 group-hover:text-indigo-600 transition-colors">
          ETFinder
        </span>
      </router-link>

      <!-- Navigation (Desktop) -->
      <nav class="hidden md:flex items-center gap-8">
        <router-link 
          v-for="item in navItems" 
          :key="item.name" 
          :to="{ name: item.route }"
          class="text-sm font-medium text-slate-600 hover:text-indigo-600 transition-colors relative group py-2"
          active-class="text-indigo-600 font-bold"
        >
          {{ item.name }}
          <span class="absolute bottom-0 left-0 w-full h-0.5 bg-indigo-600 scale-x-0 group-hover:scale-x-100 transition-transform origin-left rounded-full"></span>
        </router-link>
      </nav>

      <!-- Right Side: User Menu -->
      <div class="flex items-center gap-4">
        <!-- Login / User Info -->
        <template v-if="authStore.user">
           <div class="hidden md:flex items-center gap-3 pl-4 border-l border-slate-200">
              <div class="text-right hidden lg:block">
                 <div class="text-xs font-bold text-slate-900">{{ authStore.user.nickname }}님</div>
                 <div class="text-[10px] text-slate-500">{{ authStore.user.email }}</div>
              </div>
              <div class="w-8 h-8 rounded-full bg-indigo-100 text-indigo-600 flex items-center justify-center font-bold text-xs border border-indigo-200">
                 {{ authStore.user.nickname?.substring(0, 1) }}
              </div>
              <button @click="handleLogout" class="text-xs font-medium text-slate-400 hover:text-red-500 transition-colors text-nowrap">
                 로그아웃
              </button>
           </div>
        </template>
        <template v-else>
           <router-link :to="{ name: 'login' }" class="text-sm font-bold text-slate-900 hover:text-indigo-600 transition-colors">
             로그인
           </router-link>
           <router-link :to="{ name: 'login' }" class="hidden md:inline-flex items-center justify-center px-4 py-2 bg-slate-900 text-white text-xs font-bold rounded-full hover:bg-slate-800 transition-all hover:scale-105 shadow-lg">
             시작하기
           </router-link>
        </template>
        
        <!-- Mobile Menu Button (Placeholder) -->
        <button class="md:hidden p-2 text-slate-600 hover:bg-slate-100 rounded-lg">
           <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="4" x2="20" y1="12" y2="12"/><line x1="4" x2="20" y1="6" y2="6"/><line x1="4" x2="20" y1="18" y2="18"/></svg>
        </button>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

const navItems = computed(() => {
  if (authStore.user) {
    // Logged In: Dashboard & User features
    return [
      { name: '대시보드', route: 'dashboard' },
      { name: 'ETF 검색', route: 'etfSearch' },
      { name: '포트폴리오', route: 'portfolio' },
      { name: '관심 종목', route: 'like' },
      { name: '마이페이지', route: 'myPage' }
    ]
  } else {
    // Guest: Landing features
    return [
      { name: 'ETF 검색', route: 'etfSearch' },
      // { name: '로그인', route: 'login' } // Handled separately in template
    ]
  }
})

const isScrolled = ref(false)
let ticking = false

const handleScroll = () => {
  if (!ticking) {
    window.requestAnimationFrame(() => {
      isScrolled.value = window.scrollY > 10
      ticking = false
    })
    ticking = true
  }
}

const router = useRouter()

const handleLogout = async () => {
  await authStore.logout()
  // 로그아웃 후 랜딩 페이지로 이동
  router.push('/')
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll, { passive: true })
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>
