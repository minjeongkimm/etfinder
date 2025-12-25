<template>
  <div class="h-full">
    <!-- 로딩 중 -->
    <div v-if="loading" class="flex flex-col justify-center items-center h-64">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mb-2"></div>
      <div class="text-muted-foreground text-sm">구성 종목을 불러오는 중...</div>
    </div>

    <!-- 데이터 없음 (해외지수 등) -->
    <div v-else-if="!holdings || holdings.length === 0" class="flex flex-col justify-center items-center h-64 text-center bg-muted/30 rounded-xl border border-border">
      <div class="bg-background p-3 rounded-full mb-3 shadow-sm">
        <Scale :size="24" class="text-muted-foreground" />
      </div>
      <h4 class="text-base font-semibold text-foreground mb-1">구성종목 데이터 준비 중</h4>
      <p class="text-sm text-muted-foreground">
        해외지수 추종 상품 등 일부 ETF의 경우<br>
        데이터 제공이 준비 중입니다.
      </p>
    </div>

    <!-- 데이터 표시 -->
    <div v-else class="space-y-8">
      <!-- 상단: 도넛 차트 섹션 -->
      <div class="flex flex-col items-center justify-center">
        <div class="relative w-64 h-64 md:w-72 md:h-72">
           <Doughnut v-if="chartData" :data="chartData" :options="chartOptions" />
           <!-- 차트 중앙 텍스트 -->
           <div class="absolute inset-0 flex flex-col items-center justify-center pointer-events-none">
             <span class="text-xs font-semibold text-muted-foreground uppercase tracking-wider mb-1">Top 10</span>
             <span class="text-4xl font-bold text-foreground tracking-tighter">{{ top10TotalWeight }}<span class="text-lg text-muted-foreground font-normal ml-0.5">%</span></span>
           </div>
        </div>
      </div>

      <!-- 하단: Top 10 리스트 (2단 분리) -->
      <div class="bg-card rounded-xl border border-border overflow-hidden shadow-sm">
        <div class="px-5 py-4 border-b border-border bg-muted/20 flex justify-between items-center">
          <h3 class="font-bold text-foreground">Top 10 구성종목</h3>
          <span class="text-xs font-medium text-muted-foreground bg-background px-2 py-1 rounded border border-border">
            기준일: {{ updateDate }}
          </span>
        </div>
        
        <div class="p-5">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-x-12 gap-y-2">
                <!-- 왼쪽 컬럼 (1~5위) -->
                <ul class="space-y-1">
                    <li v-for="(item, index) in holdings.slice(0, 5)" :key="index" 
                        class="flex items-center justify-between p-2 rounded hover:bg-muted/30 transition-colors border-b border-border/50 last:border-0 h-10">
                        <div class="flex items-center gap-4 overflow-hidden">
                            <span class="flex-shrink-0 flex items-center justify-center w-6 text-foreground text-sm font-bold font-mono">
                                {{ index + 1 }}
                            </span>
                            <span class="text-sm font-medium text-foreground truncate" :title="item.stockName">
                                {{ item.stockName }}
                            </span>
                        </div>
                        <span class="flex-shrink-0 text-sm font-mono font-bold text-foreground ml-2">{{ item.weight }}%</span>
                    </li>
                </ul>

                <!-- 오른쪽 컬럼 (6~10위) -->
                <ul class="space-y-1 pt-2 md:pt-0 md:border-l md:border-border/50 md:pl-12">
                    <li v-for="(item, index) in holdings.slice(5, 10)" :key="index" 
                        class="flex items-center justify-between p-2 rounded hover:bg-muted/30 transition-colors border-b border-border/50 last:border-0 h-10">
                        <div class="flex items-center gap-4 overflow-hidden">
                            <span class="flex-shrink-0 flex items-center justify-center w-6 text-muted-foreground text-sm font-bold font-mono">
                                {{ index + 6 }}
                            </span>
                            <span class="text-sm font-medium text-foreground truncate" :title="item.stockName">
                                {{ item.stockName }}
                            </span>
                        </div>
                        <span class="flex-shrink-0 text-sm font-mono font-bold text-foreground ml-2">{{ item.weight }}%</span>
                    </li>
                </ul>
            </div>
        </div>

        <!-- 더보기 버튼 -->
        <div v-if="holdings.length > 10" class="p-3 bg-muted/10 border-t border-border">
          <button 
            @click="showModal = true"
            class="w-full py-3 text-sm font-semibold text-muted-foreground hover:text-primary hover:bg-background rounded-lg border border-transparent hover:border-border transition-all flex items-center justify-center gap-2 group"
          >
            <span>전체 {{ holdings.length }}개 종목 확인하기</span>
            <svg class="w-4 h-4 group-hover:translate-x-1 transition-transform" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"/></svg>
          </button>
        </div>
      </div>
    </div>

    <!-- 전체보기 모달 -->
    <Teleport to="body">
      <div v-if="showModal" class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4">
        <div class="bg-background w-full max-w-2xl max-h-[80vh] rounded-xl shadow-xl flex flex-col border border-border animate-in fade-in zoom-in-95 duration-200">
          <!-- 모달 헤더 -->
          <div class="flex items-center justify-between p-4 border-b border-border">
            <div>
              <h3 class="text-lg font-bold text-foreground">구성종목 전체보기</h3>
              <p class="text-sm text-muted-foreground">총 {{ holdings.length }}개 종목</p>
            </div>
            <button 
              @click="showModal = false"
              class="p-2 hover:bg-muted rounded-full transition-colors text-muted-foreground hover:text-foreground"
            >
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>
            </button>
          </div>
          
          <!-- 모달 본문 (스크롤) -->
          <div class="overflow-y-auto flex-1 p-0">
            <table class="w-full text-sm text-left">
              <thead class="text-xs text-muted-foreground uppercase bg-muted/50 sticky top-0 z-10 backdrop-blur-sm">
                <tr>
                  <th class="px-6 py-3 font-medium">순위</th>
                  <th class="px-6 py-3 font-medium">종목명</th>
                  <th class="px-6 py-3 font-medium">코드</th>
                  <th class="px-6 py-3 font-medium text-right">비중</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-border">
                <tr 
                  v-for="(item, index) in holdings" 
                  :key="item.holdingId"
                  class="bg-card hover:bg-muted/50 transition-colors"
                >
                  <td class="px-6 py-3 font-medium text-muted-foreground">{{ index + 1 }}</td>
                  <td class="px-6 py-3 font-medium text-foreground">{{ item.stockName }}</td>
                  <td class="px-6 py-3 text-muted-foreground font-mono">{{ item.stockCode }}</td>
                  <td class="px-6 py-3 text-right font-bold text-foreground font-mono">{{ item.weight }}%</td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- 모달 푸터 -->
          <div class="p-4 border-t border-border bg-muted/10 flex justify-end">
            <button 
              @click="showModal = false"
              class="px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 transition-colors font-medium text-sm"
            >
              닫기
            </button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, computed } from 'vue'
import { getEtfHoldings } from '@/api/etf'
import { Scale } from 'lucide-vue-next'
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend
} from 'chart.js'
import { Doughnut } from 'vue-chartjs'

// Chart.js 등록 (PortfolioView.vue 참고)
ChartJS.register(ArcElement, Tooltip, Legend)

const props = defineProps({
  etfId: {
    type: Number,
    required: true
  }
})

const holdings = ref([])
const loading = ref(true)
const showModal = ref(false)

const fetchHoldings = async () => {
  loading.value = true
  try {
    const res = await getEtfHoldings(props.etfId)
    holdings.value = res.data || []
  } catch (err) {
    console.error("Holdings fetch error:", err)
  } finally {
    loading.value = false
  }
}

// 기준일 계산 (updateAt 사용)
const updateDate = computed(() => {
  if (holdings.value && holdings.value.length > 0 && holdings.value[0].updatedAt) {
    return new Date(holdings.value[0].updatedAt).toLocaleDateString()
  }
  return '-'
})

// 차트 데이터 (Computed)
const chartData = computed(() => {
  if (!holdings.value || holdings.value.length === 0) return null

  const top10 = holdings.value.slice(0, 10)
  const others = holdings.value.slice(10)
  
  const labels = top10.map(h => h.stockName)
  const data = top10.map(h => h.weight)

  // 색상 팔레트
  // 색상 팔레트 (다양한 색상)
  const CHART_COLORS = [
    '#2563EB', '#10B981', '#F59E0B', '#8B5CF6', '#0EA5E9', 
    '#EC4899', '#14B8A6', '#F97316', '#6366F1', '#22C55E', 
    '#A855F7', '#EAB308', '#06B6D4', '#F43F5E', '#84CC16'
  ]

  const colors = []
  
  // 데이터 개수만큼 색상 할당
  for (let i = 0; i < data.length; i++) {
    colors.push(CHART_COLORS[i % CHART_COLORS.length])
  }

  // 기타 항목이 있다면 마지막 색상은 회색으로
  if (others.length > 0) {
    labels.push('기타')
    const othersWeight = others.reduce((sum, h) => sum + Number(h.weight), 0)
    data.push(othersWeight.toFixed(2))
    // 기존 colors 배열에서 마지막에 회색 추가 (기타는 회색)
    colors.push('#94a3b8') 
  }

  return {
    labels,
    datasets: [{
      data,
      backgroundColor: colors,
      borderWidth: 2,
      borderColor: '#ffffff',
      hoverOffset: 4
    }]
  }
})

// Top 10 비중 합계
const top10TotalWeight = computed(() => {
  if (!holdings.value || holdings.value.length === 0) return '0'
  const top10 = holdings.value.slice(0, 10)
  const total = top10.reduce((sum, item) => sum + Number(item.weight), 0)
  return total.toFixed(2)
})

// 차트 옵션
const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: { display: false },
    tooltip: {
      backgroundColor: 'rgba(255, 255, 255, 0.9)',
      titleColor: '#1e293b',
      bodyColor: '#475569',
      borderColor: '#e2e8f0',
      borderWidth: 1,
      padding: 10,
      callbacks: {
        label: function(context) {
          return ` ${context.label}: ${context.raw}%`
        }
      }
    }
  },
  cutout: '60%',
  animation: { animateScale: true, animateRotate: true }
}

onMounted(() => {
  fetchHoldings()
})

watch(() => props.etfId, () => {
  fetchHoldings()
})
</script>
