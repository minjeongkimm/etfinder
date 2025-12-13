<script setup>
import { getRecommendedEtfs } from '@/api/etf'
import { useAuthStore } from '@/stores/auth'
import { Loader2 } from 'lucide-vue-next'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const authStore = useAuthStore()

// ==============================
// State
// ==============================

const etfList = ref([])
const loading = ref(false)
const error = ref(null)

// ==============================
// Methods
// ==============================

const fetchRecommendedEtfs = async () => {
  loading.value = true
  error.value = null

  try {
    const response = await getRecommendedEtfs()
    etfList.value = response.data
    console.log('추천 ETF 조회 성공:', etfList.value)
  } catch (err) {
    console.error('추천 ETF 조회 실패:', err)
    error.value = err.response?.data?.message || '추천 ETF를 불러오는데 실패했습니다.'
  } finally {
    loading.value = false
  }
}

const goToDetail = (etfId) => {
  router.push({ name: 'etfDetail', params: { etfId } })
}

const formatPrice = (price) => {
  if (!price) return '0'
  return price.toLocaleString('ko-KR')
}

const formatAum = (aum) => {
  if (!aum) return '0억'
  if (aum >= 10000) {
    return `${(aum / 10000).toFixed(1)}조`
  }
  return `${aum.toLocaleString()}억`
}

const getRiskBadgeColor = (risk) => {
  const colors = {
    '매우 낮음': 'bg-blue-500/10 text-blue-700 dark:text-blue-400',
    '낮음': 'bg-green-500/10 text-green-700 dark:text-green-400',
    '보통': 'bg-yellow-500/10 text-yellow-700 dark:text-yellow-400',
    '높음': 'bg-orange-500/10 text-orange-700 dark:text-orange-400',
    '매우 높음': 'bg-red-500/10 text-red-700 dark:text-red-400'
  }
  return colors[risk] || 'bg-gray-500/10 text-gray-700 dark:text-gray-400'
}

const formatScore = (score) => {
  if (score === null || score === undefined) return '-'
  return Number(score).toFixed(2)
}

// ==============================
// Lifecycle
// ==============================

onMounted(() => {
  fetchRecommendedEtfs()
})
</script>

<template>
  <div class="flex min-h-screen bg-background">
    <!-- 메인 컨텐츠 영역 -->
    <main class="flex-1 p-6 lg:p-8">
      <div class="container">
        <!-- 헤더 섹션 -->
        <header class="mb-8">
          <div class="flex items-center justify-between mb-3">
            <div>
              <h1 class="text-3xl font-bold tracking-tight text-foreground">
                추천 ETF
              </h1>
              <p class="text-sm text-muted-foreground mt-2">
                회원님의 투자 성향에 맞는 ETF를 추천해드립니다.
              </p>
            </div>
            <button
              @click="$router.push({ name: 'propensityTest' })"
              class="px-4 py-2 text-sm font-medium rounded-lg border border-input bg-background hover:bg-accent hover:text-accent-foreground transition-all duration-200"
            >
              다시 분석하기
            </button>
          </div>
        </header>

        <!-- 로딩 상태 -->
        <div v-if="loading" class="flex flex-col items-center justify-center py-16">
          <Loader2 class="w-12 h-12 text-primary animate-spin mb-4" />
          <p class="text-muted-foreground">추천 ETF를 불러오는 중...</p>
        </div>

        <!-- 에러 상태 -->
        <div v-else-if="error" class="rounded-xl border border-destructive/30 bg-destructive/10 p-6 text-center">
          <p class="text-destructive font-medium mb-2">{{ error }}</p>
          <button
            @click="fetchRecommendedEtfs"
            class="mt-4 px-4 py-2 text-sm font-medium rounded-lg bg-primary text-primary-foreground hover:bg-primary/90 transition-all duration-200"
          >
            다시 시도
          </button>
        </div>

        <!-- ETF 목록 -->
        <div v-else>
          <!-- 결과 개수 -->
          <div class="mb-4">
            <p class="text-sm text-muted-foreground">
              총 <span class="font-semibold text-foreground">{{ etfList.length }}</span>개의 ETF가 추천되었습니다
            </p>
          </div>

          <!-- ETF 카드 Grid -->
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <article
              v-for="etf in etfList"
              :key="etf.etfId"
              @click="goToDetail(etf.etfId)"
              class="rounded-xl border border-border bg-card shadow-sm hover:shadow-lg hover:border-primary/30 transition-all duration-200 cursor-pointer overflow-hidden group"
            >
              <!-- 카드 헤더 -->
              <header class="px-5 pt-5 pb-3 border-b border-border bg-gradient-to-br from-card to-primary/5">
                <div class="flex items-start justify-between mb-3">
                  <div class="flex items-center gap-2">
                    <span class="px-2.5 py-1 text-xs font-semibold rounded-md bg-primary/10 text-primary border border-primary/20">
                      {{ etf.market }}
                    </span>
                    <span 
                      v-if="etf.riskRating"
                      :class="[
                        'px-2.5 py-1 text-xs font-medium rounded-md',
                        getRiskBadgeColor(etf.riskRating)
                      ]"
                    >
                      {{ etf.riskRating }}
                    </span>
                  </div>
                  <div v-if="etf.score" class="flex flex-col items-end">
                    <span class="text-xs text-muted-foreground">추천도</span>
                    <span class="text-lg font-bold text-primary">{{ formatScore(etf.score) }}점</span>
                  </div>
                </div>
                
                <h3 class="text-lg font-bold text-foreground mb-1 group-hover:text-primary transition-colors">
                  {{ etf.etfName }}
                </h3>
                <p class="text-sm font-mono text-muted-foreground">{{ etf.etfCode }}</p>
                
                <p v-if="etf.theme" class="text-xs text-muted-foreground mt-2 line-clamp-1">
                  {{ etf.theme }}
                </p>
              </header>

              <!-- 카드 본문 -->
              <section class="px-5 py-4">
                <div class="space-y-3">
                  <!-- 주요 지표 -->
                  <div class="grid grid-cols-2 gap-3">
                    <div class="bg-muted/50 rounded-lg p-3">
                      <div class="text-xs text-muted-foreground mb-1">수수료</div>
                      <div class="text-base font-bold text-foreground">{{ etf.fee }}%</div>
                    </div>
                    <div class="bg-muted/50 rounded-lg p-3">
                      <div class="text-xs text-muted-foreground mb-1">순자산</div>
                      <div class="text-base font-bold text-foreground">{{ formatAum(etf.aum) }}</div>
                    </div>
                  </div>

                  <!-- 1년 수익률 -->
                  <div v-if="etf.return1yr !== null && etf.return1yr !== undefined" class="flex items-center justify-between pt-2 border-t border-border">
                    <span class="text-xs text-muted-foreground">1년 수익률</span>
                    <span
                      :class="[
                        'text-sm font-bold font-mono',
                        etf.return1yr > 0 ? 'text-chart-1' : etf.return1yr < 0 ? 'text-destructive' : 'text-muted-foreground'
                      ]"
                    >
                      {{ etf.return1yr > 0 ? '+' : '' }}{{ etf.return1yr }}%
                      <span class="ml-1">
                        {{ etf.return1yr > 0 ? '↗' : etf.return1yr < 0 ? '↘' : '→' }}
                      </span>
                    </span>
                  </div>
                </div>
              </section>

              <!-- 카드 푸터 -->
              <footer class="px-5 py-3 bg-muted/30 border-t border-border">
                <button
                  @click.stop="goToDetail(etf.etfId)"
                  class="w-full text-xs font-medium text-primary hover:text-primary/80 transition-colors flex items-center justify-center gap-1 group-hover:gap-2"
                >
                  <span>자세히 보기</span>
                  <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="transition-transform">
                    <path d="m9 18 6-6-6-6"/>
                  </svg>
                </button>
              </footer>
            </article>
          </div>

          <!-- 빈 상태 -->
          <div v-if="etfList.length === 0" class="text-center py-16">
            <div class="mb-4">
              <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" class="mx-auto text-muted-foreground/50">
                <path d="M3 3v18h18"/>
                <path d="m19 9-5 5-4-4-3 3"/>
              </svg>
            </div>
            <p class="text-lg font-medium text-foreground mb-2">추천 가능한 ETF가 없습니다</p>
            <p class="text-sm text-muted-foreground mb-6">투자 성향 테스트를 먼저 진행해주세요</p>
            <button
              @click="$router.push({ name: 'propensityTest' })"
              class="px-6 py-3 rounded-lg bg-primary text-primary-foreground hover:bg-primary/90 font-semibold transition-all duration-200 shadow-sm"
            >
              투자 성향 분석하기
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
.line-clamp-1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
