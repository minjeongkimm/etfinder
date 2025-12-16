<template>
  <div class="flex min-h-screen bg-background">
    <main class="flex-1 p-6 lg:p-8">
      <div class="container">
        <!-- 헤더 -->
        <header class="mb-8">
          <div class="flex items-center justify-between mb-3">
            <div class="flex items-center gap-3">
              <div class="w-10 h-10 rounded-xl bg-primary flex items-center justify-center">
                <svg class="w-6 h-6 text-primary-foreground" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-6l-2-2H5a2 2 0 00-2 2z" />
                </svg>
              </div>
              <h1 class="text-3xl font-bold tracking-tight text-foreground">나의 포트폴리오</h1>
            </div>
            <button 
              @click="handleSimulation"
              class="px-6 py-3 bg-primary text-primary-foreground rounded-lg font-semibold hover:bg-primary/90 transition-all duration-200 shadow-sm hover:shadow-md"
            >
              <span class="flex items-center gap-2">
                <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
                </svg>
                시뮬레이션
              </span>
            </button>
          </div>
          <p class="text-sm text-muted-foreground">보유 종목의 비중을 관리하고 자산 배분 전략을 수립하세요.</p>
        </header>

        <!-- 로딩 상태 -->
        <div v-if="bookmarkStore.loading" class="flex justify-center items-center py-20">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
        </div>

        <!-- 에러 상태 -->
        <div v-else-if="bookmarkStore.error" class="rounded-xl border border-destructive bg-destructive/10 p-6 text-center">
          <p class="text-destructive">{{ bookmarkStore.error }}</p>
        </div>

        <!-- 빈 상태 -->
        <div v-else-if="bookmarkStore.bookmarkedEtfs.length === 0" class="rounded-xl border border-border bg-card shadow-sm p-12 text-center">
          <div class="w-20 h-20 bg-muted rounded-full mx-auto mb-4 flex items-center justify-center">
            <svg class="w-10 h-10 text-muted-foreground" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-6l-2-2H5a2 2 0 00-2 2z" />
            </svg>
          </div>
          <h3 class="text-xl font-semibold text-foreground mb-2">포트폴리오가 비어있습니다</h3>
          <p class="text-muted-foreground mb-6">ETF 검색 페이지에서 관심있는 ETF를 포트폴리오에 추가해보세요.</p>
          <router-link 
            to="/etfs"
            class="inline-block px-6 py-3 bg-primary text-primary-foreground rounded-lg font-semibold hover:bg-primary/90 transition-all duration-200"
          >
            ETF 탐색하기 →
          </router-link>
        </div>

        <!-- 메인 콘텐츠 -->
        <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <!-- 자산 구성 요약 카드 -->
          <div class="rounded-xl border border-border bg-card shadow-sm p-6 lg:col-span-1">
            <h2 class="text-lg font-bold text-foreground mb-4">자산 구성 요약</h2>
            <p class="text-sm text-muted-foreground mb-6">현재 포트폴리오의 섹터별 비중입니다.</p>
          
            <!-- 도넛 차트 -->
            <div class="flex justify-center mb-6">
              <div class="relative w-56 h-56">
                <Doughnut :data="chartData" :options="chartOptions" />
                <div class="absolute inset-0 flex flex-col items-center justify-center">
                  <div class="text-3xl font-bold text-foreground">{{ bookmarkStore.bookmarkedEtfs.length }}개</div>
                  <div class="text-sm text-muted-foreground">보유 종목</div>
                </div>
              </div>
            </div>

            <!-- 범례 -->
            <div class="space-y-3">
              <div v-for="(item, index) in themeBreakdown" :key="index" class="flex items-center justify-between">
                <div class="flex items-center gap-2">
                  <div class="w-3 h-3 rounded-full" :style="{ backgroundColor: item.color }"></div>
                  <span class="text-sm text-foreground">{{ item.theme }}</span>
                </div>
                <span class="text-sm font-semibold text-foreground font-mono">{{ item.percentage }}%</span>
              </div>
            </div>
          </div>

          <!-- 보유 종목 관리 테이블 -->
          <div class="rounded-xl border border-border bg-card shadow-sm p-6 lg:col-span-2">
            <h2 class="text-lg font-bold text-foreground mb-4">보유 종목 관리</h2>
            <p class="text-sm text-muted-foreground mb-6">포트폴리오에 포함된 ETF의 비중을 설정하고 관리합니다.</p>
          
            <!-- 테이블 -->
            <div class="overflow-x-auto">
              <table class="w-full">
                <thead>
                  <tr class="border-b border-border">
                    <th class="text-left py-3 px-4 text-sm font-semibold text-muted-foreground">종목명</th>
                    <th class="text-left py-3 px-4 text-sm font-semibold text-muted-foreground">티커</th>
                    <th class="text-right py-3 px-4 text-sm font-semibold text-muted-foreground">현재가</th>
                    <th class="text-center py-3 px-4 text-sm font-semibold text-muted-foreground">관리</th>
                  </tr>
                </thead>
                <tbody>
                  <tr 
                    v-for="etf in bookmarkStore.bookmarkedEtfs" 
                    :key="etf.etfId"
                    class="border-b border-border hover:bg-accent transition-colors duration-200"
                  >
                    <td class="py-4 px-4">
                      <router-link 
                        :to="`/etfs/${etf.etfId}`"
                        class="font-semibold text-foreground hover:text-primary transition-colors"
                      >
                        {{ etf.etfName }}
                      </router-link>
                    </td>
                    <td class="py-4 px-4 font-mono text-sm text-muted-foreground">{{ etf.etfCode }}</td>
                    <td class="py-4 px-4 text-right">
                      <div class="font-mono font-semibold text-foreground">
                        {{ formatPrice(etf.currentPrice) }}원
                      </div>
                      <div 
                        v-if="etf.return1mo !== null && etf.return1mo !== undefined" 
                        :class="[
                          'text-xs font-mono',
                          etf.return1mo >= 0 ? 'text-chart-1' : 'text-destructive'
                        ]"
                      >
                        {{ etf.return1mo >= 0 ? '↑' : '↓' }} {{ Math.abs(etf.return1mo).toFixed(1) }}%
                      </div>
                    </td>
                    <td class="py-4 px-4 text-center">
                      <button 
                        @click="handleRemove(etf.etfId)"
                        class="p-2 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-lg transition-all duration-200"
                        title="포트폴리오에서 제거"
                      >
                        <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                        </svg>
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { useBookmarkStore } from '@/stores/bookmark'
import {
  ArcElement,
  Chart as ChartJS,
  Legend,
  Tooltip
} from 'chart.js'
import { computed, onMounted } from 'vue'
import { Doughnut } from 'vue-chartjs'
import { useRouter } from 'vue-router'

// Chart.js 등록
ChartJS.register(ArcElement, Tooltip, Legend)

const router = useRouter()
const bookmarkStore = useBookmarkStore()

// 데이터 로드
onMounted(() => {
  bookmarkStore.fetchBookmarks()
})

// 프로페셔널 fintech 색상 팔레트 (인덱스 기반)
const CHART_COLORS = [
  '#2563EB', // Primary Blue
  '#10B981', // Emerald Green
  '#F59E0B', // Amber
  '#8B5CF6', // Violet
  '#0EA5E9', // Sky Blue
  '#EC4899', // Pink
  '#14B8A6', // Teal
  '#F97316', // Orange
  '#6366F1', // Indigo
  '#22C55E', // Green
  '#A855F7', // Purple
  '#EAB308', // Yellow
  '#06B6D4', // Cyan
  '#F43F5E', // Rose
  '#84CC16', // Lime
]

// 테마별 비중 계산
const themeBreakdown = computed(() => {
  const etfs = bookmarkStore.bookmarkedEtfs
  if (etfs.length === 0) return []

  // 테마별 집계
  const themeCount = {}
  etfs.forEach(etf => {
    const theme = etf.theme || '기타'
    themeCount[theme] = (themeCount[theme] || 0) + 1
  })

  // 비율 계산 및 인덱스 기반 색상 할당
  const total = etfs.length
  return Object.entries(themeCount).map(([theme, count], index) => ({
    theme,
    count,
    percentage: Math.round((count / total) * 100),
    color: CHART_COLORS[index % CHART_COLORS.length] // 인덱스로 순환 할당
  }))
})

// 차트 데이터
const chartData = computed(() => ({
  labels: themeBreakdown.value.map(item => item.theme),
  datasets: [{
    data: themeBreakdown.value.map(item => item.count),
    backgroundColor: themeBreakdown.value.map(item => item.color),
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
          const total = context.dataset.data.reduce((a, b) => a + b, 0)
          const percentage = Math.round((value / total) * 100)
          return `${label}: ${value}개 (${percentage}%)`
        }
      }
    }
  }
}

// 가격 포맷
const formatPrice = (price) => {
  if (!price) return '0'
  return price.toLocaleString('ko-KR')
}

// 포트폴리오에서 제거
const handleRemove = async (etfId) => {
  if (!confirm('이 ETF를 포트폴리오에서 제거하시겠습니까?')) return
  
  const result = await bookmarkStore.removeBookmark(etfId)
  if (result.success) {
    alert(result.message)
  } else {
    alert(result.message)
  }
}

// 시뮬레이션 페이지로 이동
const handleSimulation = () => {
  router.push('/simulation')
}
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>
