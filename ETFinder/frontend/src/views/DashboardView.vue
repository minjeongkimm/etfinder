<template>
  <div class="container mx-auto max-w-7xl px-6 py-24 min-h-screen">
    <!-- Welcome Section -->
    <div class="mb-12">
      <h1 class="text-4xl font-bold text-slate-900 mb-2">
        안녕하세요, {{ authStore.user?.nickname || '사용자' }}님 👋
      </h1>
      <p class="text-slate-500 text-lg">
        오늘의 시장 흐름을 한눈에 확인해보세요.
      </p>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-12 gap-8">
      <!-- Main Content (Left Column) -->
      <div class="lg:col-span-8 space-y-8">
        <!-- Quick Actions / Shortcuts -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <router-link :to="{ name: 'portfolio' }" class="group relative overflow-hidden rounded-3xl bg-white/40 border border-white/60 p-6 shadow-sm hover:shadow-md transition-all">
             <div class="absolute inset-0 bg-gradient-to-br from-indigo-50/50 to-white/0 opacity-0 group-hover:opacity-100 transition-opacity"></div>
             <div class="relative z-10">
               <div class="w-12 h-12 bg-white rounded-2xl flex items-center justify-center text-2xl shadow-sm mb-4 group-hover:scale-110 transition-transform">
                 💼
               </div>
               <h3 class="text-lg font-bold text-slate-800 mb-1">내 포트폴리오</h3>
               <p class="text-sm text-slate-500">자산 현황 분석하기</p>
             </div>
          </router-link>

          <router-link :to="{ name: 'propensityTest' }" class="group relative overflow-hidden rounded-3xl bg-white/40 border border-white/60 p-6 shadow-sm hover:shadow-md transition-all">
             <div class="absolute inset-0 bg-gradient-to-br from-violet-50/50 to-white/0 opacity-0 group-hover:opacity-100 transition-opacity"></div>
             <div class="relative z-10">
               <div class="w-12 h-12 bg-white rounded-2xl flex items-center justify-center text-2xl shadow-sm mb-4 group-hover:scale-110 transition-transform">
                 🎯
               </div>
               <h3 class="text-lg font-bold text-slate-800 mb-1">맞춤 추천</h3>
               <p class="text-sm text-slate-500">AI 투자 성향 진단</p>
             </div>
          </router-link>

          <router-link :to="{ name: 'mockInvestment' }" class="group relative overflow-hidden rounded-3xl bg-white/40 border border-white/60 p-6 shadow-sm hover:shadow-md transition-all">
             <div class="absolute inset-0 bg-gradient-to-br from-emerald-50/50 to-white/0 opacity-0 group-hover:opacity-100 transition-opacity"></div>
             <div class="relative z-10">
               <div class="w-12 h-12 bg-white rounded-2xl flex items-center justify-center text-2xl shadow-sm mb-4 group-hover:scale-110 transition-transform">
                 chart
               </div>
               <h3 class="text-lg font-bold text-slate-800 mb-1">모의투자</h3>
               <p class="text-sm text-slate-500">실전 같은 투자 연습</p>
             </div>
          </router-link>
        </div>

        <!-- My Assets Summary (Placeholder) -->
        <div class="rounded-3xl bg-white/60 border border-white/60 p-8 shadow-sm">
           <div class="flex items-center justify-between mb-6">
             <h2 class="text-2xl font-bold text-slate-800">내 자산 요약</h2>
             <router-link :to="{ name: 'portfolio' }" class="text-sm font-semibold text-indigo-600 hover:underline">자세히 보기</router-link>
           </div>
           <div class="flex flex-col md:flex-row gap-8 items-center justify-center h-48 text-slate-400">
             <div class="text-center">
                <span class="block text-5xl mb-4">🔒</span>
                <p>로그인 후 자산을 연동해보세요.</p>
             </div>
           </div>
        </div>
      </div>

      <!-- Sidebar (Right Column) -->
      <div class="lg:col-span-4 space-y-8">
        <!-- Real-time Trends Widget (Card Style) -->
        <div class="rounded-3xl bg-white/60 border border-white/60 p-6 shadow-sm">
           <div class="flex items-center justify-between mb-6">
              <h2 class="text-xl font-bold text-slate-900 flex items-center gap-2">
                🔥 실시간 트렌드
              </h2>
              <span class="text-xs font-medium text-slate-500 bg-slate-100 px-2 py-1 rounded-full">
                {{ currentTime }} 기준
              </span>
           </div>
           
           <div class="space-y-4">
              <div v-for="(item, index) in rankingItems" :key="index" class="flex items-center justify-between group p-2 rounded-xl hover:bg-white/50 transition-colors">
                 <div class="flex items-center gap-4">
                    <span class="w-6 text-center font-bold text-lg" :class="index < 3 ? 'text-indigo-600' : 'text-slate-400'">{{ index + 1 }}</span>
                    <span class="font-medium text-slate-700 group-hover:text-indigo-900 transition-colors cursor-pointer" @click="goSearch(item.keyword)">{{ item.keyword }}</span>
                 </div>
                 <div class="text-xs font-medium">
                    <span v-if="item.rankChange > 0" class="text-rose-500 flex items-center gap-1">
                       ▲ {{ item.rankChange }}
                    </span>
                    <span v-else-if="item.rankChange < 0" class="text-blue-500 flex items-center gap-1">
                       ▼ {{ Math.abs(item.rankChange) }}
                    </span>
                    <span v-else class="text-slate-400">-</span>
                 </div>
              </div>
              <div v-if="rankingItems.length === 0" class="text-center py-8 text-slate-400 text-sm">
                데이터를 불러오는 중...
              </div>
           </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { getHourlySearchRanking } from '@/api/ranking'

const authStore = useAuthStore()
const router = useRouter()
const rankingItems = ref([])

const currentTime = computed(() => {
  const now = new Date()
  return `${now.getHours()}시 ${now.getMinutes()}분`
})

const fetchRankings = async () => {
  try {
    const res = await getHourlySearchRanking()
    if (res.data) {
      // API 응답 구조에 따라 데이터 매핑 (rankChange가 있다고 가정)
      rankingItems.value = res.data.map(item => ({
        keyword: item.keyword,
        rankChange: item.rankChange || 0 // API에서 rankChange 제공 가정, 없으면 0
      }))
    }
  } catch (e) {
    console.error("Failed to fetch rankings", e)
  }
}

const goSearch = (keyword) => {
  router.push({ name: 'etfSearch', query: { q: keyword } })
}

onMounted(() => {
  fetchRankings()
})
</script>

<style scoped>
/* Scoped styles if needed */
</style>
