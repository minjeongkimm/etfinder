<template>
  <div class="flex min-h-screen bg-background">
    <main class="flex-1 p-6 lg:p-8">
      <div class="container mx-auto max-w-7xl">
        <!-- 헤더 -->
        <div class="flex items-center justify-between mb-6">
          <div>
            <h1 class="text-2xl font-bold text-foreground flex items-center gap-2">
              🏆 모의투자 대시보드
            </h1>
            <p class="text-sm text-muted-foreground mt-1">
              가상 자금으로 실전 투자 경험을 쌓아보세요.
            </p>
          </div>
          <button 
            @click="handleReset"
            :disabled="isResetting"
            class="px-4 py-2 border border-border rounded-lg text-sm font-medium text-foreground hover:bg-accent transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {{ isResetting ? '초기화 중...' : '🔄 계좌 초기화' }}
          </button>
        </div>

        <!-- 로딩 상태 -->
        <div v-if="isLoading" class="space-y-6">
          <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div v-for="i in 3" :key="i" class="h-32 bg-muted/30 rounded-xl animate-pulse"></div>
          </div>
          <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
            <div class="lg:col-span-2 h-96 bg-muted/30 rounded-xl animate-pulse"></div>
            <div class="h-96 bg-muted/30 rounded-xl animate-pulse"></div>
          </div>
        </div>

        <!-- 에러 상태 -->
        <div v-else-if="error" class="rounded-xl border border-destructive/50 bg-destructive/10 p-8 text-center">
          <div class="text-destructive text-lg font-semibold mb-2">⚠️ 데이터 로드 실패</div>
          <p class="text-sm text-muted-foreground mb-4">{{ error }}</p>
          <button 
            @click="loadDashboard"
            class="px-4 py-2 bg-primary text-primary-foreground rounded-lg font-medium hover:bg-primary/90 transition-all"
          >
            다시 시도
          </button>
        </div>

        <!-- 대시보드 콘텐츠 -->
        <div v-else-if="dashboard" class="space-y-6">
          <!-- 자산 요약 카드 -->
          <MockDashboardSummary 
            :wallet="dashboard.walletResponse"
            :ranking="dashboard.ranking"
          />

          <!-- 중단: 차트 + 주문 폼 -->
          <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
            <!-- 자산 추이 차트 (2/3) -->
            <div class="lg:col-span-2">
              <MockAssetTrendChart :trendData="calculatedChartData" />
            </div>

            <!-- 주문하기 폼 (1/3) -->
            <div>
              <MockOrderForm 
                :balance="dashboard.walletResponse.balance"
                :holdings="dashboard.holdings"
                @order-success="handleOrderSuccess"
              />
            </div>
          </div>

          <!-- 하단: 보유 종목 + 거래 내역 -->
          <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <!-- 보유 종목 현황 -->
            <MockHoldingsList :holdings="dashboard.holdings" />

            <!-- 최근 거래 내역 -->
            <MockRecentTradesList :trades="dashboard.recentTrades" />
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { getDashboard, resetWallet } from '@/api/mock'
import MockAssetTrendChart from '@/components/mock/MockAssetTrendChart.vue'
import MockDashboardSummary from '@/components/mock/MockDashboardSummary.vue'
import MockHoldingsList from '@/components/mock/MockHoldingsList.vue'
import MockOrderForm from '@/components/mock/MockOrderForm.vue'
import MockRecentTradesList from '@/components/mock/MockRecentTradesList.vue'
import { computed, onMounted, ref } from 'vue'

const dashboard = ref(null)
const isLoading = ref(true)
const isResetting = ref(false)
const error = ref(null)

// 거래 내역 기반 실시간 차트 데이터 계산
const calculatedChartData = computed(() => {
  if (!dashboard.value) return []
  
  const { recentTrades, walletResponse, holdings } = dashboard.value
  const INITIAL_BALANCE = 10000000
  
  // 거래가 없으면 현재 자산만 표시
  if (!recentTrades || recentTrades.length === 0) {
    return [{
      baseDate: new Date().toISOString().split('T')[0],
      totalAsset: walletResponse.totalAsset,
      realizedProfit: 0
    }]
  }

  const points = []
  const sortedTrades = [...recentTrades].sort((a, b) => 
    new Date(a.createdAt) - new Date(b.createdAt)
  )

  // 첫 거래 전날 초기 상태 추가
  const firstTradeDate = new Date(sortedTrades[0].createdAt)
  const dayBefore = new Date(firstTradeDate.getTime() - 86400000)
  points.push({
    baseDate: dayBefore.toISOString().split('T')[0],
    totalAsset: INITIAL_BALANCE,
    realizedProfit: 0
  })

  let runningBalance = INITIAL_BALANCE
  const holdingsAtPoint = new Map() // etfId -> quantity

  // 각 거래 시점의 총 자산 계산 (현금 + 보유 종목 평가액)
  sortedTrades.forEach(trade => {
    // 보유 종목 수량 업데이트
    const currentQty = holdingsAtPoint.get(trade.etfId) || 0
    if (trade.tradeType === 'BUY') {
      holdingsAtPoint.set(trade.etfId, currentQty + trade.quantity)
      runningBalance -= trade.amount
    } else {
      holdingsAtPoint.set(trade.etfId, currentQty - trade.quantity)
      runningBalance += trade.amount
    }
    
    // 보유 종목 평가액 계산 (현재가 기준)
    let holdingsValue = 0
    for (const [etfId, qty] of holdingsAtPoint) {
      if (qty > 0) {
        const holding = holdings.find(h => h.etfId === etfId)
        if (holding) {
          holdingsValue += holding.currentPrice * qty
        }
      }
    }
    
    // 총 자산 = 현금 + 보유 종목 평가액
    points.push({
      baseDate: trade.createdAt.split('T')[0],
      totalAsset: runningBalance + holdingsValue,
      realizedProfit: 0
    })
  })

  // 현재 시점 추가 (잔액 + 보유 종목 평가액)
  const currentHoldingsValue = holdings.reduce((sum, h) => 
    sum + (h.currentPrice * h.quantity), 0
  )
  
  points.push({
    baseDate: new Date().toISOString().split('T')[0],
    totalAsset: walletResponse.balance + currentHoldingsValue,
    realizedProfit: 0
  })

  return points
})

// 대시보드 데이터 로드
const loadDashboard = async () => {
  isLoading.value = true
  error.value = null
  
  try {
    const data = await getDashboard()
    dashboard.value = data
  } catch (err) {
    console.error('대시보드 로드 실패:', err)
    error.value = err.response?.data || err.message || '데이터를 불러올 수 없습니다.'
  } finally {
    isLoading.value = false
  }
}

// 주문 성공 시 데이터 새로고침
const handleOrderSuccess = async () => {
  await loadDashboard()
}

// 계좌 초기화
const handleReset = async () => {
  if (!confirm('정말로 계좌를 초기화하시겠습니까? 모든 보유 종목과 거래 내역이 삭제됩니다.')) {
    return
  }

  isResetting.value = true
  try {
    await resetWallet()
    alert('계좌가 초기화되었습니다.')
    await loadDashboard()
  } catch (err) {
    console.error('계좌 초기화 실패:', err)
    alert('계좌 초기화에 실패했습니다: ' + (err.response?.data || err.message))
  } finally {
    isResetting.value = false
  }
}

// 컴포넌트 마운트 시 데이터 로드
onMounted(() => {
  loadDashboard()
})
</script>

<style scoped>
/* Tailwind로 처리 */
</style>
