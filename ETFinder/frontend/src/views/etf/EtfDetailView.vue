<template>
  <div class="min-h-screen bg-gray-50 p-6">
    <div class="max-w-6xl mx-auto">
      <!-- 로딩 상태 -->
      <div v-if="loading" class="flex justify-center items-center h-64">
        <div class="text-gray-500">로딩 중...</div>
      </div>

      <!-- 에러 상태 -->
      <div v-else-if="error" class="text-center py-12">
        <p class="text-red-500 mb-4">{{ error }}</p>
        <button @click="$router.push('/etfs')" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
          목록으로 돌아가기
        </button>
      </div>

      <!-- ETF 상세 정보 -->
      <div v-else-if="etf" class="space-y-6">
        <!-- 헤더: 뒤로가기 버튼 -->
        <div class="flex items-center justify-between">
          <button 
            @click="$router.push('/etfs')"
            class="flex items-center text-gray-600 hover:text-gray-900"
          >
            <span class="mr-2">←</span>
            <span>목록으로 돌아가기</span>
          </button>

          <!-- 관리자 액션 버튼 -->
          <div v-if="authStore.isAdmin" class="flex gap-2">
            <button 
              @click="handleEdit"
              class="px-4 py-2 bg-gray-800 text-white rounded hover:bg-gray-900"
            >
              수정
            </button>
            <button 
              @click="handleDelete"
              class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
            >
              삭제
            </button>
          </div>
        </div>

        <!-- ETF 코드 및 카테고리 -->
        <div class="flex items-center gap-2">
          <span class="text-sm text-gray-500">{{ etf.etfCode }}</span>
          <span class="px-2 py-1 bg-blue-100 text-blue-700 text-xs rounded">{{ etf.market }}</span>
        </div>

        <!-- ETF 이름 -->
        <h1 class="text-3xl font-bold text-gray-900">{{ etf.etfName }}</h1>
        
        <!-- 설명 (있는 경우) -->
        <p v-if="etf.description" class="text-gray-600">{{ etf.description }}</p>

        <!-- 메인 정보 카드 -->
        <div class="bg-white rounded-lg shadow p-6">
          <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <!-- 현재가 -->
            <div>
              <div class="text-sm text-gray-500 mb-1">현재가</div>
              <div class="text-2xl font-bold text-gray-900">
                {{ formatPrice(etf.currentPrice) }}원
              </div>
              <div 
                v-if="etf.return1mo !== null" 
                :class="['text-sm', etf.return1mo > 0 ? 'text-green-600' : 'text-red-600']"
              >
                {{ etf.return1mo > 0 ? '▲' : '▼' }} {{ Math.abs(etf.return1mo) }}%
              </div>
            </div>

            <!-- 시가총액 -->
            <div>
              <div class="text-sm text-gray-500 mb-1">시가총액</div>
              <div class="text-xl font-semibold text-gray-900">
                {{ formatAum(etf.aum) }}
              </div>
            </div>

            <!-- 수수료 -->
            <div>
              <div class="text-sm text-gray-500 mb-1">총보수</div>
              <div class="text-xl font-semibold text-gray-900">{{ etf.fee }}%</div>
            </div>
          </div>
        </div>

        <!-- 탭 영역 -->
        <div class="bg-white rounded-lg shadow">
          <!-- 탭 헤더 -->
          <div class="flex border-b">
            <button
              v-for="tab in tabs"
              :key="tab.id"
              @click="activeTab = tab.id"
              :class="[
                'px-6 py-3 font-medium transition-colors',
                activeTab === tab.id
                  ? 'text-blue-600 border-b-2 border-blue-600'
                  : 'text-gray-500 hover:text-gray-700'
              ]"
            >
              {{ tab.label }}
            </button>
          </div>

          <!-- 탭 컨텐츠 -->
          <div class="p-6">
            <!-- 차트/수익률 탭 -->
            <div v-if="activeTab === 'chart'" class="space-y-4">
              <h3 class="text-lg font-semibold mb-4">수익률 추이</h3>
              <div class="grid grid-cols-2 md:grid-cols-5 gap-4">
                <div v-if="etf.return1mo !== null" class="text-center p-4 bg-gray-50 rounded">
                  <div class="text-sm text-gray-500 mb-2">1개월</div>
                  <div :class="['text-lg font-semibold', etf.return1mo >= 0 ? 'text-green-600' : 'text-red-600']">
                    {{ etf.return1mo }}%
                  </div>
                </div>
                <div v-if="etf.return3mo !== null" class="text-center p-4 bg-gray-50 rounded">
                  <div class="text-sm text-gray-500 mb-2">3개월</div>
                  <div :class="['text-lg font-semibold', etf.return3mo >= 0 ? 'text-green-600' : 'text-red-600']">
                    {{ etf.return3mo }}%
                  </div>
                </div>
                <div v-if="etf.return6mo !== null" class="text-center p-4 bg-gray-50 rounded">
                  <div class="text-sm text-gray-500 mb-2">6개월</div>
                  <div :class="['text-lg font-semibold', etf.return6mo >= 0 ? 'text-green-600' : 'text-red-600']">
                    {{ etf.return6mo }}%
                  </div>
                </div>
                <div v-if="etf.return1yr !== null" class="text-center p-4 bg-gray-50 rounded">
                  <div class="text-sm text-gray-500 mb-2">1년</div>
                  <div :class="['text-lg font-semibold', etf.return1yr >= 0 ? 'text-green-600' : 'text-red-600']">
                    {{ etf.return1yr }}%
                  </div>
                </div>
                <div v-if="etf.return3yr !== null" class="text-center p-4 bg-gray-50 rounded">
                  <div class="text-sm text-gray-500 mb-2">3년</div>
                  <div :class="['text-lg font-semibold', etf.return3yr >= 0 ? 'text-green-600' : 'text-red-600']">
                    {{ etf.return3yr }}%
                  </div>
                </div>
              </div>
            </div>

            <!-- 종목 정보 탭 -->
            <div v-if="activeTab === 'info'" class="space-y-4">
              <h3 class="text-lg font-semibold mb-4">기본 정보</h3>
              <div class="space-y-3">
                <div class="flex justify-between py-2 border-b">
                  <span class="text-gray-600">ETF 코드</span>
                  <span class="font-medium">{{ etf.etfCode }}</span>
                </div>
                <div class="flex justify-between py-2 border-b">
                  <span class="text-gray-600">ETF 명</span>
                  <span class="font-medium">{{ etf.etfName }}</span>
                </div>
                <div class="flex justify-between py-2 border-b">
                  <span class="text-gray-600">시장</span>
                  <span class="font-medium">{{ etf.market }}</span>
                </div>
                <div class="flex justify-between py-2 border-b">
                  <span class="text-gray-600">테마</span>
                  <span class="font-medium">{{ etf.theme || '-' }}</span>
                </div>
                <div class="flex justify-between py-2 border-b">
                  <span class="text-gray-600">위험등급</span>
                  <span class="font-medium">{{ etf.riskRating ? `${etf.riskRating}등급` : '-' }}</span>
                </div>
                <div class="flex justify-between py-2 border-b">
                  <span class="text-gray-600">총보수 (연)</span>
                  <span class="font-medium">{{ etf.fee }}%</span>
                </div>
                <div class="flex justify-between py-2 border-b">
                  <span class="text-gray-600">시가총액</span>
                  <span class="font-medium">{{ formatAum(etf.aum) }}</span>
                </div>
              </div>
            </div>

            <!-- 구성 종목 탭 (추후 확장) -->
            <div v-if="activeTab === 'holdings'">
              <p class="text-gray-500 text-center py-8">보유 종목 정보는 준비 중입니다.</p>
            </div>

            <!-- 한줄평 탭 (추후 확장) -->
            <div v-if="activeTab === 'comments'">
              <p class="text-gray-500 text-center py-8">한줄평 기능은 준비 중입니다.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { getEtfDetail, deleteEtf } from '@/api/etf'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const etf = ref(null)
const loading = ref(true)
const error = ref(null)
const activeTab = ref('chart')

const tabs = [
  { id: 'chart', label: '차트/수익률' },
  { id: 'info', label: '종목 정보' },
  { id: 'holdings', label: '구성 종목' },
  { id: 'comments', label: '한줄평' }
]

// 가격 포맷팅
const formatPrice = (price) => {
  if (!price) return '0'
  return price.toLocaleString('ko-KR')
}

// AUM 포맷팅 (억 단위)
const formatAum = (aum) => {
  if (!aum) return '-'
  const aukInOk = Math.round(aum / 100000000)
  return `${aukInOk.toLocaleString('ko-KR')}억`
}

// ETF 상세 정보 가져오기
const fetchEtfDetail = async () => {
  try {
    loading.value = true
    const response = await getEtfDetail(route.params.etfId)
    etf.value = response.data
  } catch (err) {
    console.error('ETF 상세 조회 실패:', err)
    error.value = 'ETF 정보를 불러오는데 실패했습니다.'
  } finally {
    loading.value = false
  }
}

// 수정 페이지로 이동
const handleEdit = () => {
  router.push(`/etfs/${etf.value.etfCode}/edit`)
}

// 삭제 처리
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
/* 필요한 경우 추가 스타일 */
</style>
