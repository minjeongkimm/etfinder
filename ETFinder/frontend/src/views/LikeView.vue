<template>
  <div class="flex min-h-screen bg-background">
    <main class="flex-1 p-6 lg:p-8">
      <div class="container">
        <!-- 헤더 -->
        <header class="mb-8">
          <div class="flex items-center gap-3 mb-3">
            <div class="w-10 h-10 rounded-xl bg-destructive flex items-center justify-center">
              <svg class="w-6 h-6 text-destructive-foreground" fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
              </svg>
            </div>
            <h1 class="text-3xl font-bold tracking-tight text-foreground">찜한 ETF</h1>
          </div>
          <p class="text-sm text-muted-foreground">관심 있게 지켜보는 ETF 목록입니다.</p>
        </header>

        <!-- 로딩 상태 -->
        <div v-if="likeStore.loading" class="flex justify-center items-center py-20">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
        </div>

        <!-- 에러 상태 -->
        <div v-else-if="likeStore.error" class="rounded-xl border border-destructive bg-destructive/10 p-6 text-center">
          <p class="text-destructive">{{ likeStore.error }}</p>
        </div>

        <!-- 빈 상태 -->
        <div v-else-if="likeStore.likedEtfs.length === 0" class="rounded-xl border border-border bg-card shadow-sm p-12 text-center">
          <div class="w-20 h-20 bg-destructive/10 rounded-full mx-auto mb-4 flex items-center justify-center">
            <svg class="w-10 h-10 text-destructive" fill="currentColor" viewBox="0 0 24 24">
              <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
            </svg>
          </div>
          <h3 class="text-xl font-semibold text-foreground mb-2">찜한 ETF가 없습니다</h3>
          <p class="text-muted-foreground mb-6">ETF 검색 페이지에서 관심있는 ETF를 찜해보세요.</p>
          <router-link 
            to="/etfs"
            class="inline-block px-6 py-3 bg-primary text-primary-foreground rounded-lg font-semibold hover:bg-primary/90 transition-all duration-200"
          >
            ETF 탐색하기 →
          </router-link>
        </div>

        <!-- ETF 카드 그리드 -->
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          <div 
            v-for="etf in likeStore.likedEtfs" 
            :key="etf.etfId"
            class="rounded-xl border border-border bg-card shadow-sm p-6 hover:shadow-md transition-all duration-200 relative"
          >
            <!-- 뱃지 -->
            <div class="absolute top-4 right-4 px-3 py-1 bg-muted rounded-full text-xs font-semibold text-muted-foreground">
              {{ etf.market || '해외주식' }}
            </div>

            <!-- ETF 정보 -->
            <div class="mb-4">
              <h3 class="text-lg font-bold text-foreground mb-2 pr-20">
                {{ etf.etfName }}
              </h3>
              <div class="flex items-center gap-2 mb-3">
                <span class="text-sm font-mono text-muted-foreground">{{ etf.etfCode }}</span>
              </div>
            </div>

            <!-- 가격 및 수익률 -->
            <div class="mb-4">
              <div class="text-2xl font-bold text-foreground font-mono mb-1">
                {{ formatPrice(etf.currentPrice) }}원
              </div>
              <div 
                v-if="etf.return1mo !== null && etf.return1mo !== undefined"
                :class="[
                  'text-sm font-semibold font-mono',
                  etf.return1mo >= 0 ? 'text-chart-1' : 'text-destructive'
                ]"
              >
                {{ etf.return1mo >= 0 ? '↑' : '↓' }} {{ Math.abs(etf.return1mo).toFixed(1) }}%
              </div>
            </div>

            <!-- 액션 버튼 -->
            <div class="flex gap-2">
              <router-link 
                :to="`/etfs/${etf.etfId}`"
                class="flex-1 py-2 px-4 bg-primary text-primary-foreground text-center rounded-lg font-semibold hover:bg-primary/90 transition-all duration-200 text-sm"
              >
                상세보기
              </router-link>
              <button 
                @click="handleUnlike(etf.etfId)"
                class="p-2 text-destructive hover:bg-destructive/10 rounded-lg transition-all duration-200"
                title="찜 해제"
              >
                <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                </svg>
              </button>
            </div>
          </div>
        </div>

        <!-- ETF 더 추가하기 버튼 -->
        <div v-if="likeStore.likedEtfs.length > 0" class="mt-8 text-center">
          <router-link 
            to="/etfs"
            class="inline-flex items-center gap-2 px-6 py-3 border-2 border-primary text-primary rounded-lg font-semibold hover:bg-primary hover:text-primary-foreground transition-all duration-200"
          >
            <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            ETF 더 추가하기
          </router-link>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useLikeStore } from '@/stores/like'

const likeStore = useLikeStore()

// 데이터 로드
onMounted(() => {
  likeStore.fetchLikes()
})

// 가격 포맷
const formatPrice = (price) => {
  if (!price) return '0'
  return price.toLocaleString('ko-KR')
}

// 찜 해제
const handleUnlike = async (etfId) => {
  if (!confirm('이 ETF를 찜 목록에서 제거하시겠습니까?')) return
  
  const result = await likeStore.removeLike(etfId)
  if (result.success) {
    // 성공 메시지는 조용히 처리 (UX 향상)
  } else {
    alert(result.message)
  }
}
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>
