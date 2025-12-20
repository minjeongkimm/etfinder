<template>
  <div class="flex min-h-screen bg-background">
    <!-- 메인 컨텐츠 영역 -->
    <main class="flex-1 p-6 lg:p-8">
      <div class="container">
        <!-- 헤더 섹션 -->
        <header class="mb-8">
          <div class="flex items-center justify-between mb-3">
            <h1 class="text-3xl font-bold tracking-tight text-foreground">
              ETF 탐색
            </h1>
            <div class="flex items-center gap-2">
              <button
                :class="[
                  'px-3 py-2 text-sm font-medium rounded-lg transition-all duration-200',
                  viewMode === 'grid'
                    ? 'bg-primary text-primary-foreground'
                    : 'bg-background border border-input hover:bg-accent hover:text-accent-foreground'
                ]"
                @click="viewMode = 'grid'"
              >
                Grid
              </button>
              <button
                :class="[
                  'px-3 py-2 text-sm font-medium rounded-lg transition-all duration-200',
                  viewMode === 'list'
                    ? 'bg-primary text-primary-foreground'
                    : 'bg-background border border-input hover:bg-accent hover:text-accent-foreground'
                ]"
                @click="viewMode = 'list'"
              >
                List
              </button>
            </div>
          </div>
          <p class="text-sm text-muted-foreground">
            다양한 조건으로 원하는 ETF를 찾아보세요.
          </p>
        </header>

        <!-- 검색 및 필터 카드 -->
        <div class="rounded-xl border border-border bg-card shadow-sm mb-6">
          <div class="p-6">
            <!-- 검색바 -->
            <div class="flex gap-3 mb-4">
              <div class="flex-1 relative">
                <input
                  v-model="searchParams.keyword"
                  type="text"
                  placeholder="ETF 이름, 티커, 운용사 검색..."
                  @keyup.enter="handleSearch"
                  class="w-full rounded-lg border border-input bg-background px-4 py-2.5 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring ring-offset-background transition-all duration-200"
                />
              </div>
              
              <!-- 전체 자산 드롭다운 -->
              <select
                v-model="searchParams.market"
                class="rounded-lg border border-input bg-background px-4 py-2.5 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200 min-w-[120px]"
              >
                <option value="">전체 자산</option>
                <option value="KOR">한국</option>
                <option value="USA">미국</option>
                <option value="GLOBAL">글로벌</option>
              </select>

              <!-- 테마 드롭다운 -->
              <select
                v-model="searchParams.theme"
                class="rounded-lg border border-input bg-background px-4 py-2.5 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200 min-w-[160px]"
              >
                <option value="">전체 테마</option>
                <option v-for="themeOption in THEME_OPTIONS" :key="themeOption" :value="themeOption">
                  {{ themeOption }}
                </option>
              </select>

              <!-- 상세 필터 버튼 -->
              <button
                @click="showAdvancedFilters = !showAdvancedFilters"
                class="px-4 py-2.5 rounded-lg border border-input bg-background hover:bg-accent hover:text-accent-foreground text-sm font-medium transition-all duration-200 flex items-center gap-2"
              >
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3"/></svg>
                상세 필터
              </button>

              <!-- 검색 버튼 -->
              <button
                @click="handleSearch"
                :disabled="loading"
                class="px-6 py-2.5 rounded-lg bg-primary text-primary-foreground hover:bg-primary/90 text-sm font-medium transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-2"
              >
                <svg v-if="loading" class="animate-spin h-4 w-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                <svg v-else xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
                <span>{{ loading ? '검색 중...' : '검색' }}</span>
              </button>
            </div>

            <!-- 고급 필터 (접힌 상태) -->
            <div v-if="showAdvancedFilters" class="pt-4 border-t border-border space-y-4">
              <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                <!-- 위험등급 -->
                <div>
                  <label class="block text-sm font-medium text-foreground mb-2">위험등급</label>
                  <select
                    v-model.number="searchParams.riskRating"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                  >
                    <option :value="null">전체</option>
                    <option :value="1">1등급</option>
                    <option :value="2">2등급</option>
                    <option :value="3">3등급</option>
                    <option :value="4">4등급</option>
                    <option :value="5">5등급</option>
                  </select>
                </div>

                <!-- 최소 수수료 -->
                <div>
                  <label class="block text-sm font-medium text-foreground mb-2">최소 수수료 (%)</label>
                  <input
                    v-model.number="searchParams.minFee"
                    type="number"
                    step="0.01"
                    placeholder="0.0"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                  />
                </div>

                <!-- 최대 수수료 -->
                <div>
                  <label class="block text-sm font-medium text-foreground mb-2">최대 수수료 (%)</label>
                  <input
                    v-model.number="searchParams.maxFee"
                    type="number"
                    step="0.01"
                    placeholder="1.0"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                  />
                </div>

                <!-- 정렬 기준 -->
                <div>
                  <label class="block text-sm font-medium text-foreground mb-2">정렬 기준</label>
                  <select
                    v-model="searchParams.orderBy"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                  >
                    <option value="">기본</option>
                    <option value="fee">수수료</option>
                    <option value="return1yr">1년 수익률</option>
                    <option value="aum">시가총액</option>
                  </select>
                </div>

                <!-- 정렬 방향 -->
                <div>
                  <label class="block text-sm font-medium text-foreground mb-2">정렬 방향</label>
                  <select
                    v-model="searchParams.orderDir"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                  >
                    <option value="ASC">오름차순</option>
                    <option value="DESC">내림차순</option>
                  </select>
                </div>
              </div>

              <div class="flex justify-end gap-2">
                <button
                  @click="resetFilters"
                  class="px-4 py-2 text-sm font-medium rounded-lg border border-input bg-background hover:bg-accent hover:text-accent-foreground transition-all duration-200"
                >
                  초기화
                </button>
                <button
                  @click="handleSearch"
                  class="px-4 py-2 text-sm font-medium rounded-lg bg-primary text-primary-foreground hover:bg-primary/90 transition-all duration-200"
                >
                  적용
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 결과 정보 및 페이지 사이즈 -->
        <div class="flex items-center justify-between mb-4">
          <div class="flex items-center gap-4">
            <!-- 등록 버튼 (관리자) -->
            <button
              v-if="authStore.isAdmin"
              @click="goToCreatePage"
              class="px-4 py-2 text-sm font-medium rounded-lg bg-primary text-primary-foreground hover:bg-primary/90 transition-all duration-200"
            >
              + ETF 등록
            </button>

            <!-- 결과 개수 -->
            <div class="text-sm text-muted-foreground">
              전체 <span class="font-semibold text-foreground">{{ etfList.length }}</span>개
            </div>
          </div>

          <!-- 페이지 사이즈 드롭다운 -->
          <div class="flex items-center gap-2">
            <label class="text-sm text-muted-foreground">페이지당</label>
            <select
              v-model.number="pageSize"
              @change="handlePageSizeChange"
              class="rounded-lg border border-input bg-background px-3 py-1.5 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
            >
              <option :value="10">10개</option>
              <option :value="20">20개</option>
              <option :value="30">30개</option>
            </select>
          </div>
        </div>

        <!-- ETF 카드 Grid -->
        <div v-if="viewMode === 'grid'" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <article
            v-for="etf in paginatedEtfList"
            :key="etf.etfId"
            @click="goToDetail(etf.etfId)"
            class="rounded-xl border border-border bg-card shadow-sm hover:shadow-md transition-all duration-200 cursor-pointer"
          >
            <!-- 카드 헤더 -->
            <header class="px-4 pt-4">
              <div class="flex items-center justify-between mb-2">
                <span class="px-2 py-1 text-xs font-medium rounded bg-primary/10 text-primary">
                  {{ etf.market }}
                </span>
                <div class="flex items-center gap-1">
                  <button class="p-1 hover:bg-accent rounded transition-all duration-200">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/></svg>
                  </button>
                  <button v-if="authStore.isAdmin" @click.stop="goToEdit(etf.etfId)" class="p-1 hover:bg-accent rounded transition-all duration-200">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z"/></svg>
                  </button>
                </div>
              </div>
              <h3 class="text-base font-semibold text-foreground mb-1">
                {{ etf.etfName }}
              </h3>
              <p class="text-sm font-mono text-muted-foreground">{{ etf.etfCode }}</p>
            </header>

            <!-- 카드 본문 -->
            <section class="px-4 pb-4 pt-3">
              <div class="space-y-3">
                <!-- 현재가 -->
                <div>
                  <div class="text-xs text-muted-foreground mb-1">현재가</div>
                  <div class="flex items-end justify-between">
                    <span class="text-2xl font-mono font-bold text-foreground">
                      {{ formatPrice(etf.currentPrice) }}원
                    </span>
                    <div
                      v-if="etf.return1mo !== null"
                      :class="[
                        'flex flex-col items-end',
                        'font-mono text-sm font-medium',
                        etf.return1mo > 0 ? 'text-chart-1' : etf.return1mo < 0 ? 'text-destructive' : 'text-chart-2'
                      ]"
                    >
                      <span class="text-xs text-muted-foreground mb-0.5">1개월</span>
                      <span>
                        {{ etf.return1mo > 0 ? '↗' : etf.return1mo < 0 ? '↘' : '→' }}
                        {{ etf.return1mo > 0 ? '+' : '' }}{{ etf.return1mo }}%
                      </span>
                    </div>
                  </div>
                </div>

                <!-- 하단 정보 -->
                <div class="flex items-center justify-between text-xs text-muted-foreground border-t border-border pt-3">
                  <span>수수료 {{ etf.fee }}%</span>
                  <span>시가총액 {{ formatAum(etf.aum) }}</span>
                </div>
              </div>
            </section>
          </article>
        </div>

        <!-- ETF 리스트 List View -->
        <div v-if="viewMode === 'list'" class="rounded-xl border border-border bg-card shadow-sm divide-y divide-border">
          <article
            v-for="etf in paginatedEtfList"
            :key="etf.etfId"
            @click="goToDetail(etf.etfId)"
            class="px-6 py-4 hover:bg-accent transition-all duration-200 cursor-pointer"
          >
            <div class="flex items-center justify-between">
              <!-- 좌측: ETF 정보 -->
              <div class="flex-1">
                <div class="flex items-center gap-3 mb-1">
                  <span class="px-2 py-1 text-xs font-medium rounded bg-primary/10 text-primary">
                    {{ etf.market }}
                  </span>
                  <h3 class="text-base font-semibold text-foreground">{{ etf.etfName }}</h3>
                  <span class="text-sm font-mono text-muted-foreground">{{ etf.etfCode }}</span>
                </div>
                <div class="flex gap-4 text-xs text-muted-foreground">
                  <span>수수료 {{ etf.fee }}%</span>
                  <span>1년 수익률: 
                    <span :class="etf.return1yr > 0 ? 'text-chart-1' : etf.return1yr < 0 ? 'text-destructive' : 'text-chart-2'">
                      {{ etf.return1yr }}%
                    </span>
                  </span>
                </div>
              </div>

              <!-- 중앙: 가격 -->
              <div class="text-center px-8">
                <div class="text-xs text-muted-foreground mb-1">현재가</div>
                <div class="text-xl font-mono font-bold text-foreground">{{ formatPrice(etf.currentPrice) }}원</div>
                <div
                  v-if="etf.return1mo !== null"
                  :class="[
                    'flex items-center justify-center gap-1',
                    'text-xs font-mono font-medium',
                    etf.return1mo > 0 ? 'text-chart-1' : etf.return1mo < 0 ? 'text-destructive' : 'text-chart-2'
                  ]"
                >
                  <span class="text-muted-foreground">1개월</span>
                  <span>
                    {{ etf.return1mo > 0 ? '↗' : etf.return1mo < 0 ? '↘' : '→' }}
                    {{ etf.return1mo > 0 ? '+' : '' }}{{ etf.return1mo }}%
                  </span>
                </div>
              </div>

              <!-- 우측: 액션 버튼 -->
              <div class="flex items-center gap-2" @click.stop>
                <button class="p-2 hover:bg-background rounded-lg transition-all duration-200">
                  <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/></svg>
                </button>
                <button v-if="authStore.isAdmin" @click="goToEdit(etf.etfId)" class="px-3 py-2 text-sm font-medium rounded-lg border border-input bg-background hover:bg-accent transition-all duration-200">
                  수정
                </button>
                <button v-if="authStore.isAdmin" @click="handleDelete(etf.etfCode)" class="px-3 py-2 text-sm font-medium rounded-lg bg-destructive text-destructive-foreground hover:bg-destructive/90 transition-all duration-200">
                  삭제
                </button>
              </div>
            </div>
          </article>
        </div>

        <!-- 빈 상태 -->
        <div v-if="etfList.length === 0" class="text-center py-16">
          <p class="text-muted-foreground">검색 결과가 없습니다.</p>
        </div>

        <!-- 페이지네이션 -->
        <div v-if="etfList.length > 0" class="mt-8 flex items-center justify-center gap-2">
          <!-- 이전 버튼 -->
          <button
            @click="goToPage(currentPage - 1)"
            :disabled="currentPage === 1"
            class="px-3 py-2 rounded-lg border border-input bg-background hover:bg-accent hover:text-accent-foreground text-sm font-medium transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="15 18 9 12 15 6"/></svg>
          </button>

          <!-- 페이지 번호 -->
          <div class="flex items-center gap-1">
            <button
              v-for="page in visiblePages"
              :key="page"
              @click="goToPage(page)"
              :class="[
                'min-w-[40px] px-3 py-2 rounded-lg text-sm font-medium transition-all duration-200',
                page === currentPage
                  ? 'bg-primary text-primary-foreground'
                  : 'border border-input bg-background hover:bg-accent hover:text-accent-foreground'
              ]"
            >
              {{ page }}
            </button>
          </div>

          <!-- 다음 버튼 -->
          <button
            @click="goToPage(currentPage + 1)"
            :disabled="currentPage === totalPages"
            class="px-3 py-2 rounded-lg border border-input bg-background hover:bg-accent hover:text-accent-foreground text-sm font-medium transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="9 18 15 12 9 6"/></svg>
          </button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { deleteEtf, getEtfs, searchEtfs } from '@/api/etf'
import { useAuthStore } from '@/stores/auth'
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const authStore = useAuthStore()

// 테마 옵션 상수
const THEME_OPTIONS = [
  '2차전지/전기차',
  'AI/로봇',
  'IT/테크',
  '금융',
  '기타',
  '리츠/부동산',
  '바이오/헬스',
  '반도체',
  '배당',
  '소비재/컨텐츠',
  '시장대표',
  '에너지/환경',
  '우주/방산',
  '원자재',
  '자산배분/TDF',
  '채권/금리',
  '파생/레버리지'
]

const etfList = ref([])
const viewMode = ref('grid')
const showAdvancedFilters = ref(false)
const loading = ref(false)

// 페이지네이션 상태
const pageSize = ref(10)
const currentPage = ref(1)

const searchParams = ref({
  keyword: '',
  market: '',
  theme: '',
  minFee: null,
  maxFee: null,
  minAum: null,
  maxAum: null,
  riskRating: null,
  orderBy: '',
  orderDir: 'ASC'
})

// 총 페이지 수 계산
const totalPages = computed(() => {
  return Math.ceil(etfList.value.length / pageSize.value)
})

// 현재 페이지에 표시할 ETF 목록
const paginatedEtfList = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return etfList.value.slice(start, end)
})

// 표시할 페이지 번호 목록 (최대 7개)
const visiblePages = computed(() => {
  const total = totalPages.value
  const current = currentPage.value
  const pages = []

  if (total <= 7) {
    // 총 페이지가 7개 이하면 모두 표시
    for (let i = 1; i <= total; i++) {
      pages.push(i)
    }
  } else {
    // 총 페이지가 7개 초과
    if (current <= 4) {
      // 현재 페이지가 앞쪽
      for (let i = 1; i <= 5; i++) {
        pages.push(i)
      }
      pages.push('...')
      pages.push(total)
    } else if (current >= total - 3) {
      // 현재 페이지가 뒤쪽
      pages.push(1)
      pages.push('...')
      for (let i = total - 4; i <= total; i++) {
        pages.push(i)
      }
    } else {
      // 현재 페이지가 중간
      pages.push(1)
      pages.push('...')
      for (let i = current - 1; i <= current + 1; i++) {
        pages.push(i)
      }
      pages.push('...')
      pages.push(total)
    }
  }

  return pages.filter(p => p !== '...' || pages.indexOf(p) === pages.lastIndexOf(p))
})

// etfList가 변경되면 첫 페이지로 이동
watch(() => etfList.value.length, () => {
  currentPage.value = 1
})

const formatPrice = (price) => {
  if (!price) return '0'
  return price.toLocaleString('ko-KR')
}

const formatAum = (aum) => {
  if (!aum) return '-'
  
  // 조 단위 계산 (1조 = 1,000,000,000,000원 = 10,000억원)
  const trillion = Math.floor(aum / 1000000000000)
  const billion = Math.round((aum % 1000000000000) / 100000000)
  
  if (trillion > 0 && billion > 0) {
    return `${trillion.toLocaleString('ko-KR')}조 ${billion.toLocaleString('ko-KR')}억원`
  } else if (trillion > 0) {
    return `${trillion.toLocaleString('ko-KR')}조원`
  } else if (billion > 0) {
    return `${billion.toLocaleString('ko-KR')}억원`
  } else {
    return `${aum.toLocaleString('ko-KR')}원`
  }
}

const handleSearch = async () => {
  try {
    loading.value = true
    
    const params = {}
    Object.keys(searchParams.value).forEach(key => {
      const value = searchParams.value[key]
      if (value !== null && value !== '') {
        if (key === 'minAum' || key === 'maxAum') {
          params[key] = value * 100000000
        } else {
          params[key] = value
        }
      }
    })

    const response = Object.keys(params).length > 0 
      ? await searchEtfs(params)
      : await getEtfs()
    
    etfList.value = response.data
  } catch (error) {
    console.error('검색 오류:', error)
    alert('검색 중 오류가 발생했습니다.')
  } finally {
    loading.value = false
  }
}

const resetFilters = () => {
  searchParams.value = {
    keyword: '',
    market: '',
    theme: '',
    minFee: null,
    maxFee: null,
    minAum: null,
    maxAum: null,
    riskRating: null,
    orderBy: '',
    orderDir: 'ASC'
  }
  handleSearch()
}

const goToDetail = (etfId) => {
  router.push(`/etfs/${etfId}`)
}

const goToCreatePage = () => {
  router.push('/etfs/new')
}

const goToEdit = (etfId) => {
  router.push(`/etfs/${etfId}/edit`)
}

const handleDelete = async (etfCode) => {
  if (!confirm('정말 이 ETF를 삭제하시겠습니까?')) return

  try {
    await deleteEtf(etfCode)
    alert('삭제되었습니다.')
    handleSearch()
  } catch (error) {
    console.error('삭제 오류:', error)
    alert('삭제에 실패했습니다.')
  }
}

// 페이지 이동
const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return
  currentPage.value = page
  // 페이지 최상단으로 스크롤
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 페이지 사이즈 변경
const handlePageSizeChange = () => {
  currentPage.value = 1
}

onMounted(async () => {
  // 관리자 권한 확인을 위해 로그인 상태이면 사용자 정보 로드
  if (authStore.isAuthenticated && !authStore.user) {
    try {
      await authStore.getMyInfo()
    } catch (err) {
      console.error('사용자 정보 조회 실패:', err)
    }
  }
  
  handleSearch()
})
</script>

<style scoped>
/* Tailwind로 처리 */
</style>
