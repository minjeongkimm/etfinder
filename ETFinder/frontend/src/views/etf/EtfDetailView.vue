<template>
  <div class="flex min-h-screen bg-background">
    <main class="flex-1 p-6 lg:p-8">
      <div class="container">
        <!-- 로딩 -->
        <div v-if="loading" class="flex justify-center items-center h-64">
          <div class="text-muted-foreground">로딩 중...</div>
        </div>

        <!-- 에러 -->
        <div v-else-if="error" class="text-center py-12">
          <p class="text-destructive mb-4">{{ error }}</p>
          <button
            @click="$router.push('/etfs')"
            class="px-4 py-2 rounded-lg bg-primary text-primary-foreground hover:bg-primary/90 transition-all duration-200"
          >
            목록으로 돌아가기
          </button>
        </div>

        <!-- ETF 상세 -->
        <div v-else-if="etf">
          <!-- 헤더 -->
          <header class="mb-6">
            <button
              @click="$router.push('/etfs')"
              class="flex items-center gap-2 text-sm text-muted-foreground hover:text-foreground mb-4 transition-all duration-200"
            >
              <span>←</span>
              <span>목록으로 돌아가기</span>
            </button>

            <div class="flex items-start justify-between mb-3">
              <div class="flex-1">
                <div class="flex items-center gap-2 mb-2">
                  <span class="text-sm font-mono text-muted-foreground">{{ etf.etfCode }}</span>
                  <span class="px-2 py-1 text-xs font-medium rounded bg-primary/10 text-primary">
                    {{ etf.market }}
                  </span>
                </div>
                <h1 class="text-3xl font-bold tracking-tight text-foreground mb-2">
                  {{ etf.etfName }}
                </h1>
                <p v-if="etf.description" class="text-sm text-muted-foreground">
                  {{ etf.description }}
                </p>
              </div>

              <!-- 액션 버튼 -->
              <div class="flex items-center gap-2">
                <!-- 좋아요 수 표시 -->
                <div class="flex items-center gap-1 px-3 py-2 rounded-lg border border-border bg-muted/30">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="currentColor" class="text-destructive">
                    <path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z"/>
                  </svg>
                  <span class="text-sm font-medium text-foreground">{{ etf.likeCount || 0 }}</span>
                </div>

                <!-- 공유 버튼 -->
                <button 
                  @click="handleShare"
                  class="px-4 py-2 rounded-lg border border-input bg-background hover:bg-accent hover:text-accent-foreground transition-all duration-200 flex items-center gap-2"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 12v8a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-8"/><polyline points="16 6 12 2 8 6"/><line x1="12" x2="12" y1="2" y2="15"/></svg>
                  <span class="text-sm font-medium">공유</span>
                </button>

                <!-- 찜하기 버튼 -->
                <button 
                  @click="handleToggleLike"
                  :disabled="likeLoading"
                  :class="[
                    'px-4 py-2 rounded-lg border transition-all duration-200 flex items-center gap-2',
                    etf.likedByMe 
                      ? 'border-destructive bg-destructive text-destructive-foreground hover:bg-destructive/90'
                      : 'border-input bg-background hover:bg-accent hover:text-accent-foreground'
                  ]"
                >
                  <svg 
                    xmlns="http://www.w3.org/2000/svg" 
                    width="18" 
                    height="18" 
                    viewBox="0 0 24 24" 
                    :fill="etf.likedByMe ? 'currentColor' : 'none'" 
                    stroke="currentColor" 
                    stroke-width="2" 
                    stroke-linecap="round" 
                    stroke-linejoin="round"
                  >
                    <path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z"/>
                  </svg>
                  <span class="text-sm font-medium">{{ etf.likedByMe ? '찜 해제' : '찜하기' }}</span>
                </button>

                <!-- 포트폴리오 담기 버튼 -->
                <button 
                  @click="handleToggleBookmark"
                  :disabled="bookmarkLoading"
                  :class="[
                    'px-6 py-2 rounded-lg transition-all duration-200 font-medium',
                    isBookmarked
                      ? 'bg-muted border border-primary text-primary hover:bg-primary/10'
                      : 'bg-primary text-primary-foreground hover:bg-primary/90'
                  ]"
                >
                  {{ isBookmarked ? '포트폴리오에 있음' : '포트폴리오 담기' }}
                </button>
              </div>
            </div>
          </header>

          <!-- 메인 정보 카드 -->
          <div class="rounded-xl border border-border bg-card shadow-sm mb-6">
            <div class="p-6">
              <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
                <!-- 현재가 -->
                <div>
                  <div class="text-sm text-muted-foreground mb-1">현재가</div>
                  <div class="text-3xl font-mono font-bold text-foreground mb-1">
                    {{ formatPrice(etf.currentPrice) }}원
                  </div>
                  <div
                    v-if="etf.return1mo !== null"
                    :class="[
                      'text-sm font-mono font-medium flex items-center gap-1',
                      etf.return1mo > 0 ? 'text-chart-1' : etf.return1mo < 0 ? 'text-destructive' : 'text-chart-2'
                    ]"
                  >
                    <span>{{ etf.return1mo > 0 ? '↗' : etf.return1mo < 0 ? '↘' : '→' }}</span>
                    <span>{{ etf.return1mo > 0 ? '+' : '' }}{{ etf.return1mo }}% (360원)</span>
                  </div>
                </div>

                <!-- 시가총액 -->
                <div>
                  <div class="text-sm text-muted-foreground mb-1">시가총액</div>
                  <div class="text-xl font-mono font-semibold text-foreground">
                    {{ formatAumDetail(etf.aum) }}
                  </div>
                </div>

                <!-- 총보수 -->
                <div>
                  <div class="text-sm text-muted-foreground mb-1">총보수</div>
                  <div class="text-xl font-mono font-semibold text-foreground">
                    {{ etf.fee }}%
                  </div>
                </div>

                <!-- 분배금 -->
                <div>
                  <div class="text-sm text-muted-foreground mb-1">분배금</div>
                  <div class="text-xl font-mono font-semibold text-foreground">
                    분기지급
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 탭 영역 -->
          <div class="rounded-xl border border-border bg-card shadow-sm">
            <!-- 탭 헤더 -->
            <div class="border-b border-border">
              <nav class="flex gap-1 px-2" aria-label="Tabs">
                <button
                  v-for="tab in tabs"
                  :key="tab.id"
                  @click="activeTab = tab.id"
                  :class="[
                    'px-4 py-3 text-sm font-medium transition-all duration-200 rounded-t-lg',
                    activeTab === tab.id
                      ? 'text-primary border-b-2 border-primary'
                      : 'text-muted-foreground hover:text-foreground'
                  ]"
                >
                  {{ tab.label }}
                </button>
              </nav>
            </div>

            <!-- 탭 컨텐츠 -->
            <div class="p-6">
              <!-- 차트/수익률 탭 -->
              <div v-if="activeTab === 'chart'">
                <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
                  <!-- 차트 영역 -->
                  <div class="lg:col-span-2">
                    <h3 class="text-lg font-semibold text-foreground mb-4">수익률 추이</h3>
                    <div class="rounded-lg border border-border bg-muted/30 h-80 flex items-center justify-center">
                      <p class="text-muted-foreground text-sm">차트 영역 (추후 구현)</p>
                    </div>
                  </div>

                  <!-- AI 견해 -->
                  <div class="rounded-lg border border-border bg-accent/50 p-4">
                    <div class="flex items-center gap-2 mb-3">
                      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 8V4H8"/><rect width="16" height="12" x="4" y="8" rx="2"/><path d="M2 14h2"/><path d="M20 14h2"/><path d="M15 13v2"/><path d="M9 13v2"/></svg>
                      <h4 class="font-semibold text-foreground">AI 견해</h4>
                    </div>
                    <p class="text-sm text-muted-foreground mb-4">
                      이 ETF는 기술주 중심의 공격적인 투자를 선호하는 투자자에게 적합합니다. 최근 AI 산업 성장과 반의 높은 수익률 기대감이 있으며, 변동성도 다소 높은 편입니다.
                    </p>
                    <div class="space-y-2">
                      <div class="flex justify-between text-sm">
                        <span class="text-muted-foreground">변동성</span>
                        <span class="font-medium text-destructive">높음</span>
                      </div>
                      <div class="flex justify-between text-sm">
                        <span class="text-muted-foreground">성장성</span>
                        <span class="font-medium text-chart-1">매우 높음</span>
                      </div>
                      <div class="flex justify-between text-sm">
                        <span class="text-muted-foreground">배당수익</span>
                        <span class="font-medium text-chart-2">낮음</span>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 수익률 그리드 -->
                <div class="mt-6 grid grid-cols-2 md:grid-cols-5 gap-4">
                  <div v-if="etf.return1mo !== null" class="rounded-lg border border-border bg-muted/30 p-4 text-center">
                    <div class="text-sm text-muted-foreground mb-2">1개월</div>
                    <div
                      :class="[
                        'text-xl font-mono font-bold',
                        etf.return1mo >= 0 ? 'text-chart-1' : 'text-destructive'
                      ]"
                    >
                      {{ etf.return1mo }}%
                    </div>
                  </div>
                  <div v-if="etf.return3mo !== null" class="rounded-lg border border-border bg-muted/30 p-4 text-center">
                    <div class="text-sm text-muted-foreground mb-2">3개월</div>
                    <div
                      :class="[
                        'text-xl font-mono font-bold',
                        etf.return3mo >= 0 ? 'text-chart-1' : 'text-destructive'
                      ]"
                    >
                      {{ etf.return3mo }}%
                    </div>
                  </div>
                  <div v-if="etf.return6mo !== null" class="rounded-lg border border-border bg-muted/30 p-4 text-center">
                    <div class="text-sm text-muted-foreground mb-2">6개월</div>
                    <div
                      :class="[
                        'text-xl font-mono font-bold',
                        etf.return6mo >= 0 ? 'text-chart-1' : 'text-destructive'
                      ]"
                    >
                      {{ etf.return6mo }}%
                    </div>
                  </div>
                  <div v-if="etf.return1yr !== null" class="rounded-lg border border-border bg-muted/30 p-4 text-center">
                    <div class="text-sm text-muted-foreground mb-2">1년</div>
                    <div
                      :class="[
                        'text-xl font-mono font-bold',
                        etf.return1yr >= 0 ? 'text-chart-1' : 'text-destructive'
                      ]"
                    >
                      {{ etf.return1yr }}%
                    </div>
                  </div>
                  <div v-if="etf.return3yr !== null" class="rounded-lg border border-border bg-muted/30 p-4 text-center">
                    <div class="text-sm text-muted-foreground mb-2">3년</div>
                    <div
                      :class="[
                        'text-xl font-mono font-bold',
                        etf.return3yr >= 0 ? 'text-chart-1' : 'text-destructive'
                      ]"
                    >
                      {{ etf.return3yr }}%
                    </div>
                  </div>
                </div>
              </div>

              <!-- 종목 정보 탭 -->
              <div v-if="activeTab === 'info'">
                <h3 class="text-lg font-semibold text-foreground mb-4">기본 정보</h3>
                <div class="space-y-3">
                  <div class="flex justify-between py-3 border-b border-border">
                    <span class="text-sm text-muted-foreground">ETF 코드</span>
                    <span class="text-sm font-mono font-medium text-foreground">{{ etf.etfCode }}</span>
                  </div>
                  <div class="flex justify-between py-3 border-b border-border">
                    <span class="text-sm text-muted-foreground">ETF 명</span>
                    <span class="text-sm font-medium text-foreground">{{ etf.etfName }}</span>
                  </div>
                  <div class="flex justify-between py-3 border-b border-border">
                    <span class="text-sm text-muted-foreground">시장</span>
                    <span class="text-sm font-medium text-foreground">{{ etf.market }}</span>
                  </div>
                  <div class="flex justify-between py-3 border-b border-border">
                    <span class="text-sm text-muted-foreground">테마</span>
                    <span class="text-sm font-medium text-foreground">{{ etf.theme || '-' }}</span>
                  </div>
                  <div class="flex justify-between py-3 border-b border-border">
                    <span class="text-sm text-muted-foreground">위험등급</span>
                    <span class="text-sm font-medium text-foreground">{{ etf.riskRating ? `${etf.riskRating}등급` : '-' }}</span>
                  </div>
                  <div class="flex justify-between py-3 border-b border-border">
                    <span class="text-sm text-muted-foreground">총보수 (연)</span>
                    <span class="text-sm font-mono font-medium text-foreground">{{ etf.fee }}%</span>
                  </div>
                  <div class="flex justify-between py-3 border-b border-border">
                    <span class="text-sm text-muted-foreground">시가총액</span>
                    <span class="text-sm font-mono font-medium text-foreground">{{ formatAumDetail(etf.aum) }}</span>
                  </div>
                </div>
              </div>

              <!-- 구성 종목 탭 -->
              <div v-if="activeTab === 'holdings'">
                <h3 class="text-lg font-semibold text-foreground mb-4">보유 종목 비중</h3>
                <p class="text-sm text-muted-foreground text-center py-8">
                  보유 종목 정보는 준비 중입니다.
                </p>
              </div>

              <!-- 한줄평 탭 -->
              <div v-if="activeTab === 'comments'">
                <h3 class="text-lg font-semibold text-foreground mb-4">투자자 한줄평</h3>
                <div class="rounded-lg border border-border bg-muted/30 p-4 mb-4">
                  <textarea
                    placeholder="이 ETF에 대한 의견을 남겨주세요 (로그인 필요)"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring resize-none"
                    rows="3"
                  ></textarea>
                  <div class="flex justify-end mt-2">
                    <button class="px-4 py-2 text-sm font-medium rounded-lg bg-primary text-primary-foreground hover:bg-primary/90 transition-all duration-200">
                      등록하기
                    </button>
                  </div>
                </div>
                <p class="text-sm text-muted-foreground text-center py-8">
                  아직 작성된 한줄평이 없습니다.
                </p>
              </div>
            </div>
          </div>

          <!-- 관리자 액션 (하단) -->
          <div v-if="authStore.isAdmin" class="mt-6 flex justify-end gap-2">
            <button
              @click="handleEdit"
              class="px-4 py-2 text-sm font-medium rounded-lg border border-input bg-background hover:bg-accent hover:text-accent-foreground transition-all duration-200"
            >
              수정
            </button>
            <button
              @click="handleDelete"
              class="px-4 py-2 text-sm font-medium rounded-lg bg-destructive text-destructive-foreground hover:bg-destructive/90 transition-all duration-200"
            >
              삭제
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { deleteEtf, getEtfDetail } from '@/api/etf'
import { useAuthStore } from '@/stores/auth'
import { useBookmarkStore } from '@/stores/bookmark'
import { useLikeStore } from '@/stores/like'
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const bookmarkStore = useBookmarkStore()
const likeStore = useLikeStore()

const etf = ref(null)
const loading = ref(true)
const error = ref(null)
const activeTab = ref('chart')
const likeLoading = ref(false)
const bookmarkLoading = ref(false)

const tabs = [
  { id: 'chart', label: '차트/수익률' },
  { id: 'info', label: '종목 정보' },
  { id: 'holdings', label: '구성 종목' },
  { id: 'comments', label: '한줄평' }
]

// 북마크 여부 확인
const isBookmarked = computed(() => {
  return bookmarkStore.isBookmarked(etf.value?.etfId)
})

const formatPrice = (price) => {
  if (!price) return '0'
  return price.toLocaleString('ko-KR')
}

const formatAumDetail = (aum) => {
  if (!aum) return '-'
  const aukInOk = Math.round(aum / 100000000)
  return `2조 ${aukInOk.toLocaleString('ko-KR')}억원`
}

const fetchEtfDetail = async () => {
  try {
    loading.value = true
    const response = await getEtfDetail(route.params.etfId)
    etf.value = response.data
    
    // 북마크 및 좋아요 목록 로드 (로그인 상태인 경우)
    if (authStore.isAuthenticated) {
      await Promise.all([
        bookmarkStore.fetchBookmarks(),
        likeStore.fetchLikes()
      ])
      
      // likedByMe 상태 업데이트
      etf.value.likedByMe = likeStore.isLiked(etf.value.etfId)
    }
  } catch (err) {
    console.error('ETF 상세 조회 실패:', err)
    error.value = 'ETF 정보를 불러오는데 실패했습니다.'
  } finally {
    loading.value = false
  }
}

// 좋아요 토글
const handleToggleLike = async () => {
  if (!authStore.isAuthenticated) {
    alert('로그인이 필요합니다.')
    router.push('/login')
    return
  }

  likeLoading.value = true
  try {
    const result = await likeStore.toggleLike(etf.value.etfId)
    
    if (result.success) {
      // UI 즉시 업데이트
      etf.value.likedByMe = !etf.value.likedByMe
      etf.value.likeCount = etf.value.likedByMe 
        ? (etf.value.likeCount || 0) + 1 
        : Math.max((etf.value.likeCount || 0) - 1, 0)
    } else {
      alert(result.message)
    }
  } catch (err) {
    console.error('좋아요 처리 실패:', err)
    alert('좋아요 처리에 실패했습니다.')
  } finally {
    likeLoading.value = false
  }
}

// 북마크 토글
const handleToggleBookmark = async () => {
  if (!authStore.isAuthenticated) {
    alert('로그인이 필요합니다.')
    router.push('/login')
    return
  }

  bookmarkLoading.value = true
  try {
    let result
    if (isBookmarked.value) {
      result = await bookmarkStore.removeBookmark(etf.value.etfId)
    } else {
      result = await bookmarkStore.addBookmark(etf.value.etfId)
    }
    
    if (!result.success) {
      alert(result.message)
    }
  } catch (err) {
    console.error('북마크 처리 실패:', err)
    alert('포트폴리오 처리에 실패했습니다.')
  } finally {
    bookmarkLoading.value = false
  }
}

// 공유하기
const handleShare = () => {
  const url = window.location.href
  if (navigator.share) {
    navigator.share({
      title: etf.value.etfName,
      text: `${etf.value.etfName} - ETFinder에서 확인하세요`,
      url: url
    }).catch(() => {
      // 공유 취소시 무시
    })
  } else {
    // Web Share API 미지원시 클립보드에 복사
    navigator.clipboard.writeText(url).then(() => {
      alert('링크가 클립보드에 복사되었습니다.')
    })
  }
}

const handleEdit = () => {
  router.push(`/etfs/${etf.value.etfCode}/edit`)
}

const handleDelete = async () => {
  if (!confirm('정말 이 ETF를 삭제하시겠습니까?')) return

  try {
    await deleteEtf(etf.value.etfCode)
    alert('삭제되었습니다.')
    router.push('/etfs')
  } catch (err) {
    console.error('삭제 실패:', err)
    alert('삭제에 실패했습니다.')
  }
}

onMounted(() => {
  fetchEtfDetail()
})
</script>

<style scoped>
/* Tailwind로 처리 */
</style>
