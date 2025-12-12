<template>
  <div class="min-h-screen bg-gray-50 p-6">
    <div class="max-w-7xl mx-auto">
      <!-- 헤더 -->
      <div class="flex justify-between items-center mb-6">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">ETF 탐색</h1>
          <p class="text-gray-500 mt-2">다양한 조건으로 원하는 ETF를 찾아보세요.</p>
        </div>
        
        <button 
          v-if="authStore.isAdmin" 
          @click="goToCreatePage"
          class="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-medium"
        >
          + ETF 등록
        </button>
      </div>

      <!-- 검색 및 필터 영역 -->
      <div class="bg-white rounded-lg shadow p-6 mb-6">
        <div class="space-y-4">
          <!-- 키워드 검색 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">
              ETF 이름, 티커, 운용사 검색
            </label>
            <div class="flex gap-2">
              <input
                v-model="searchParams.keyword"
                type="text"
                placeholder="예: TIGER, S&P500"
                @keyup.enter="handleSearch"
                class="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
              <button
                @click="handleSearch"
                class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-medium"
              >
                검색
              </button>
            </div>
          </div>

          <!-- 고급 필터 토글 -->
          <button
            @click="showAdvancedFilters = !showAdvancedFilters"
            class="text-blue-600 hover:text-blue-700 text-sm font-medium flex items-center gap-1"
          >
            <span>{{ showAdvancedFilters ? '▼' : '▶' }}</span>
            <span>상세 필터</span>
          </button>

          <!-- 고급 필터 영역 -->
          <div v-if="showAdvancedFilters" class="space-y-4 pt-4 border-t">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <!-- 시장 선택 -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  시장
                </label>
                <select
                  v-model="searchParams.market"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                >
                  <option value="">전체</option>
                  <option value="KOR">한국</option>
                  <option value="USA">미국</option>
                  <option value="GLOBAL">글로벌</option>
                </select>
              </div>

              <!-- 테마 -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  테마
                </label>
                <input
                  v-model="searchParams.theme"
                  type="text"
                  placeholder="예: 기술주"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>

              <!-- 위험등급 -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  위험등급
                </label>
                <select
                  v-model.number="searchParams.riskRating"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                >
                  <option :value="null">전체</option>
                  <option :value="1">1등급 (매우 높은 위험)</option>
                  <option :value="2">2등급 (높은 위험)</option>
                  <option :value="3">3등급 (보통 위험)</option>
                  <option :value="4">4등급 (낮은 위험)</option>
                  <option :value="5">5등급 (매우 낮은 위험)</option>
                </select>
              </div>
            </div>

            <!-- 수수료 범위 -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  최소 수수료 (%)
                </label>
                <input
                  v-model.number="searchParams.minFee"
                  type="number"
                  step="0.01"
                  placeholder="예: 0.1"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  최대 수수료 (%)
                </label>
                <input
                  v-model.number="searchParams.maxFee"
                  type="number"
                  step="0.01"
                  placeholder="예: 1.0"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>
            </div>

            <!-- 시가총액 범위 -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  최소 시가총액 (억원)
                </label>
                <input
                  v-model.number="searchParams.minAum"
                  type="number"
                  placeholder="예: 100"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  최대 시가총액 (억원)
                </label>
                <input
                  v-model.number="searchParams.maxAum"
                  type="number"
                  placeholder="예: 10000"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>
            </div>

            <!-- 정렬 옵션 -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  정렬 기준
                </label>
                <select
                  v-model="searchParams.orderBy"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                >
                  <option value="">기본</option>
                  <option value="fee">수수료</option>
                  <option value="return1yr">1년 수익률</option>
                  <option value="aum">시가총액</option>
                  <option value="risk">위험등급</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  정렬 방향
                </label>
                <select
                  v-model="searchParams.orderDir"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                >
                  <option value="ASC">오름차순</option>
                  <option value="DESC">내림차순</option>
                </select>
              </div>
            </div>

            <!-- 필터 초기화 버튼 -->
            <div class="flex justify-end">
              <button
                @click="resetFilters"
                class="px-4 py-2 text-gray-600 hover:text-gray-900 text-sm font-medium"
              >
                필터 초기화
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 결과 영역 -->
      <div class="bg-white rounded-lg shadow">
        <!-- 결과 헤더 -->
        <div class="px-6 py-4 border-b">
          <div class="flex justify-between items-center">
            <h2 class="text-lg font-semibold text-gray-900">
              검색 결과 <span class="text-blue-600">{{ etfList.length }}</span>개
            </h2>
            <div class="flex gap-2">
              <button
                @click="viewMode = 'grid'"
                :class="[
                  'px-3 py-1 rounded',
                  viewMode === 'grid' ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-700'
                ]"
              >
                Grid
              </button>
              <button
                @click="viewMode = 'list'"
                :class="[
                  'px-3 py-1 rounded',
                  viewMode === 'list' ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-700'
                ]"
              >
                List
              </button>
            </div>
          </div>
        </div>

        <!-- 결과 목록 (Grid View) -->
        <div v-if="viewMode === 'grid'" class="p-6">
          <div v-if="etfList.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <div
              v-for="etf in etfList"
              :key="etf.etfId"
              @click="goToDetail(etf.etfId)"
              class="border rounded-lg p-4 hover:shadow-lg transition-shadow cursor-pointer"
            >
              <div class="flex items-start justify-between mb-2">
                <span class="px-2 py-1 bg-blue-100 text-blue-700 text-xs rounded">
                  {{ etf.market }}
                </span>
                <div v-if="authStore.isAdmin" class="flex gap-1" @click.stop>
                  <button
                    @click="goToEdit(etf.etfCode)"
                    class="px-2 py-1 text-xs bg-gray-200 text-gray-700 rounded hover:bg-gray-300"
                  >
                    수정
                  </button>
                  <button
                    @click="handleDelete(etf.etfCode)"
                    class="px-2 py-1 text-xs bg-red-100 text-red-700 rounded hover:bg-red-200"
                  >
                    삭제
                  </button>
                </div>
              </div>
              <h3 class="font-semibold text-gray-900 mb-1">{{ etf.etfName }}</h3>
              <p class="text-sm text-gray-500 mb-3">{{ etf.etfCode }}</p>
              <div class="flex justify-between items-center">
                <div>
                  <div class="text-sm text-gray-500">현재가</div>
                  <div class="font-semibold">{{ formatPrice(etf.currentPrice) }}원</div>
                </div>
                <div class="text-right">
                  <div class="text-sm text-gray-500">1개월</div>
                  <div
                    :class="[
                      'font-semibold',
                      etf.return1mo > 0 ? 'text-green-600' : 'text-red-600'
                    ]"
                  >
                    {{ etf.return1mo > 0 ? '+' : '' }}{{ etf.return1mo }}%
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div v-else class="text-center py-12 text-gray-500">
            검색 결과가 없습니다.
          </div>
        </div>

        <!-- 결과 목록 (List View) -->
        <div v-if="viewMode === 'list'" class="divide-y">
          <div
            v-for="etf in etfList"
            :key="etf.etfId"
            @click="goToDetail(etf.etfId)"
            class="px-6 py-4 hover:bg-gray-50 cursor-pointer transition-colors"
          >
            <div class="flex items-center justify-between">
              <!-- 왼쪽: ETF 정보 -->
              <div class="flex-1">
                <div class="flex items-center gap-2 mb-1">
                  <span class="px-2 py-1 bg-blue-100 text-blue-700 text-xs rounded">
                    {{ etf.market }}
                  </span>
                  <h3 class="font-semibold text-gray-900">{{ etf.etfName }}</h3>
                  <span class="text-sm text-gray-500">{{ etf.etfCode }}</span>
                </div>
                <div class="flex gap-4 text-sm text-gray-600">
                  <span>수수료: {{ etf.fee }}%</span>
                  <span>1년 수익률: 
                    <span :class="etf.return1yr > 0 ? 'text-green-600' : 'text-red-600'">
                      {{ etf.return1yr }}%
                    </span>
                  </span>
                </div>
              </div>

              <!-- 중앙: 가격 정보 -->
              <div class="text-center px-6">
                <div class="text-sm text-gray-500">현재가</div>
                <div class="font-semibold text-lg">{{ formatPrice(etf.currentPrice) }}원</div>
                <div
                  :class="[
                    'text-sm',
                    etf.return1mo > 0 ? 'text-green-600' : 'text-red-600'
                  ]"
                >
                  {{ etf.return1mo > 0 ? '▲' : '▼' }} {{ Math.abs(etf.return1mo) }}%
                </div>
              </div>

              <!-- 오른쪽: 관리자 버튼 -->
              <div v-if="authStore.isAdmin" class="flex gap-2" @click.stop>
                <button
                  @click="goToEdit(etf.etfCode)"
                  class="px-4 py-2 bg-gray-200 text-gray-700 rounded hover:bg-gray-300"
                >
                  수정
                </button>
                <button
                  @click="handleDelete(etf.etfCode)"
                  class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
                >
                  삭제
                </button>
              </div>
            </div>
          </div>
          <div v-if="etfList.length === 0" class="text-center py-12 text-gray-500">
            검색 결과가 없습니다.
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { getEtfs, searchEtfs, deleteEtf } from '@/api/etf'

const router = useRouter()
const authStore = useAuthStore()

const etfList = ref([])
const viewMode = ref('grid') // 'grid' or 'list'
const showAdvancedFilters = ref(false)

// 검색 파라미터
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

// 가격 포맷팅
const formatPrice = (price) => {
  if (!price) return '0'
  return price.toLocaleString('ko-KR')
}

// 검색 실행
const handleSearch = async () => {
  try {
    // null과 빈 문자열 필터링
    const params = {}
    Object.keys(searchParams.value).forEach(key => {
      const value = searchParams.value[key]
      if (value !== null && value !== '') {
        // minAum, maxAum은 억원 단위로 입력받아 원 단위로 변환
        if (key === 'minAum' || key === 'maxAum') {
          params[key] = value * 100000000
        } else {
          params[key] = value
        }
      }
    })

    // 파라미터가 있으면 검색, 없으면 전체 조회
    const response = Object.keys(params).length > 0 
      ? await searchEtfs(params)
      : await getEtfs()
    
    etfList.value = response.data
  } catch (error) {
    console.error('검색 오류:', error)
    alert('검색 중 오류가 발생했습니다.')
  }
}

// 필터 초기화
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

// 상세 페이지로 이동
const goToDetail = (etfId) => {
  router.push(`/etfs/${etfId}`)
}

// 등록 페이지로 이동
const goToCreatePage = () => {
  router.push('/etfs/new')
}

// 수정 페이지로 이동
const goToEdit = (etfCode) => {
  router.push(`/etfs/${etfCode}/edit`)
}

// 삭제 처리
const handleDelete = async (etfCode) => {
  if (!confirm('정말 이 ETF를 삭제하시겠습니까?')) return

  try {
    await deleteEtf(etfCode)
    alert('삭제되었습니다.')
    handleSearch() // 목록 새로고침
  } catch (error) {
    console.error('삭제 오류:', error)
    alert('삭제에 실패했습니다.')
  }
}

// 컴포넌트 마운트 시 전체 목록 조회
onMounted(() => {
  handleSearch()
})
</script>

<style scoped>
/* Tailwind CSS로 대부분 처리, 필요시 추가 스타일 */
</style>