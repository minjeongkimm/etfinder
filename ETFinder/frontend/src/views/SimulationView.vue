<template>
  <div class="flex min-h-screen bg-background">
    <main class="flex-1 p-6 lg:p-8">
      <div class="container">
        <!-- 헤더 -->
        <header class="mb-8">
          <div class="flex items-center gap-3 mb-3">
            <div class="w-10 h-10 rounded-xl bg-primary flex items-center justify-center">
              <svg class="w-6 h-6 text-primary-foreground" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
              </svg>
            </div>
            <h1 class="text-3xl font-bold tracking-tight text-foreground">⚡ 시뮬레이션</h1>
          </div>
          <p class="text-sm text-muted-foreground">포트폴리오를 기반으로 과거 1년 수익률을 시뮬레이션합니다.</p>
        </header>

        <!-- 로딩 상태 -->
        <div v-if="bookmarkStore.loading" class="flex justify-center items-center py-20">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
        </div>

        <!-- 에러 상태 -->
        <div v-else-if="bookmarkStore.error" class="rounded-xl border border-destructive bg-destructive/10 p-6 text-center">
          <p class="text-destructive">{{ bookmarkStore.error }}</p>
        </div>

        <!-- 빈 상태 (북마크가 없는 경우) -->
        <div v-else-if="bookmarkStore.bookmarkedEtfs.length === 0" class="rounded-xl border border-border bg-card shadow-sm p-12 text-center">
          <div class="w-20 h-20 bg-muted rounded-full mx-auto mb-4 flex items-center justify-center">
            <svg class="w-10 h-10 text-muted-foreground" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-6l-2-2H5a2 2 0 00-2 2z" />
            </svg>
          </div>
          <h3 class="text-xl font-semibold text-foreground mb-2">포트폴리오가 비어있습니다</h3>
          <p class="text-muted-foreground mb-6">ETF를 담은 후 시뮬레이션을 실행해 주세요.</p>
          <router-link 
            to="/etfs"
            class="inline-block px-6 py-3 bg-primary text-primary-foreground rounded-lg font-semibold hover:bg-primary/90 transition-all duration-200"
          >
            ETF 탐색하기 →
          </router-link>
        </div>

        <!-- 메인 콘텐츠 -->
        <div v-else class="space-y-6">
          <!-- 투자 금액 입력 카드 -->
          <div class="rounded-xl border border-border bg-card shadow-sm p-6">
            <h2 class="text-lg font-bold text-foreground mb-4">투자 금액 설정</h2>
            <div class="flex items-end gap-4">
              <div class="flex-1">
                <label class="block text-sm font-medium text-muted-foreground mb-2">총 투자 금액</label>
                <input
                  v-model="investmentAmount"
                  type="text"
                  class="w-full px-4 py-3 border border-border rounded-lg bg-background text-foreground font-mono text-lg focus:outline-none focus:ring-2 focus:ring-primary"
                  placeholder="10,000,000"
                  @input="formatInvestmentAmount"
                />
              </div>
              <button
                @click="handleRunSimulation"
                :disabled="!canRunSimulation || isRunning"
                :class="[
                  'px-8 py-3 rounded-lg font-semibold transition-all duration-200',
                  canRunSimulation && !isRunning
                    ? 'bg-primary text-primary-foreground hover:bg-primary/90 shadow-sm hover:shadow-md'
                    : 'bg-muted text-muted-foreground cursor-not-allowed'
                ]"
              >
                <span v-if="isRunning" class="flex items-center gap-2">
                  <div class="animate-spin rounded-full h-4 w-4 border-b-2 border-primary-foreground"></div>
                  실행 중...
                </span>
                <span v-else>시뮬레이션 실행하기</span>
              </button>
            </div>
          </div>

          <!-- 메인 그리드 -->
          <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
            <!-- 왼쪽: 결과 요약 & 도넛 차트 -->
            <div class="lg:col-span-1 space-y-6">
              <!-- 결과 요약 카드 -->
              <div v-if="simulationResult" class="rounded-xl border border-border bg-card shadow-sm p-6">
                <h2 class="text-lg font-bold text-foreground mb-4">수익 결과</h2>
                <div class="space-y-4">
                  <div>
                    <div class="text-sm text-muted-foreground mb-1">최종 평가금액</div>
                    <div class="text-2xl font-bold text-primary">
                      {{ formatNumber(simulationResult.finalAmount) }}원
                    </div>
                  </div>
                  <div>
                    <div class="text-sm text-muted-foreground mb-1">총 수익금</div>
                    <div 
                      :class="[
                        'text-xl font-bold',
                        simulationResult.totalProfit >= 0 ? 'text-chart-1' : 'text-destructive'
                      ]"
                    >
                      {{ simulationResult.totalProfit >= 0 ? '+' : '' }}{{ formatNumber(simulationResult.totalProfit) }}원
                    </div>
                  </div>
                  <div>
                    <div class="text-sm text-muted-foreground mb-1">총 수익률</div>
                    <div 
                      :class="[
                        'text-xl font-bold',
                        simulationResult.totalReturnRate >= 0 ? 'text-chart-1' : 'text-destructive'
                      ]"
                    >
                      {{ simulationResult.totalReturnRate >= 0 ? '+' : '' }}{{ simulationResult.totalReturnRate.toFixed(2) }}%
                    </div>
                  </div>
                </div>
              </div>

              <!-- 자산 배분 요약 카드 -->
              <div class="rounded-xl border border-border bg-card shadow-sm p-6">
                <h2 class="text-lg font-bold text-foreground mb-4">자산 배분</h2>
                
                <!-- 도넛 차트 -->
                <div class="flex justify-center mb-6">
                  <div class="relative w-56 h-56">
                    <Doughnut :data="chartData" :options="chartOptions" />
                    <div class="absolute inset-0 flex flex-col items-center justify-center">
                      <div class="text-3xl font-bold text-foreground">{{ bookmarkStore.bookmarkedEtfs.length }}개</div>
                      <div class="text-sm text-muted-foreground">ETF</div>
                    </div>
                  </div>
                </div>

                <!-- 범례 -->
                <div class="space-y-3">
                  <div v-for="(etf, index) in bookmarkStore.bookmarkedEtfs" :key="etf.etfId" class="flex items-center justify-between">
                    <div class="flex items-center gap-2 flex-1 min-w-0">
                      <div class="w-3 h-3 rounded-full flex-shrink-0" :style="{ backgroundColor: CHART_COLORS[index % CHART_COLORS.length] }"></div>
                      <span class="text-sm text-foreground truncate">{{ etf.etfName }}</span>
                    </div>
                    <span class="text-sm font-semibold text-foreground font-mono ml-2">{{ portfolioRatios[etf.etfId] || 0 }}%</span>
                  </div>
                </div>
              </div>
            </div>

            <!-- 오른쪽: 포트폴리오 비중 설정 & 결과 테이블 -->
            <div class="lg:col-span-2 space-y-6">
              <!-- 포트폴리오 비중 설정 -->
              <div class="rounded-xl border border-border bg-card shadow-sm p-6">
                <h2 class="text-lg font-bold text-foreground mb-4">포트폴리오 비중 설정</h2>
                <p class="text-sm text-muted-foreground mb-4">각 ETF의 비중을 조정하세요. (커스텀 모드)</p>
                
                <!-- 총 비중 표시 -->
                <div class="mb-4 p-4 rounded-lg bg-muted">
                  <div class="flex items-center justify-between">
                    <span class="text-sm font-medium text-muted-foreground">총 비중</span>
                    <span 
                      :class="[
                        'text-lg font-bold font-mono',
                        totalRatio === 100 ? 'text-chart-1' : 'text-destructive'
                      ]"
                    >
                      {{ totalRatio }}%
                    </span>
                  </div>
                  <div v-if="totalRatio !== 100" class="mt-2 text-xs text-destructive">
                    ⚠️ 비중의 합이 100%가 되어야 합니다.
                  </div>
                </div>

                <!-- 비중 입력 -->
                <div class="space-y-3">
                  <div 
                    v-for="etf in bookmarkStore.bookmarkedEtfs" 
                    :key="etf.etfId"
                    class="flex items-center gap-4"
                  >
                    <div class="flex-1 min-w-0">
                      <div class="text-sm font-semibold text-foreground truncate">{{ etf.etfName }}</div>
                      <div class="text-xs text-muted-foreground font-mono">{{ etf.etfCode }}</div>
                    </div>
                    <div class="flex items-center gap-2">
                      <input
                        v-model.number="portfolioRatios[etf.etfId]"
                        type="number"
                        min="0"
                        max="100"
                        class="w-20 px-3 py-2 border border-border rounded-lg bg-background text-foreground font-mono text-center focus:outline-none focus:ring-2 focus:ring-primary"
                        @input="updateRatio(etf.etfId)"
                      />
                      <span class="text-sm text-muted-foreground">%</span>
                    </div>
                  </div>
                </div>

                <!-- 균등 배분 버튼 -->
                <div class="mt-4">
                  <button
                    @click="setEqualRatios"
                    class="w-full px-4 py-2 border border-border rounded-lg text-sm font-medium text-foreground hover:bg-accent transition-colors"
                  >
                    균등 배분 ({{ Math.floor(100 / bookmarkStore.bookmarkedEtfs.length) }}% 씩)
                  </button>
                </div>
              </div>

              <!-- ETF별 수익 결과 테이블 -->
              <div v-if="simulationResult" class="rounded-xl border border-border bg-card shadow-sm p-6">
                <h2 class="text-lg font-bold text-foreground mb-4">ETF별 수익 결과</h2>
                
                <!-- 테이블 -->
                <div class="overflow-x-auto">
                  <table class="w-full">
                    <thead>
                      <tr class="border-b border-border">
                        <th class="text-left py-3 px-4 text-sm font-semibold text-muted-foreground">종목명</th>
                        <th class="text-right py-3 px-4 text-sm font-semibold text-muted-foreground">비중</th>
                        <th class="text-right py-3 px-4 text-sm font-semibold text-muted-foreground">1년 수익률</th>
                        <th class="text-right py-3 px-4 text-sm font-semibold text-muted-foreground">예상 수익금</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr 
                        v-for="detail in simulationResult.details" 
                        :key="detail.etfName"
                        class="border-b border-border hover:bg-accent transition-colors duration-200"
                      >
                        <td class="py-4 px-4">
                          <div class="font-semibold text-foreground">{{ detail.etfName }}</div>
                        </td>
                        <td class="py-4 px-4 text-right">
                          <span class="font-mono text-sm text-foreground">{{ detail.ratio }}%</span>
                        </td>
                        <td class="py-4 px-4 text-right">
                          <span 
                            :class="[
                              'font-mono text-sm font-semibold',
                              detail.returnRate1y >= 0 ? 'text-chart-1' : 'text-destructive'
                            ]"
                          >
                            {{ detail.returnRate1y >= 0 ? '+' : '' }}{{ detail.returnRate1y.toFixed(2) }}%
                          </span>
                        </td>
                        <td class="py-4 px-4 text-right">
                          <span 
                            :class="[
                              'font-mono text-sm font-semibold',
                              detail.profit >= 0 ? 'text-chart-1' : 'text-destructive'
                            ]"
                          >
                            {{ detail.profit >= 0 ? '+' : '' }}{{ formatNumber(detail.profit) }}원
                          </span>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { runSimulation } from '@/api/simulation'
import { useBookmarkStore } from '@/stores/bookmark'
import {
    ArcElement,
    Chart as ChartJS,
    Legend,
    Tooltip
} from 'chart.js'
import { computed, onMounted, reactive, ref } from 'vue'
import { Doughnut } from 'vue-chartjs'

// Chart.js 등록
ChartJS.register(ArcElement, Tooltip, Legend)

const bookmarkStore = useBookmarkStore()

// 상태
const investmentAmount = ref('10,000,000')
const portfolioRatios = reactive({})
const simulationResult = ref(null)
const isRunning = ref(false)

// 색상 팔레트
const CHART_COLORS = [
  '#2563EB', '#10B981', '#F59E0B', '#8B5CF6', '#0EA5E9', '#EC4899',
  '#14B8A6', '#F97316', '#6366F1', '#22C55E', '#A855F7', '#EAB308',
  '#06B6D4', '#F43F5E', '#84CC16'
]

// 데이터 로드
onMounted(async () => {
  if (bookmarkStore.bookmarkedEtfs.length === 0) {
    await bookmarkStore.fetchBookmarks()
  }
  
  // 초기 비중 설정 (균등 배분)
  setEqualRatios()
})

// 투자 금액 포맷팅
const formatInvestmentAmount = (event) => {
  const value = event.target.value.replace(/,/g, '')
  if (!isNaN(value) && value !== '') {
    investmentAmount.value = Number(value).toLocaleString('ko-KR')
  }
}

// 숫자 포맷팅
const formatNumber = (num) => {
  if (num === null || num === undefined) return '0'
  return num.toLocaleString('ko-KR')
}

// 총 비중 계산
const totalRatio = computed(() => {
  return Object.values(portfolioRatios).reduce((sum, ratio) => sum + (ratio || 0), 0)
})

// 시뮬레이션 실행 가능 여부
const canRunSimulation = computed(() => {
  return totalRatio.value === 100
})

// 비중 업데이트
const updateRatio = (etfId) => {
  // 0-100 범위로 제한
  if (portfolioRatios[etfId] < 0) {
    portfolioRatios[etfId] = 0
  } else if (portfolioRatios[etfId] > 100) {
    portfolioRatios[etfId] = 100
  }
}

// 균등 배분 설정
const setEqualRatios = () => {
  const count = bookmarkStore.bookmarkedEtfs.length
  if (count === 0) return
  
  const equalRatio = Math.floor(100 / count)
  const remainder = 100 - (equalRatio * count)
  
  bookmarkStore.bookmarkedEtfs.forEach((etf, index) => {
    // 나머지를 첫 번째 ETF에 추가
    portfolioRatios[etf.etfId] = index === 0 ? equalRatio + remainder : equalRatio
  })
}

// 시뮬레이션 실행
const handleRunSimulation = async () => {
  if (!canRunSimulation.value || isRunning.value) return
  
  isRunning.value = true
  simulationResult.value = null
  
  try {
    // 투자 금액 파싱
    const amount = Number(investmentAmount.value.replace(/,/g, ''))
    
    if (isNaN(amount) || amount <= 0) {
      alert('유효한 투자 금액을 입력해주세요.')
      return
    }
    
    // 포트폴리오 배열 생성
    const portfolio = bookmarkStore.bookmarkedEtfs.map(etf => ({
      etfId: etf.etfId,
      ratio: portfolioRatios[etf.etfId] || 0
    }))
    
    // API 호출
    const response = await runSimulation({
      investmentAmount: amount,
      portfolio: portfolio
    })
    
    if (response.status === 200 && response.data) {
      simulationResult.value = response.data
    } else {
      alert('시뮬레이션 실행에 실패했습니다.')
    }
  } catch (error) {
    // 401은 interceptor에서 처리됨
    if (error.response?.status === 401) {
      return
    }
    
    const errorMessage = error.response?.data || '시뮬레이션 실행 중 오류가 발생했습니다.'
    alert(errorMessage)
  } finally {
    isRunning.value = false
  }
}

// 차트 데이터
const chartData = computed(() => ({
  labels: bookmarkStore.bookmarkedEtfs.map(etf => etf.etfName),
  datasets: [{
    data: bookmarkStore.bookmarkedEtfs.map(etf => portfolioRatios[etf.etfId] || 0),
    backgroundColor: bookmarkStore.bookmarkedEtfs.map((_, index) => CHART_COLORS[index % CHART_COLORS.length]),
    borderWidth: 0,
    hoverOffset: 8
  }]
}))

// 차트 옵션
const chartOptions = {
  responsive: true,
  maintainAspectRatio: true,
  cutout: '70%',
  plugins: {
    legend: {
      display: false
    },
    tooltip: {
      backgroundColor: 'rgba(0, 0, 0, 0.8)',
      padding: 12,
      titleFont: {
        size: 14,
        weight: 'bold'
      },
      bodyFont: {
        size: 13
      },
      callbacks: {
        label: (context) => {
          const label = context.label || ''
          const value = context.parsed || 0
          return `${label}: ${value}%`
        }
      }
    }
  }
}
</script>

<style scoped>
/* Chrome, Safari, Edge, Opera */
input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
input[type="number"] {
  -moz-appearance: textfield;
}
</style>
