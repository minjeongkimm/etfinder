<template>
  <div class="flex min-h-screen bg-background">
    <main class="flex-1 p-6 lg:p-8">
      <div class="container max-w-4xl">
        <!-- 헤더 -->
        <header class="mb-6">
          <button
            @click="$router.push('/etfs')"
            class="flex items-center gap-2 text-sm text-muted-foreground hover:text-foreground mb-4 transition-all duration-200"
          >
            <span>←</span>
            <span>목록으로 돌아가기</span>
          </button>
          <h1 class="text-3xl font-bold tracking-tight text-foreground">ETF 등록</h1>
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
                    ETF 코드 <span class="text-destructive">*</span>
                  </label>
                  <input
                    v-model="form.etfCode"
                    type="text"
                    required
                    placeholder="예: 381170"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                  />
                </div>

                <div>
                  <label class="block text-sm font-medium text-foreground mb-2">
                    ETF 명 <span class="text-destructive">*</span>
                  </label>
                  <input
                    v-model="form.etfName"
                    type="text"
                    required
                    placeholder="예: TIGER 미국테크TOP10"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                  />
                </div>

                <div>
                  <label class="block text-sm font-medium text-foreground mb-2">
                    시장 <span class="text-destructive">*</span>
                  </label>
                  <select
                    v-model="form.market"
                    required
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                  >
                    <option value="">선택하세요</option>
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
                    placeholder="예: 기술주"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                  />
                </div>

                <div>
                  <label class="block text-sm font-medium text-foreground mb-2">현재가 (원)</label>
                  <input
                    v-model.number="form.currentPrice"
                    type="number"
                    step="1"
                    placeholder="예: 15420"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm font-mono focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                  />
                </div>

                <div>
                  <label class="block text-sm font-medium text-foreground mb-2">총보수 (%)</label>
                  <input
                    v-model.number="form.fee"
                    type="number"
                    step="0.01"
                    placeholder="예: 0.49"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm font-mono focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                  />
                </div>

                <div>
                  <label class="block text-sm font-medium text-foreground mb-2">시가총액 (원)</label>
                  <input
                    v-model.number="form.aum"
                    type="number"
                    step="1"
                    placeholder="예: 450000000000"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm font-mono focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                  />
                </div>

                <div>
                  <label class="block text-sm font-medium text-foreground mb-2">위험등급</label>
                  <select
                    v-model.number="form.riskRating"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                  >
                    <option :value="null">선택하세요</option>
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
                    placeholder="예: 2.4"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm font-mono focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-foreground mb-2">3개월</label>
                  <input
                    v-model.number="form.return3mo"
                    type="number"
                    step="0.01"
                    placeholder="예: 5.2"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm font-mono focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-foreground mb-2">6개월</label>
                  <input
                    v-model.number="form.return6mo"
                    type="number"
                    step="0.01"
                    placeholder="예: 8.1"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm font-mono focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-foreground mb-2">1년</label>
                  <input
                    v-model.number="form.return1yr"
                    type="number"
                    step="0.01"
                    placeholder="예: 15.3"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm font-mono focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-foreground mb-2">3년</label>
                  <input
                    v-model.number="form.return3yr"
                    type="number"
                    step="0.01"
                    placeholder="예: 45.7"
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
                placeholder="ETF에 대한 설명을 입력하세요"
                class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring transition-all duration-200 resize-none"
              ></textarea>
            </section>

            <!-- 버튼 -->
            <div class="flex justify-end gap-3 pt-4 border-t border-border">
              <button
                type="button"
                @click="$router.push('/etfs')"
                class="px-6 py-2.5 text-sm font-medium rounded-lg border border-input bg-background hover:bg-accent hover:text-accent-foreground transition-all duration-200"
              >
                취소
              </button>
              <button
                type="submit"
                :disabled="submitting"
                class="px-6 py-2.5 text-sm font-medium rounded-lg bg-primary text-primary-foreground hover:bg-primary/90 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200"
              >
                {{ submitting ? '등록 중...' : '등록' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { insertEtf } from '@/api/etf'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const submitting = ref(false)

const form = ref({
  etfCode: '',
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

    const response = await insertEtf(data)
    alert(response.data || 'ETF가 등록되었습니다.')
    router.push('/etfs')
  } catch (err) {
    console.error('ETF 등록 실패:', err)
    alert('ETF 등록에 실패했습니다.')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
/* Tailwind로 처리 */
</style>
