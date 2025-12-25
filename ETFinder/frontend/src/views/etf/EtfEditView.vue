<template>
  <div class="flex min-h-screen bg-background">
    <main class="flex-1 p-6 lg:p-8">
      <div class="container max-w-4xl">
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

        <!-- 수정 폼 -->
        <div v-else>
          <!-- 헤더 -->
          <header class="mb-6">
            <button
              @click="goBack"
              class="flex items-center gap-2 text-sm text-muted-foreground hover:text-foreground mb-4 transition-all duration-200"
            >
              <span>←</span>
              <span>뒤로 가기</span>
            </button>
            <h1 class="text-3xl font-bold tracking-tight text-foreground mb-2">ETF 수정</h1>
            <p class="text-sm text-muted-foreground">
              수정하지 않을 항목은 비워두시면 기존 값이 유지됩니다.
            </p>
          </header>

          <!-- 폼 카드 -->
          <div class="rounded-xl border border-border bg-card shadow-sm">
            <form @submit.prevent="handleSubmit" class="p-6 space-y-8">
              <!-- 기본 정보 -->
              <section>
                <h2 class="text-xl font-semibold text-foreground mb-4">기본 정보</h2>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label class="block text-sm font-medium text-foreground mb-2">
                      ETF 코드 (수정 불가)
                    </label>
                    <input
                      :value="originalEtf?.etfCode"
                      type="text"
                      disabled
                      class="w-full rounded-lg border border-input bg-muted px-3 py-2 text-sm text-muted-foreground cursor-not-allowed"
                    />
                  </div>

                  <div>
                    <label class="block text-sm font-medium text-foreground mb-2">ETF 명</label>
                    <input
                      v-model="form.etfName"
                      type="text"
                      placeholder="변경하지 않으려면 비워두세요"
                      class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                    />
                  </div>

                  <div>
                    <label class="block text-sm font-medium text-foreground mb-2">시장</label>
                    <select
                      v-model="form.market"
                      class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                    >
                      <option value="">변경하지 않음</option>
                      <option value="KOR">한국</option>
                      <option value="USA">미국</option>
                      <option value="GLOBAL">글로벌</option>
                    </select>
                  </div>

                  <div>
                    <label class="block text-sm font-medium text-foreground mb-2">테마</label>
                    <input
                      v-model="form.theme"
                      type="text"
                      placeholder="변경하지 않으려면 비워두세요"
                      class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                    />
                  </div>

                  <div>
                    <label class="block text-sm font-medium text-foreground mb-2">현재가 (원)</label>
                    <input
                      v-model.number="form.currentPrice"
                      type="number"
                      step="1"
                      placeholder="변경하지 않으려면 비워두세요"
                      class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm font-mono focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                    />
                  </div>

                  <div>
                    <label class="block text-sm font-medium text-foreground mb-2">총보수 (%)</label>
                    <input
                      v-model.number="form.fee"
                      type="number"
                      step="0.01"
                      placeholder="변경하지 않으려면 비워두세요"
                      class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm font-mono focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                    />
                  </div>

                  <div>
                    <label class="block text-sm font-medium text-foreground mb-2">시가총액 (원)</label>
                    <input
                      v-model.number="form.aum"
                      type="number"
                      step="1"
                      placeholder="변경하지 않으려면 비워두세요"
                      class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm font-mono focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                    />
                  </div>

                  <div>
                    <label class="block text-sm font-medium text-foreground mb-2">위험등급</label>
                    <select
                      v-model.number="form.riskRating"
                      class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
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
              </section>

              <!-- 수익률 정보 -->
              <section>
                <h2 class="text-xl font-semibold text-foreground mb-4">수익률 정보 (%)</h2>
                <div class="grid grid-cols-2 md:grid-cols-5 gap-4">
                  <div>
                    <label class="block text-sm font-medium text-foreground mb-2">1개월</label>
                    <input
                      v-model.number="form.return1mo"
                      type="number"
                      step="0.01"
                      placeholder="비워두면 유지"
                      class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm font-mono focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-foreground mb-2">3개월</label>
                    <input
                      v-model.number="form.return3mo"
                      type="number"
                      step="0.01"
                      placeholder="비워두면 유지"
                      class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm font-mono focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-foreground mb-2">6개월</label>
                    <input
                      v-model.number="form.return6mo"
                      type="number"
                      step="0.01"
                      placeholder="비워두면 유지"
                      class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm font-mono focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-foreground mb-2">1년</label>
                    <input
                      v-model.number="form.return1yr"
                      type="number"
                      step="0.01"
                      placeholder="비워두면 유지"
                      class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm font-mono focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-foreground mb-2">3년</label>
                    <input
                      v-model.number="form.return3yr"
                      type="number"
                      step="0.01"
                      placeholder="비워두면 유지"
                      class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm font-mono focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                    />
                  </div>
                </div>
              </section>

              <!-- 설명 -->
              <section>
                <h2 class="text-xl font-semibold text-foreground mb-4">상세 설명</h2>
                <textarea
                  v-model="form.description"
                  rows="4"
                  placeholder="변경하지 않으려면 비워두세요"
                  class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200 resize-none"
                ></textarea>
              </section>

              <!-- 버튼 -->
              <div class="flex justify-end gap-3 pt-4 border-t border-border">
                <button
                  type="button"
                  @click="goBack"
                  class="px-6 py-2.5 text-sm font-medium rounded-lg border border-input bg-background hover:bg-accent hover:text-accent-foreground transition-all duration-200"
                >
                  취소
                </button>
                <button
                  type="submit"
                  :disabled="submitting"
                  class="px-6 py-2.5 text-sm font-medium rounded-lg bg-primary text-primary-foreground hover:bg-primary/90 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200"
                >
                  {{ submitting ? '수정 중...' : '수정' }}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { getEtfDetail, updateEtf } from '@/api/etf'
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const etfId = route.params.etfId
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

const fetchEtfDetail = async () => {
  try {
    loading.value = true
    const response = await getEtfDetail(etfId)
    originalEtf.value = response.data
  } catch (err) {
    console.error('ETF 조회 실패:', err)
    error.value = 'ETF 정보를 불러오는데 실패했습니다.'
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.back()
}

const handleSubmit = async () => {
  try {
    submitting.value = true

    const data = {}
    Object.keys(form.value).forEach(key => {
      const value = form.value[key]
      if (value !== null && value !== '') {
        data[key] = value
      }
    })

    if (Object.keys(data).length === 0) {
      alert('수정할 항목을 입력해주세요.')
      return
    }

    // 백엔드 API는 etfCode를 사용하여 수정
    const response = await updateEtf(originalEtf.value.etfCode, data)
    alert(response.data || 'ETF가 수정되었습니다.')
    
    // 수정 후 해당 ETF의 상세 페이지로 이동 (etfId 사용)
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
/* Tailwind로 처리 */
</style>
