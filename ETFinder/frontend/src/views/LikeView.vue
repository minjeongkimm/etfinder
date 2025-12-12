<template>
  <div class="flex min-h-screen bg-background">
    <main class="flex-1 p-6 lg:p-8">
      <!-- 중앙 정렬 컨테이너 -->
      <div class="max-w-7xl mx-auto">
        <!-- 헤더 -->
        <header class="mb-8">
          <div class="flex items-center justify-between mb-4">
            <div>
              <div class="flex items-center gap-3 mb-2">
                <div class="w-10 h-10 rounded-xl bg-destructive flex items-center justify-center">
                  <svg class="w-6 h-6 text-destructive-foreground" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                  </svg>
                </div>
                <h1 class="text-3xl font-bold tracking-tight text-foreground">찜한 ETF</h1>
              </div>
              <p class="text-sm text-muted-foreground">관심 있게 지켜보는 ETF 목록입니다.</p>
            </div>
            <!-- 찜 개수 표시 -->
            <div v-if="!likeStore.loading && likeStore.likedEtfs.length > 0" class="px-4 py-2 rounded-lg bg-muted">
              <span class="text-sm font-medium text-muted-foreground">총 </span>
              <span class="text-lg font-bold text-foreground">{{ likeStore.likedEtfs.length }}</span>
              <span class="text-sm font-medium text-muted-foreground">개</span>
            </div>
          </div>
        </header>

        <!-- 로딩 상태 (스켈레톤) -->
        <div v-if="likeStore.loading">
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <div v-for="i in 6" :key="i" class="rounded-xl border border-border bg-card shadow-sm p-6 h-80 animate-pulse">
              <div class="flex justify-between items-start mb-4">
                <div class="flex-1">
                  <div class="h-6 bg-muted rounded w-3/4 mb-2"></div>
                  <div class="h-4 bg-muted rounded w-1/2"></div>
                </div>
                <div class="h-6 w-16 bg-muted rounded-full"></div>
              </div>
              <div class="space-y-3 mb-6">
                <div class="h-8 bg-muted rounded w-2/3"></div>
                <div class="h-5 bg-muted rounded w-1/3"></div>
              </div>
              <div class="mt-auto pt-4">
                <div class="h-10 bg-muted rounded"></div>
              </div>
            </div>
          </div>
        </div>

        <!-- 에러 상태 -->
        <div v-else-if="likeStore.error" class="rounded-xl border border-destructive bg-destructive/10 p-6 text-center">
          <p class="text-destructive">{{ likeStore.error }}</p>
        </div>

        <!-- 빈 상태 -->
        <div v-else-if="likeStore.likedEtfs.length === 0">
          <div class="rounded-xl border border-border bg-card shadow-sm p-12 text-center max-w-md mx-auto">
            <div class="w-20 h-20 bg-destructive/10 rounded-full mx-auto mb-6 flex items-center justify-center">
              <svg class="w-10 h-10 text-destructive" fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-foreground mb-2">찜한 ETF가 없습니다</h3>
            <p class="text-sm text-muted-foreground mb-8">
              ETF 탐색 페이지에서 마음에 드는 ETF를 찜해보세요.<br />
              찜한 ETF의 변동을 쉽게 추적할 수 있습니다.
            </p>
            <router-link 
              to="/etfs"
              class="inline-flex items-center gap-2 px-6 py-3 bg-primary text-primary-foreground rounded-lg font-semibold hover:bg-primary/90 transition-all duration-200 shadow-sm hover:shadow-md"
            >
              <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
              ETF 탐색하기
            </router-link>
          </div>
        </div>

        <!-- ETF 카드 그리드 -->
        <div v-else>
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <!-- ETF 카드 -->
            <div 
              v-for="etf in likeStore.likedEtfs" 
              :key="etf.etfId"
              class="group rounded-xl border border-border bg-card shadow-sm hover:shadow-lg transition-all duration-200 flex flex-col h-full overflow-hidden"
            >
              <!-- 카드 헤더 -->
              <div class="p-6 flex-1 flex flex-col">
                <!-- 상단: 뱃지와 여백 -->
                <div class="flex justify-end mb-3">
                  <span class="px-3 py-1 bg-muted rounded-full text-xs font-semibold text-muted-foreground">
                    {{ etf.market || '해외주식' }}
                  </span>
                </div>

                <!-- ETF 정보 -->
                <div class="mb-4 flex-1">
                  <!-- 제목 (2줄 제한) -->
                  <h3 class="text-lg font-bold text-foreground mb-2 line-clamp-2 leading-snug min-h-[3.5rem]">
                    {{ etf.etfName }}
                  </h3>
                  <!-- 티커 -->
                  <p class="text-sm font-mono text-muted-foreground">{{ etf.etfCode }}</p>
                </div>

                <!-- 가격 및 수익률 -->
                <div class="mb-6">
                  <div class="text-2xl font-bold text-foreground font-mono mb-1">
                    {{ formatPrice(etf.currentPrice) }}<span class="text-base text-muted-foreground ml-1">원</span>
                  </div>
                  <div 
                    v-if="etf.return1mo !== null && etf.return1mo !== undefined"
                    :class="[
                      'inline-flex items-center gap-1 text-sm font-semibold font-mono px-2 py-0.5 rounded',
                      etf.return1mo >= 0 
                        ? 'text-chart-1 bg-chart-1/10' 
                        : 'text-destructive bg-destructive/10'
                    ]"
                  >
                    <span>{{ etf.return1mo >= 0 ? '↑' : '↓' }}</span>
                    <span>{{ Math.abs(etf.return1mo).toFixed(1) }}%</span>
                  </div>
                  <span v-else class="text-sm text-muted-foreground">수익률 정보 없음</span>
                </div>
              </div>

              <!-- 카드 하단: 고정된 액션 버튼 영역 -->
              <div class="p-6 pt-0 mt-auto">
                <div class="flex gap-2">
                  <!-- 상세보기 버튼 (전체 너비) -->
                  <router-link 
                    :to="`/etfs/${etf.etfId}`"
                    class="flex-1 py-2.5 px-4 bg-primary text-primary-foreground text-center rounded-lg font-semibold hover:bg-primary/90 transition-all duration-200 text-sm group-hover:shadow-md"
                  >
                    상세보기
                  </router-link>
                  <!-- 찜 해제 버튼 -->
                  <button 
                    @click="handleUnlike(etf.etfId)"
                    class="p-2.5 text-destructive hover:bg-destructive/10 rounded-lg transition-all duration-200 border border-destructive/20"
                    title="찜 해제"
                    aria-label="찜 해제"
                  >
                    <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- ETF 더 추가하기 CTA -->
          <div class="mt-12 text-center">
            <router-link 
              to="/etfs"
              class="inline-flex items-center gap-2 px-6 py-3 border-2 border-primary text-primary rounded-lg font-semibold hover:bg-primary hover:text-primary-foreground transition-all duration-200 shadow-sm hover:shadow-md"
            >
              <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
              </svg>
              더 많은 ETF 탐색하기
            </router-link>
          </div>
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
