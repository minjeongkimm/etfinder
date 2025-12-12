<template>
  <div class="min-h-screen bg-gray-50 p-6">
    <div class="max-w-4xl mx-auto">
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

      <!-- 수정 폼 -->
      <div v-else>
        <!-- 헤더 -->
        <div class="mb-6">
          <button 
            @click="goBack"
            class="flex items-center text-gray-600 hover:text-gray-900 mb-4"
          >
            <span class="mr-2">←</span>
            <span>뒤로 가기</span>
          </button>
          <h1 class="text-3xl font-bold text-gray-900">ETF 수정</h1>
          <p class="text-gray-500 mt-2">수정하지 않을 항목은 비워두시면 기존 값이 유지됩니다.</p>
        </div>

        <!-- 수정 폼 -->
        <div class="bg-white rounded-lg shadow p-6">
          <form @submit.prevent="handleSubmit" class="space-y-6">
            <!-- 기본 정보 섹션 -->
            <div>
              <h2 class="text-lg font-semibold text-gray-900 mb-4">기본 정보</h2>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <!-- ETF 코드 (읽기 전용) -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    ETF 코드
                  </label>
                  <input
                    :value="etfCode"
                    type="text"
                    disabled
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-100 text-gray-500"
                  />
                </div>

                <!-- ETF 명 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    ETF 명
                  </label>
                  <input
                    v-model="form.etfName"
                    type="text"
                    placeholder="변경하지 않으려면 비워두세요"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>

                <!-- 시장 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    시장
                  </label>
                  <select
                    v-model="form.market"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  >
                    <option value="">변경하지 않음</option>
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
                    v-model="form.theme"
                    type="text"
                    placeholder="변경하지 않으려면 비워두세요"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>

                <!-- 현재가 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    현재가 (원)
                  </label>
                  <input
                    v-model.number="form.currentPrice"
                    type="number"
                    step="1"
                    placeholder="변경하지 않으려면 비워두세요"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>

                <!-- 총보수 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    총보수 (%)
                  </label>
                  <input
                    v-model.number="form.fee"
                    type="number"
                    step="0.01"
                    placeholder="변경하지 않으려면 비워두세요"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>

                <!-- 시가총액 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    시가총액 (원)
                  </label>
                  <input
                    v-model.number="form.aum"
                    type="number"
                    step="1"
                    placeholder="변경하지 않으려면 비워두세요"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>

                <!-- 위험등급 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    위험등급 (1~5)
                  </label>
                  <select
                    v-model.number="form.riskRating"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  >
                    <option :value="null">변경하지 않음</option>
                    <option :value="1">1등급 (매우 높은 위험)</option>
                    <option :value="2">2등급 (높은 위험)</option>
                    <option :value="3">3등급 (보통 위험)</option>
                    <option :value="4">4등급 (낮은 위험)</option>
                    <option :value="5">5등급 (매우 낮은 위험)</option>
                  </select>
                </div>
              </div>
            </div>

            <!-- 수익률 정보 섹션 -->
            <div>
              <h2 class="text-lg font-semibold text-gray-900 mb-4">수익률 정보 (%)</h2>
              <div class="grid grid-cols-2 md:grid-cols-5 gap-4">
                <!-- 1개월 수익률 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    1개월
                  </label>
                  <input
                    v-model.number="form.return1mo"
                    type="number"
                    step="0.01"
                    placeholder="비워두면 유지"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>

                <!-- 3개월 수익률 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    3개월
                  </label>
                  <input
                    v-model.number="form.return3mo"
                    type="number"
                    step="0.01"
                    placeholder="비워두면 유지"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>

                <!-- 6개월 수익률 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    6개월
                  </label>
                  <input
                    v-model.number="form.return6mo"
                    type="number"
                    step="0.01"
                    placeholder="비워두면 유지"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>

                <!-- 1년 수익률 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    1년
                  </label>
                  <input
                    v-model.number="form.return1yr"
                    type="number"
                    step="0.01"
                    placeholder="비워두면 유지"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>

                <!-- 3년 수익률 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    3년
                  </label>
                  <input
                    v-model.number="form.return3yr"
                    type="number"
                    step="0.01"
                    placeholder="비워두면 유지"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  />
                </div>
              </div>
            </div>

            <!-- 설명 섹션 -->
            <div>
              <h2 class="text-lg font-semibold text-gray-900 mb-4">상세 설명</h2>
              <textarea
                v-model="form.description"
                rows="4"
                placeholder="변경하지 않으려면 비워두세요"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              ></textarea>
            </div>

            <!-- 버튼 영역 -->
            <div class="flex justify-end gap-4 pt-4">
              <button
                type="button"
                @click="goBack"
                class="px-6 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50"
              >
                취소
              </button>
              <button
                type="submit"
                :disabled="submitting"
                class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-400"
              >
                {{ submitting ? '수정 중...' : '수정' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getEtfDetail, updateEtf } from '@/api/etf'

const route = useRoute()
const router = useRouter()

const etfCode = route.params.etfCode
const loading = ref(true)
const error = ref(null)
const submitting = ref(false)
const originalEtf = ref(null)

const form = ref({
  etfName: '',
  market: '',
  theme: '',
  currentPrice: null,
  fee: null,
  aum: null,
  riskRating: null,
  return1mo: null,
  return3mo: null,
  return6mo: null,
  return1yr: null,
  return3yr: null,
  description: ''
})

// 기존 ETF 정보 불러오기
const fetchEtfDetail = async () => {
  try {
    loading.value = true
    // etfCode로 상세 조회 (백엔드에서 etfCode로도 조회 가능하다고 가정)
    // 만약 etfId가 필요하다면 목록에서 찾아야 함
    // 여기서는 etfCode를 etfId처럼 사용
    const response = await getEtfDetail(etfCode)
    originalEtf.value = response.data
    
    // 폼에 기존 값 표시하지 않음 (비워둔 필드는 수정하지 않음)
    // 사용자가 명시적으로 수정할 항목만 입력하도록 유도
  } catch (err) {
    console.error('ETF 조회 실패:', err)
    error.value = 'ETF 정보를 불러오는데 실패했습니다.'
  } finally {
    loading.value = false
  }
}

// 뒤로 가기
const goBack = () => {
  router.back()
}

// 수정 제출
const handleSubmit = async () => {
  try {
    submitting.value = true

    // null과 빈 문자열 필터링 (값이 있는 필드만 전송)
    const data = {}
    Object.keys(form.value).forEach(key => {
      const value = form.value[key]
      if (value !== null && value !== '') {
        data[key] = value
      }
    })

    // 수정할 데이터가 없으면 경고
    if (Object.keys(data).length === 0) {
      alert('수정할 항목을 입력해주세요.')
      return
    }

    const response = await updateEtf(etfCode, data)
    
    // 성공 메시지
    alert(response.data || 'ETF가 수정되었습니다.')
    
    // 상세 페이지로 이동
    router.push(`/etfs/${originalEtf.value.etfId}`)
  } catch (err) {
    console.error('ETF 수정 실패:', err)
    alert('ETF 수정에 실패했습니다.')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  fetchEtfDetail()
})
</script>

<style scoped>
/* 필요한 경우 추가 스타일 */
</style>
