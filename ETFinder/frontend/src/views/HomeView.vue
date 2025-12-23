<template>
  <div class="min-h-screen bg-background">
    <!-- 메인 콘텐츠 영역 (Two-column layout) -->
    <main class="flex-1 overflow-y-auto">
      <div class="container py-6">
        <!-- Grid: Main Content (Left ~77%) + Sidebar (Right ~23%) -->
        <div class="grid grid-cols-1 lg:grid-cols-[3.3fr_1fr] gap-6">
          
          <!-- ========== LEFT: Main Content ========== -->
          <div class="space-y-6">
            
            <!-- Hero Section (Full Width, Bold Impact) -->
            <section class="bg-gradient-to-br from-background via-background to-primary/[0.02] border border-border rounded-xl px-10 lg:px-16 py-8 lg:py-10">
              <!-- 배지 -->
              <div class="inline-flex items-center gap-2 px-3 py-1.5 bg-primary/10 text-primary text-xs font-medium rounded-full mb-4">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 20a8 8 0 1 0 0-16 8 8 0 0 0 0 16Z"/><path d="M12 14a2 2 0 1 0 0-4 2 2 0 0 0 0 4Z"/><path d="M12 2v2"/><path d="M12 22v-2"/><path d="m17 20.66-1-1.73"/><path d="M11 10.27 7 3.34"/><path d="m20.66 17-1.73-1"/><path d="m3.34 7 1.73 1"/><path d="M14 12h8"/><path d="M2 12h2"/><path d="m20.66 7-1.73 1"/><path d="m3.34 17 1.73-1"/><path d="m17 3.34-1 1.73"/><path d="m11 13.73-4 6.93"/></svg>
                AI 기반 맞춤형 ETF 추천
              </div>

              <!-- 헤드라인 -->
              <h1 class="text-3xl lg:text-4xl xl:text-5xl font-bold text-foreground mb-4 leading-tight tracking-tight">
                당신의 투자 성향에 딱 맞는<br>
                <span class="text-primary">최적의 ETF</span>를 찾아보세요.
              </h1>

              <!-- 서브텍스트 -->
              <p class="text-base text-muted-foreground mb-6 leading-relaxed max-w-3xl">
                복잡한 금융 데이터는 저희가 분석해드립니다. 투자 성향 분석부터 모의투자까지, 안전하고 스마트하게 시작하세요.
              </p>

              <!-- CTA 버튼 -->
              <div class="flex flex-wrap gap-3">
                <router-link
                  :to="{ name: 'propensityTest' }"
                  class="inline-flex items-center gap-2 px-6 py-3 bg-primary text-primary-foreground rounded-lg font-semibold hover:bg-primary/90 transition-all duration-200 shadow-md hover:shadow-lg text-sm"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 3v18h18"/><path d="M18 17V9"/><path d="M13 17V5"/><path d="M8 17v-3"/></svg>
                  투자 성향 분석하기
                </router-link>
                
                <router-link
                  :to="{ name: 'etfSearch' }"
                  class="inline-flex items-center gap-2 px-6 py-3 bg-card text-foreground border border-border rounded-lg font-semibold hover:bg-accent hover:border-primary/30 transition-all duration-200 text-sm"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
                  ETF 전체 둘러보기
                </router-link>
              </div>
            </section>

            <!-- Core Features Section (2x2 Grid - Expanded) -->
            <section>
              <div class="mb-5">
                <h2 class="text-2xl font-bold text-foreground mb-2">ETFinder의 핵심 기능</h2>
                <p class="text-sm text-muted-foreground">복잡한 투자 분석을 쉽고 빠르게</p>
              </div>

              <div class="grid grid-cols-1 sm:grid-cols-2 gap-5">
                <!-- 스마트 검색 -->
                <router-link
                  :to="{ name: 'etfSearch' }"
                  class="group rounded-xl border border-border bg-card p-6 hover:shadow-lg hover:border-primary/40 hover:-translate-y-0.5 transition-all duration-200 cursor-pointer"
                >
                  <div class="flex items-start gap-4">
                    <div class="w-12 h-12 bg-primary/10 rounded-xl flex items-center justify-center flex-shrink-0 group-hover:bg-primary/20 group-hover:scale-110 transition-all duration-200">
                      <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-primary"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
                    </div>
                    <div class="flex-1 min-w-0">
                      <h3 class="text-lg font-bold text-foreground mb-2 group-hover:text-primary transition-colors">스마트 검색</h3>
                      <p class="text-sm text-muted-foreground leading-relaxed">
                        섹터, 수수료, 자산 규모 등 다양한 필터로 원하는 ETF를 찾으세요.
                      </p>
                    </div>
                  </div>
                </router-link>

                <!-- 모의투자 -->
                <router-link
                  to="/mock-investment"
                  class="group rounded-xl border border-border bg-card p-6 hover:shadow-lg hover:border-primary/40 hover:-translate-y-0.5 transition-all duration-200 cursor-pointer"
                >
                  <div class="flex items-start gap-4">
                    <div class="w-12 h-12 bg-primary/10 rounded-xl flex items-center justify-center flex-shrink-0 group-hover:bg-primary/20 group-hover:scale-110 transition-all duration-200">
                      <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-primary"><rect width="18" height="18" x="3" y="3" rx="2"/><path d="M7 7h10"/><path d="M10 7v10"/><path d="M17 7v10"/></svg>
                    </div>
                    <div class="flex-1 min-w-0">
                      <h3 class="text-lg font-bold text-foreground mb-2 group-hover:text-primary transition-colors">모의투자</h3>
                      <p class="text-sm text-muted-foreground leading-relaxed">
                        가상 자산으로 투자 전략과 수익률을 시뮬레이션해보세요.
                      </p>
                    </div>
                  </div>
                </router-link>

                <!-- 포트폴리오 -->
                <router-link
                  :to="{ name: 'portfolio' }"
                  class="group rounded-xl border border-border bg-card p-6 hover:shadow-lg hover:border-primary/40 hover:-translate-y-0.5 transition-all duration-200 cursor-pointer"
                >
                  <div class="flex items-start gap-4">
                    <div class="w-12 h-12 bg-primary/10 rounded-xl flex items-center justify-center flex-shrink-0 group-hover:bg-primary/20 group-hover:scale-110 transition-all duration-200">
                      <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-primary"><path d="M22 12h-4l-3 9L9 3l-3 9H2"/></svg>
                    </div>
                    <div class="flex-1 min-w-0">
                      <h3 class="text-lg font-bold text-foreground mb-2 group-hover:text-primary transition-colors">포트폴리오</h3>
                      <p class="text-sm text-muted-foreground leading-relaxed">
                        관심 있는 ETF를 담고 나만의 최적 포트폴리오를 구성하세요.
                      </p>
                    </div>
                  </div>
                </router-link>


              </div>
            </section>

          </div>

          <!-- ========== RIGHT: Sidebar (Real-time Rankings - Lightweight) ========== -->
          <aside class="space-y-3">
            
            <!-- Period Selector (Shared) -->
            <div class="bg-card border border-border rounded-lg p-3">
              <!-- Header: Title & Date -->
              <div class="flex items-center justify-between mb-3">
                <h3 class="text-xs font-semibold text-muted-foreground uppercase tracking-wide">실시간 트렌드</h3>
                <!-- Date with Clock Icon -->
                <div class="flex items-center gap-1 text-[11px] text-muted-foreground font-medium">
                  <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
                  {{ criteriaDateLabel }}
                </div>
              </div>

              <!-- Segmented Control for Periods -->
              <div class="flex bg-muted/50 rounded-lg p-1">
                <button
                  v-for="period in periods"
                  :key="period.value"
                  @click="selectedPeriod = period.value"
                  :class="[
                    'flex-1 py-1 text-xs font-semibold rounded-md transition-all duration-200',
                    selectedPeriod === period.value
                      ? 'bg-background text-foreground shadow-sm ring-1 ring-border/50'
                      : 'text-muted-foreground hover:text-foreground hover:bg-muted'
                  ]"
                >
                  {{ period.label }}
                </button>
              </div>
            </div>

            <!-- Trending Search Keywords -->
            <div class="bg-muted/20 border border-border/50 rounded-lg p-3">
              <div class="flex items-center gap-1.5 mb-2">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-primary"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
                <h3 class="text-xs font-semibold text-foreground">트렌드 검색어</h3>
              </div>

              <!-- Loading State -->
              <div v-if="loadingSearchRanking" class="flex justify-center py-8">
                <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
              </div>

              <!-- Search Keyword List -->
              <div v-else-if="searchRankings.length > 0" class="space-y-0.5">
                <div
                  v-for="(item, index) in searchRankings.slice(0, 10)"
                  :key="index"
                  @click="goToSearch(item.keyword)"
                  class="group flex items-center gap-1.5 px-2 py-1 rounded hover:bg-accent/50 transition-colors cursor-pointer"
                >
                  <span
                    :class="[
                      'flex-shrink-0 w-4 h-4 flex items-center justify-center text-[10px] font-semibold',
                      index < 3 ? 'text-primary' : 'text-muted-foreground'
                    ]"
                  >
                    {{ index + 1 }}
                  </span>
                  <span class="flex-1 text-xs text-foreground truncate">{{ item.keyword }}</span>
                  
                  <!-- 순위 변동 표시 -->
                  <div class="flex-shrink-0 min-w-[30px] flex justify-end">
                    <!-- New -->
                    <span v-if="item.rankChange === null" class="text-[9px] font-bold text-red-500">NEW</span>
                    <!-- 변동 없음 -->
                    <span v-else-if="item.rankChange === 0" class="text-[10px] text-muted-foreground">-</span>
                    <!-- 상승 -->
                    <div v-else-if="item.rankChange > 0" class="flex items-center text-red-500">
                      <svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 24 24" fill="currentColor" stroke="none"><path d="m12 3-8 12h16z"/></svg>
                      <span class="text-[9px] font-semibold ml-0.5">{{ item.rankChange }}</span>
                    </div>
                    <!-- 하락 -->
                    <div v-else class="flex items-center text-blue-500">
                      <svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 24 24" fill="currentColor" stroke="none"><path d="m12 21 8-12H4z"/></svg>
                      <span class="text-[9px] font-semibold ml-0.5">{{ Math.abs(item.rankChange) }}</span>
                    </div>
                  </div>

                  <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-muted-foreground opacity-0 group-hover:opacity-100 transition-opacity flex-shrink-0"><path d="m9 18 6-6-6-6"/></svg>
                </div>
              </div>

              <!-- Empty State -->
              <div v-else class="py-4 text-center">
                <p class="text-[10px] text-muted-foreground">검색어 데이터가 없습니다.</p>
              </div>
            </div>

            <!-- Trending ETFs -->
            <div class="bg-muted/20 border border-border/50 rounded-lg p-3">
              <div class="flex items-center gap-1.5 mb-2">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-primary"><path d="M3 3v18h18"/><path d="m19 9-5 5-4-4-3 3"/></svg>
                <h3 class="text-xs font-semibold text-foreground">인기 ETF</h3>
              </div>

              <!-- Loading State -->
              <div v-if="loadingEtfRanking" class="flex justify-center py-6">
                <div class="animate-spin rounded-full h-6 w-6 border-b-2 border-primary"></div>
              </div>

              <!-- ETF List -->
              <div v-else-if="rankings.length > 0" class="space-y-0.5">
                <div
                  v-for="(item, index) in rankings.slice(0, 10)"
                  :key="item.etfId"
                  @click="goToEtfDetail(item.etfId)"
                  class="group flex items-center gap-1.5 px-2 py-1 rounded hover:bg-accent/50 transition-colors cursor-pointer"
                >
                  <span
                    :class="[
                      'flex-shrink-0 w-4 h-4 flex items-center justify-center text-[10px] font-semibold',
                      index < 3 ? 'text-primary' : 'text-muted-foreground'
                    ]"
                  >
                    {{ index + 1 }}
                  </span>
                  <span class="flex-1 text-xs text-foreground truncate">{{ item.etfName }}</span>
                  <span v-if="index < 3" class="px-1 py-0.5 bg-red-50 text-red-600 text-[9px] font-bold rounded">HOT</span>
                  <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-muted-foreground opacity-0 group-hover:opacity-100 transition-opacity flex-shrink-0"><path d="m9 18 6-6-6-6"/></svg>
                </div>
              </div>

              <!-- Empty State -->
              <div v-else class="py-4 text-center">
                <p class="text-[10px] text-muted-foreground">ETF 데이터가 없습니다.</p>
              </div>
            </div>

          </aside>

        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import {
    getDailySearchRanking,
    getDailyViewRanking,
    getHourlySearchRanking,
    getHourlyViewRanking,
    getMonthlySearchRanking,
    getMonthlyViewRanking
} from '@/api/ranking'
import { onMounted, ref, watch, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 기간 선택
const periods = [
  { value: 'hourly', label: '1h' },
  { value: 'daily', label: '1d' },
  { value: 'monthly', label: '1m' }
]

const selectedPeriod = ref('hourly')

const criteriaDateLabel = computed(() => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const hours = String(now.getHours()).padStart(2, '0')
  const minutes = String(now.getMinutes()).padStart(2, '0')

  if (selectedPeriod.value === 'hourly') {
    return `${year}-${month}-${day} ${hours}:${minutes}`
  } else if (selectedPeriod.value === 'daily') {
    return `${year}-${month}-${day}`
  } else {
    return `${year}-${month}`
  }
})
const rankings = ref([])
const searchRankings = ref([])
const loadingEtfRanking = ref(false)
const loadingSearchRanking = ref(false)

// ETF 랭킹 데이터 가져오기
const fetchEtfRankings = async () => {
  loadingEtfRanking.value = true
  try {
    let response
    
    if (selectedPeriod.value === 'hourly') {
      response = await getHourlyViewRanking()
    } else if (selectedPeriod.value === 'daily') {
      response = await getDailyViewRanking()
    } else {
      response = await getMonthlyViewRanking()
    }

    if (response.status === 200 || response.status === 204) {
      rankings.value = response.data || []
    } else {
      rankings.value = []
    }
  } catch (error) {
    console.error('ETF 랭킹 데이터 로드 실패:', error)
    rankings.value = []
  } finally {
    loadingEtfRanking.value = false
  }
}

// 검색어 랭킹 데이터 가져오기
const fetchSearchRankings = async () => {
  loadingSearchRanking.value = true
  try {
    let response
    
    if (selectedPeriod.value === 'hourly') {
      response = await getHourlySearchRanking()
    } else if (selectedPeriod.value === 'daily') {
      response = await getDailySearchRanking()
    } else {
      response = await getMonthlySearchRanking()
    }

    if (response.status === 200 || response.status === 204) {
      searchRankings.value = response.data || []
    } else {
      searchRankings.value = []
    }
  } catch (error) {
    console.error('검색어 랭킹 데이터 로드 실패:', error)
    searchRankings.value = []
  } finally {
    loadingSearchRanking.value = false
  }
}

// 모든 랭킹 데이터 로드
const fetchAllRankings = () => {
  fetchEtfRankings()
  fetchSearchRankings()
}

// ETF 상세 페이지로 이동
const goToEtfDetail = (etfId) => {
  router.push({ name: 'etfDetail', params: { etfId } })
}

// 검색 페이지로 이동 (랭킹 키워드 클릭 시)
const goToSearch = (keyword) => {
  router.push({ 
    name: 'etfSearch', 
    query: { keyword } 
  })
}

// 기간 변경 시 데이터 다시 로드
watch(selectedPeriod, () => {
  fetchAllRankings()
})

let pollingInterval = null

// 초기 로드 및 폴링 설정
onMounted(() => {
  fetchAllRankings()
  // 30초마다 데이터 갱신
  pollingInterval = setInterval(fetchAllRankings, 30000)
})

import { onUnmounted } from 'vue'

// 컴포넌트 해제 시 폴링 중단
onUnmounted(() => {
  if (pollingInterval) clearInterval(pollingInterval)
})
</script>

<style scoped>
/* Tailwind로 처리 */
</style>