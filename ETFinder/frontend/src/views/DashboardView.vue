<template>
  <div class="container mx-auto max-w-7xl px-6 py-24 min-h-screen">
    <!-- Welcome Section -->
    <div class="mb-12">
      <h1 class="text-4xl font-bold text-slate-900 mb-2 flex items-center gap-2">
        안녕하세요, {{ authStore.user?.nickname || '사용자' }}님 <HandRaisedIcon class="w-8 h-8 text-amber-400" />
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
             <div class="absolute inset-0 bg-gradient-to-br from-blue-50/50 to-white/0 opacity-0 group-hover:opacity-100 transition-opacity"></div>
             <div class="relative z-10">
               <div class="w-12 h-12 bg-white rounded-2xl flex items-center justify-center shadow-sm mb-4 group-hover:scale-110 transition-transform">
                 <BriefcaseIcon class="w-6 h-6 text-blue-600" />
               </div>
               <h3 class="text-lg font-bold text-slate-800 mb-1">내 포트폴리오</h3>
               <p class="text-sm text-slate-500">자산 현황 분석하기</p>
             </div>
          </router-link>

          <router-link :to="{ name: 'propensityTest' }" class="group relative overflow-hidden rounded-3xl bg-white/40 border border-white/60 p-6 shadow-sm hover:shadow-md transition-all">
             <div class="absolute inset-0 bg-gradient-to-br from-sky-50/50 to-white/0 opacity-0 group-hover:opacity-100 transition-opacity"></div>
             <div class="relative z-10">
               <div class="w-12 h-12 bg-white rounded-2xl flex items-center justify-center shadow-sm mb-4 group-hover:scale-110 transition-transform">
                 <ClipboardDocumentCheckIcon class="w-6 h-6 text-sky-600" />
               </div>
               <h3 class="text-lg font-bold text-slate-800 mb-1">투자 성향 테스트</h3>
               <p class="text-sm text-slate-500">결과로 확인하는 AI 투자 코멘트</p>
             </div>
          </router-link>

          <router-link :to="{ name: 'like' }" class="group relative overflow-hidden rounded-3xl bg-white/40 border border-white/60 p-6 shadow-sm hover:shadow-md transition-all">
             <div class="absolute inset-0 bg-gradient-to-br from-rose-50/50 to-white/0 opacity-0 group-hover:opacity-100 transition-opacity"></div>
             <div class="relative z-10">
               <div class="w-12 h-12 bg-white rounded-2xl flex items-center justify-center shadow-sm mb-4 group-hover:scale-110 transition-transform">
                 <HeartIcon class="w-6 h-6 text-rose-500" />
               </div>
               <h3 class="text-lg font-bold text-slate-800 mb-1">관심 종목</h3>
               <p class="text-sm text-slate-500">내가 찜한 ETF 모음</p>
             </div>
          </router-link>
        </div>

        <!-- Mock Investment Section -->
        <div class="rounded-3xl bg-white/60 border border-white/60 p-8 shadow-sm">
           <div class="flex items-center justify-between mb-6">
             <h2 class="text-2xl font-bold text-slate-800">모의투자</h2>
             <router-link :to="{ name: 'mockInvestment' }" class="text-sm font-semibold text-blue-600 hover:underline">자세히 보기</router-link>
           </div>

           <div v-if="mockData" class="space-y-6">
             <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
               <div>
                  <p class="text-sm text-slate-500 mb-1">총 자산 평가액</p>
                  <div class="flex items-baseline gap-2">
                    <span class="text-3xl font-bold text-slate-900">
                      {{ formatNumber(mockData.walletResponse.totalAsset) }}원
                    </span>
                  </div>
                  <p :class="['text-sm font-semibold mt-2 flex items-center gap-1', profitRate >= 0 ? 'text-emerald-600' : 'text-rose-600']">
                    <component :is="profitRate >= 0 ? ArrowTrendingUpIcon : ArrowTrendingDownIcon" class="w-4 h-4" />
                    {{ Math.abs(profitRate).toFixed(2) }}%
                  </p>
               </div>

               <div class="flex flex-col justify-center">
                  <p class="text-sm text-slate-500 mb-1">나의 랭킹</p>
                  <div class="flex items-baseline gap-2">
                    <span class="text-3xl font-bold text-slate-900">
                      {{ formatNumber(mockData.ranking.myRank) }}위
                    </span>
                    <span class="text-sm font-medium text-slate-500">
                      (상위 {{ mockData.ranking.topPercent }}%)
                    </span>
                  </div>
               </div>
             </div>
             
             <!-- Top 3 Holdings -->
             <div v-if="topHoldings.length > 0" class="pt-4 border-t border-slate-100">
               <h3 class="text-sm font-bold text-slate-700 mb-4 flex items-center gap-1.5">
                 <TrophyIcon class="w-4 h-4 text-amber-500" /> 수익률 BEST 3
               </h3>
               <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-4">
                 <div v-for="item in topHoldings" :key="item.etfId" class="bg-white/50 border border-slate-100 rounded-xl p-4 hover:bg-white hover:shadow-sm transition-all">
                   <div class="flex items-center gap-2 mb-2">
                     <span class="text-xs font-bold text-slate-500 bg-slate-100 px-1.5 py-0.5 rounded">{{ item.etfCode }}</span>
                     <span class="text-sm font-bold text-slate-800 truncate block flex-1">{{ item.etfName }}</span>
                   </div>
                   <div class="flex items-end justify-between">
                     <div class="text-xs text-slate-500">
                       {{ formatNumber(item.currentPrice) }}원
                     </div>
                     <div :class="['text-sm font-bold', item.profitRate >= 0 ? 'text-emerald-600' : 'text-rose-600']">
                       {{ item.profitRate >= 0 ? '+' : '' }}{{ item.profitRate.toFixed(1) }}%
                     </div>
                   </div>
                 </div>
               </div>
             </div>

             <!-- Asset Chart -->
             <div class="pt-4 border-t border-slate-100">
                <MockAssetTrendChart :trendData="chartData" />
             </div>
           </div>

           <div v-else class="flex flex-col md:flex-row gap-8 items-center justify-center h-32 text-slate-400">
             <div class="text-center flex flex-col items-center">
                <PresentationChartLineIcon class="w-12 h-12 mb-3 text-slate-300" />
                <p>실전 같은 투자 연습을 시작해보세요.</p>
             </div>
           </div>
        </div>
      </div>

      <!-- Sidebar (Right Column) -->
      <div class="lg:col-span-4 space-y-8">
        <!-- Real-time Trends Widget (Card Style) -->
        <!-- Real-time Trends Widget (Card Style) -->
        <div class="rounded-3xl bg-white/60 border border-white/60 p-6 shadow-sm">
           <div class="flex flex-col gap-4 mb-6">
              <div class="flex items-center justify-between">
                 <h2 class="text-xl font-bold text-slate-900 flex items-center gap-2">
                   <FireIcon class="w-5 h-5 text-orange-500" /> 트렌드
                 </h2>
                 <div class="flex items-center gap-1 text-xs font-medium text-slate-500 bg-slate-100 px-2 py-1 rounded-full">
                   <ClockIcon class="w-3.5 h-3.5" />
                   <span>{{ currentTime }}</span>
                 </div>
              </div>
              
              <!-- Period Toggle -->
              <div class="bg-slate-100 p-1 rounded-xl flex text-xs font-medium">
                <button 
                  v-for="period in ['hourly', 'daily', 'monthly']" 
                  :key="period"
                  @click="trendPeriod = period"
                  :class="[
                    'flex-1 py-1.5 rounded-lg transition-all text-center',
                    trendPeriod === period ? 'bg-white text-slate-900 shadow-sm' : 'text-slate-400 hover:text-slate-600'
                  ]"
                >
                  {{ period === 'hourly' ? '1시간' : period === 'daily' ? '1일' : '1개월' }}
                </button>
              </div>
           </div>
           
           <div class="space-y-6">
              <!-- Search Rankings -->
              <div class="space-y-3">
                 <h3 class="text-sm font-bold text-slate-700 flex items-center gap-1.5">
                   <MagnifyingGlassIcon class="w-4 h-4 text-slate-400" /> 인기 검색어
                 </h3>
                  <div class="space-y-2">
                    <div v-for="(item, index) in searchRankings" :key="item.keyword" class="flex items-center justify-between group p-2 rounded-xl hover:bg-white/50 transition-colors">
                       <div class="flex items-center gap-3 overflow-hidden">
                          <span class="w-5 text-center font-bold text-sm shrink-0" :class="index < 3 ? 'text-blue-600' : 'text-slate-400'">{{ index + 1 }}</span>
                          <span class="font-medium text-slate-700 text-sm truncate group-hover:text-blue-900 transition-colors cursor-pointer" @click="goSearch(item.keyword)">{{ item.keyword }}</span>
                       </div>
                       
                       <div class="text-xs font-medium shrink-0 ml-2">
                          <span v-if="item.rankChange === undefined || item.rankChange === null" class="text-rose-500 font-bold px-1.5 py-0.5 bg-rose-50 rounded text-[10px]">NEW</span>
                          <span v-else-if="item.rankChange > 0" class="text-rose-500 flex items-center gap-0.5">
                             <ArrowTrendingUpIcon class="w-3 h-3" /> {{ item.rankChange }}
                          </span>
                          <span v-else-if="item.rankChange < 0" class="text-blue-500 flex items-center gap-0.5">
                             <ArrowTrendingDownIcon class="w-3 h-3" /> {{ Math.abs(item.rankChange) }}
                          </span>
                          <span v-else class="text-slate-400 flex items-center gap-0.5">
                            <MinusIcon class="w-3 h-3" />
                          </span>
                       </div>
                    </div>
                    <div v-if="searchRankings.length === 0" class="text-center py-4 text-slate-400 text-xs text-muted-foreground bg-slate-50/50 rounded-lg">
                      데이터가 없습니다.
                    </div>
                 </div>
              </div>

              <div class="h-px bg-slate-100"></div>

              <!-- View Rankings -->
              <div class="space-y-3">
                 <h3 class="text-sm font-bold text-slate-700 flex items-center gap-1.5">
                   <EyeIcon class="w-4 h-4 text-slate-400" /> 조회수 급상승
                 </h3>
                 <div class="space-y-2">
                    <div v-for="(item, index) in viewRankings" :key="item.keyword" class="flex items-center justify-between group p-2 rounded-xl hover:bg-white/50 transition-colors">
                       <div class="flex items-center gap-3 overflow-hidden">
                          <span class="w-5 text-center font-bold text-sm shrink-0" :class="index < 3 ? 'text-blue-600' : 'text-slate-400'">{{ index + 1 }}</span>
                          <span class="font-medium text-slate-700 text-sm truncate group-hover:text-blue-900 transition-colors cursor-pointer" @click="goDetail(item.etfId)">{{ item.keyword }}</span>
                       </div>

                       <div class="text-xs font-medium shrink-0 ml-2">
                          <span v-if="item.rankChange === undefined || item.rankChange === null" class="text-rose-500 font-bold px-1.5 py-0.5 bg-rose-50 rounded text-[10px]">NEW</span>
                          <span v-else-if="item.rankChange > 0" class="text-rose-500 flex items-center gap-0.5">
                             <ArrowTrendingUpIcon class="w-3 h-3" /> {{ item.rankChange }}
                          </span>
                          <span v-else-if="item.rankChange < 0" class="text-blue-500 flex items-center gap-0.5">
                             <ArrowTrendingDownIcon class="w-3 h-3" /> {{ Math.abs(item.rankChange) }}
                          </span>
                          <span v-else class="text-slate-400 flex items-center gap-0.5">
                            <MinusIcon class="w-3 h-3" />
                          </span>
                       </div>
                    </div>
                    <div v-if="viewRankings.length === 0" class="text-center py-4 text-slate-400 text-xs text-muted-foreground bg-slate-50/50 rounded-lg">
                      데이터가 없습니다.
                    </div>
                 </div>
              </div>
           </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { 
  getHourlySearchRanking, getDailySearchRanking, getMonthlySearchRanking,
  getHourlyViewRanking, getDailyViewRanking, getMonthlyViewRanking
} from '@/api/ranking'
import { getDashboard, getMinuteTrend } from '@/api/mock'
import MockAssetTrendChart from '@/components/mock/MockAssetTrendChart.vue'
import { 
  BriefcaseIcon, 
  ClipboardDocumentCheckIcon, 
  HeartIcon, 
  PresentationChartLineIcon, 
  FireIcon, 
  ArrowTrendingUpIcon, 
  ArrowTrendingDownIcon, 
  MinusIcon,
  HandRaisedIcon,
  EyeIcon,
  MagnifyingGlassIcon,
  ClockIcon,
  TrophyIcon
} from '@heroicons/vue/24/outline'

const authStore = useAuthStore()
const router = useRouter()

// State
const trendPeriod = ref('hourly') // 'hourly', 'daily', 'monthly'
const searchRankings = ref([])
const viewRankings = ref([])
const mockData = ref(null)
const minuteTrendData = ref([])
const now = ref(new Date())
let pollingInterval = null
let clockInterval = null

const currentTime = computed(() => {
  const dateObj = now.value
  const year = dateObj.getFullYear()
  const month = dateObj.getMonth() + 1
  const date = dateObj.getDate()
  const hours = String(dateObj.getHours()).padStart(2, '0')
  const minutes = String(dateObj.getMinutes()).padStart(2, '0')
  return `${year}.${month}.${date} ${hours}:${minutes}`
})

const profitRate = computed(() => {
  if (!mockData.value?.walletResponse) return 0
  const INITIAL_BALANCE = 10000000
  return ((mockData.value.walletResponse.totalAsset - INITIAL_BALANCE) / INITIAL_BALANCE) * 100
})

const topHoldings = computed(() => {
  if (!mockData.value?.holdings) return []
  return [...mockData.value.holdings]
    .map(item => {
      const profitRate = ((item.currentPrice - item.averagePrice) / item.averagePrice) * 100
      return { ...item, profitRate }
    })
    .sort((a, b) => b.profitRate - a.profitRate)
    .slice(0, 3)
})

const chartData = computed(() => {
  if (minuteTrendData.value.length === 0) {
    if (!mockData.value) return []
    return [{
      baseDate: new Date().toISOString(),
      totalAsset: mockData.value.walletResponse.totalAsset,
      realizedProfit: 0
    }]
  }
  return minuteTrendData.value.map(point => ({
    baseDate: point.baseDatetime,
    totalAsset: point.totalAsset,
    realizedProfit: 0
  }))
})

// Data Processing with Fallback
const processRankingData = async (type, period) => {
  let apiFunc
  if (type === 'search') {
    if (period === 'hourly') apiFunc = getHourlySearchRanking
    else if (period === 'daily') apiFunc = getDailySearchRanking
    else apiFunc = getMonthlySearchRanking
  } else {
    if (period === 'hourly') apiFunc = getHourlyViewRanking
    else if (period === 'daily') apiFunc = getDailyViewRanking
    else apiFunc = getMonthlyViewRanking
  }

  try {
    let res = await apiFunc()
    let data = res.data || []

    // Fallback Logic for Hourly (<= 3 items)
    if (period === 'hourly' && data.length <= 3) {
      // console.log(`[Trends] Hourly ${type} data insufficient (${data.length}), falling back to daily.`)
      const fallbackApi = type === 'search' ? getDailySearchRanking : getDailyViewRanking
      res = await fallbackApi()
      data = res.data || []
    }

    return data.map(item => ({
      keyword: item.keyword || item.etfName || item.name, // Handle different field names
      etfId: item.etfId, // For view ranking navigation
      rankChange: item.rankChange, // undefined/null -> NEW logic in template
      count: item.searchCount || item.viewCount
    }))
  } catch (e) {
    console.error(`Failed to fetch ${type} ranking (${period})`, e)
    return []
  }
}

const fetchTrendData = async () => {
  const [search, view] = await Promise.all([
    processRankingData('search', trendPeriod.value),
    processRankingData('view', trendPeriod.value)
  ])
  searchRankings.value = search
  viewRankings.value = view
}

const fetchMockData = async () => {
  try {
    const data = await getDashboard()
    if (data) {
      mockData.value = data
    }
  } catch (e) {
    console.error("Failed to fetch mock/dashboard", e)
  }
}

const fetchMinuteTrend = async () => {
  try {
    const res = await getMinuteTrend(60)
    if (res && res.points) {
      minuteTrendData.value = res.points
    }
  } catch (e) {
    console.error("Failed to fetch minute trend", e)
  }
}

const goSearch = (keyword) => {
  router.push({ name: 'etfSearch', query: { keyword: keyword } })
}

const goDetail = (etfId) => {
  if (etfId) {
    router.push({ name: 'etfDetail', params: { etfId } })
  }
}

const formatNumber = (num) => {
  return num?.toLocaleString('ko-KR') ?? '0'
}

// Watch & Lifecycle
watch(trendPeriod, () => {
  fetchTrendData()
})

onMounted(() => {
  fetchTrendData()
  if (authStore.isAuthenticated) {
    fetchMockData()
    fetchMinuteTrend()
  }

  // 30s Polling for data
  pollingInterval = setInterval(() => {
    fetchTrendData()
    if (authStore.isAuthenticated) {
      fetchMockData()
    }
  }, 30000)

  // 1s Polling for clock
  clockInterval = setInterval(() => {
    now.value = new Date()
  }, 1000)
})

onUnmounted(() => {
  if (pollingInterval) clearInterval(pollingInterval)
  if (clockInterval) clearInterval(clockInterval)
})
</script>

<style scoped>
/* Scoped styles if needed */
</style>
