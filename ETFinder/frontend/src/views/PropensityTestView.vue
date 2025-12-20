<script setup>
import { submitPropensityTest } from '@/api/propensity'
import { ArrowRight, CheckCircle2, Loader2, RotateCcw } from 'lucide-vue-next'
import { computed, ref } from 'vue'

// ==============================
// State Management
// ==============================

const currentStep = ref(0) // 0-4: 질문, 5: 결과
const answers = ref([null, null, null, null, null]) // 각 질문의 선택된 점수
const isSubmitting = ref(false)
const result = ref(null) // PropensityResult 객체
const error = ref(null)

// ==============================
// 설문 데이터 (백엔드 명세 기반)
// ⚠️ 질문 텍스트는 백엔드 명세와 정확히 일치해야 함
// ==============================

const QUESTIONS = [
  {
    id: 1,
    title: 'Q1.',
    question: '투자를 통해 기대하는 수익률은 어느 정도인가요?',
    options: [
      { text: '투자는 해본 적 없어요. 예금이나 적금만 이용합니다.', score: 10 },
      { text: '펀드나 ETF 같은 금융 상품에 가입해 본 적이 있어요.', score: 20 },
      { text: '주식 투자를 직접 하고 있고, 기본적인 용어는 알고 있어요.', score: 30 },
      { text: '주식은 물론 파생상품(선물/옵션)이나 가상화폐 등 다양한 투자를 경험해 봤어요.', score: 40 }
    ]
  },
  {
    id: 2,
    title: 'Q2.',
    question: '이번 투자를 위한 자금은 언제 사용하실 계획인가요?',
    options: [
      { text: '1년 이내입니다. 조만간 써야 할 자금이에요.', score: 10 },
      { text: '1년 ~ 3년 정도는 묶어둘 수 있어요.', score: 20 },
      { text: '3년 ~ 5년 정도는 없어도 되는 여유 자금이에요.', score: 30 },
      { text: '5년 이상, 노후 대비나 장기적인 목적이라 잊고 지낼 수 있어요.', score: 40 }
    ]
  },
  {
    id: 3,
    title: 'Q3.',
    question: '투자한 자산이 일주일 만에 -20% 하락했습니다. 이때 어떤 생각이 드시나요?',
    options: [
      { text: '너무 불안해서 잠이 안 옵니다. 당장 매도해서 남은 돈이라도 지킵니다.', score: 10 },
      { text: '걱정은 되지만, 일단 조금 더 지켜보다가 계속 떨어지면 매도합니다.', score: 20 },
      { text: '"언젠가는 다시 오르겠지"라고 생각하며 회복될 때까지 기다립니다.', score: 30 },
      { text: '"이건 저가 매수의 기회다!"라고 생각하며 추가로 매수합니다.', score: 40 }
    ]
  },
  {
    id: 4,
    title: 'Q4.',
    question: '이번 투자를 통해 기대하는 연간 수익률은 어느 정도인가요?',
    options: [
      { text: '원금 보전이 최우선입니다. 은행 이자보다 조금만 더 높으면 만족해요. (연 3~5%)', score: 10 },
      { text: '물가 상승률보다는 높아야 투자의 의미가 있다고 생각해요. (연 6~9%)', score: 20 },
      { text: '주식 시장의 평균적인 수익률 정도는 기대하고 있어요. (연 10~15%)', score: 30 },
      { text: '위험을 감수하더라도 남들보다 월등히 높은 고수익을 노립니다. (연 20% 이상)', score: 40 }
    ]
  },
  {
    id: 5,
    title: 'Q5.',
    question: '현재 고객님의 소득 및 자산 현황은 어떠신가요?',
    options: [
      { text: '현재 소득이 없거나 불규칙해서 여유가 없어요.', score: 10 },
      { text: '고정적인 소득은 있지만, 투자 비중을 늘리기엔 빠듯해요.', score: 20 },
      { text: '매달 월급을 받고 있고, 어느 정도 여유 자금을 운용하고 있어요.', score: 30 },
      { text: '소득이 안정적이며, 이미 모아둔 자산도 충분히 있습니다.', score: 40 }
    ]
  }
]

// ==============================
// Computed Properties
// ==============================

const totalSteps = computed(() => QUESTIONS.length)
const progressPercent = computed(() => ((currentStep.value + 1) / totalSteps.value) * 100)
const canProceed = computed(() => answers.value[currentStep.value] !== null)
const allAnswered = computed(() => answers.value.every(answer => answer !== null))

// ==============================
// Methods
// ==============================

const selectOption = (score) => {
  answers.value[currentStep.value] = score
}

const nextStep = () => {
  if (currentStep.value < totalSteps.value - 1) {
    currentStep.value++
  } else if (allAnswered.value) {
    submitTest()
  }
}

const prevStep = () => {
  if (currentStep.value > 0) {
    currentStep.value--
  }
}

const submitTest = async () => {
  if (!allAnswered.value) return

  isSubmitting.value = true
  error.value = null

  try {
    const response = await submitPropensityTest(answers.value)
    result.value = response.data
    currentStep.value = totalSteps.value // 결과 화면으로 이동
  } catch (err) {
    console.error('투자 성향 테스트 제출 실패:', err)
    error.value = err.response?.data?.message || '테스트 제출에 실패했습니다. 다시 시도해주세요.'
  } finally {
    isSubmitting.value = false
  }
}

const restartTest = () => {
  currentStep.value = 0
  answers.value = [null, null, null, null, null]
  result.value = null
  error.value = null
}

// 포트폴리오 구성 비율 계산 (목업 기반)
const portfolioData = computed(() => {
  if (!result.value) return null
  
  // 투자 성향에 따른 포트폴리오 구성 (예시)
  const portfolios = {
    'STABLE': { stocks: 30, bonds: 50, alternatives: 20 },
    'NEUTRAL': { stocks: 50, bonds: 30, alternatives: 20 },
    'AGGRESSIVE': { stocks: 70, bonds: 10, alternatives: 20 }
  }
  
  return portfolios[result.value.type] || portfolios['NEUTRAL']
})
</script>

<template>
  <div class="min-h-screen bg-background">
    <!-- 로딩 화면 -->
    <div v-if="isSubmitting" class="container mx-auto px-4 py-8">
      <div class="w-full max-w-2xl mx-auto">
        <div class="bg-card border border-border rounded-xl p-8 lg:p-12 flex flex-col items-center justify-center space-y-6 min-h-[400px]">
          <Loader2 class="w-16 h-16 text-primary animate-spin" />
          <div class="text-center space-y-3">
            <h2 class="text-2xl font-bold text-foreground">분석 중입니다</h2>
            <div class="space-y-2">
              <p class="text-base text-muted-foreground">
                🤖✨ AI가 투자 성향을 분석하고 있어요.
              </p>
              <p class="text-base text-muted-foreground">
                📊 회원님께 맞는 투자 스타일을 정리 중이에요.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 결과 화면 -->
    <div v-else-if="currentStep === totalSteps && result" class="container mx-auto px-4 py-8">
      <div class="w-full max-w-2xl mx-auto space-y-8">
        <!-- 완료 아이콘 -->
        <div class="flex justify-center">
          <div class="w-20 h-20 bg-primary/10 rounded-full flex items-center justify-center">
            <CheckCircle2 class="w-12 h-12 text-primary" />
          </div>
        </div>

        <!-- 제목 -->
        <div class="text-center space-y-2">
          <h1 class="text-3xl font-bold text-foreground">분석이 완료되었습니다!</h1>
          <p class="text-muted-foreground">
            회원님의 투자 성향은 
            <span class="text-primary font-bold">"{{ result.label }}"</span> 
            입니다.
          </p>
        </div>

        <!-- AI 분석 결과 카드 -->
        <div class="bg-card border border-border rounded-xl p-6 lg:p-8 space-y-6">
          <h2 class="text-xl font-bold text-foreground">AI 투자 코멘트</h2>
          
          <div class="space-y-5">
            <!-- 제목 -->
            <div v-if="result.aiResult?.title">
              <h3 class="text-lg font-semibold text-foreground">{{ result.aiResult.title }}</h3>
            </div>

            <!-- 분석 이유 -->
            <div v-if="result.aiResult?.reason">
              <p class="text-sm text-muted-foreground leading-relaxed whitespace-pre-line">
                {{ result.aiResult.reason }}
              </p>
            </div>

            <!-- 조언 -->
            <div v-if="result.aiResult?.advice" class="bg-primary/5 border border-primary/20 rounded-lg p-4">
              <p class="text-sm text-foreground leading-relaxed whitespace-pre-line">
                {{ result.aiResult.advice }}
              </p>
            </div>

            <!-- 응원 메시지 -->
            <div v-if="result.aiResult?.cheering" class="text-center pt-2">
              <p class="text-sm text-primary font-semibold">
                {{ result.aiResult.cheering }}
              </p>
            </div>
          </div>

          <!-- 추천 포트폴리오 구성 -->
          <div v-if="portfolioData" class="space-y-3 pt-4 border-t border-border">
            <h3 class="font-semibold text-foreground">추천 포트폴리오 구성</h3>
            
            <!-- 포트폴리오 바 차트 -->
            <div class="relative h-10 rounded-full overflow-hidden flex">
              <div 
                class="h-full bg-green-500 flex items-center justify-center text-xs font-medium text-white transition-all duration-500"
                :style="{ width: `${portfolioData.stocks}%` }"
              >
                <span v-if="portfolioData.stocks >= 10">주식 {{ portfolioData.stocks }}%</span>
              </div>
              <div 
                class="h-full bg-blue-500 flex items-center justify-center text-xs font-medium text-white transition-all duration-500"
                :style="{ width: `${portfolioData.bonds}%` }"
              >
                <span v-if="portfolioData.bonds >= 10">채권 {{ portfolioData.bonds }}%</span>
              </div>
              <div 
                class="h-full bg-orange-500 flex items-center justify-center text-xs font-medium text-white transition-all duration-500"
                :style="{ width: `${portfolioData.alternatives}%` }"
              >
                <span v-if="portfolioData.alternatives >= 10">대체투자 {{ portfolioData.alternatives }}%</span>
              </div>
            </div>

            <!-- 범례 -->
            <div class="flex items-center justify-center gap-6 text-xs">
              <div class="flex items-center gap-2">
                <div class="w-3 h-3 bg-green-500 rounded-full"></div>
                <span class="text-muted-foreground">주식 {{ portfolioData.stocks }}%</span>
              </div>
              <div class="flex items-center gap-2">
                <div class="w-3 h-3 bg-blue-500 rounded-full"></div>
                <span class="text-muted-foreground">채권 {{ portfolioData.bonds }}%</span>
              </div>
              <div class="flex items-center gap-2">
                <div class="w-3 h-3 bg-orange-500 rounded-full"></div>
                <span class="text-muted-foreground">대체투자 {{ portfolioData.alternatives }}%</span>
              </div>
            </div>
          </div>

          <!-- 액션 버튼 -->
          <div class="flex flex-col sm:flex-row gap-3 pt-4">
            <button
              @click="$router.push({ name: 'recommend' })"
              class="flex-1 px-6 py-3.5 bg-primary text-primary-foreground rounded-lg font-semibold hover:bg-primary/90 transition-all duration-200 shadow-sm hover:shadow flex items-center justify-center gap-2"
            >
              추천 ETF 보러가기
              <ArrowRight class="w-4 h-4" />
            </button>
            <button
              @click="restartTest"
              class="px-6 py-3.5 bg-card border-2 border-border text-foreground rounded-lg font-semibold hover:bg-accent hover:border-primary/30 transition-all duration-200 flex items-center justify-center gap-2"
            >
              <RotateCcw class="w-4 h-4" />
              다시하기
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 질문 화면 -->
    <div v-else class="container mx-auto px-4 pt-16 pb-8">
      <div class="w-full max-w-2xl mx-auto space-y-6">
        <!-- 진행률 표시 -->
        <div class="space-y-2">
          <div class="flex items-center justify-between">
            <span class="text-sm font-semibold text-foreground">Step {{ currentStep + 1 }}</span>
          </div>
          <div class="h-2 bg-muted rounded-full overflow-hidden">
            <div 
              class="h-full bg-primary transition-all duration-300 ease-out rounded-full"
              :style="{ width: `${progressPercent}%` }"
            ></div>
          </div>
        </div>

        <!-- 질문 카드 -->
        <div class="bg-card border border-border rounded-xl p-6 lg:p-8 space-y-6">
          <div class="space-y-3">
            <h2 class="text-lg font-bold text-primary">
              {{ QUESTIONS[currentStep].title }}
            </h2>
            <p class="text-xl lg:text-2xl font-bold text-foreground leading-snug">
              {{ QUESTIONS[currentStep].question }}
            </p>
          </div>

          <!-- 선택지 -->
          <div class="space-y-3">
            <button
              v-for="option in QUESTIONS[currentStep].options"
              :key="option.score"
              @click="selectOption(option.score)"
              :class="[
                'w-full p-4 rounded-lg border-2 text-left transition-all duration-200',
                'flex items-center justify-between gap-3',
                answers[currentStep] === option.score
                  ? 'border-primary bg-primary/5 shadow-sm'
                  : 'border-border bg-card hover:border-primary/30 hover:bg-primary/5'
              ]"
            >
              <span class="text-sm font-medium text-foreground flex-1">
                {{ option.text }}
              </span>
              <CheckCircle2 
                v-if="answers[currentStep] === option.score"
                class="w-5 h-5 text-primary flex-shrink-0"
              />
            </button>
          </div>

          <!-- 에러 메시지 -->
          <div v-if="error" class="p-4 bg-destructive/10 border border-destructive/30 rounded-lg">
            <p class="text-sm text-destructive">{{ error }}</p>
          </div>

          <!-- 네비게이션 버튼 -->
          <div class="pt-2 flex gap-3">
            <button
              v-if="currentStep > 0"
              @click="prevStep"
              :disabled="isSubmitting"
              class="px-6 py-3 rounded-lg font-semibold transition-all duration-200 bg-muted text-muted-foreground hover:bg-muted/80 flex items-center justify-center gap-2"
            >
              이전
            </button>
            <button
              @click="nextStep"
              :disabled="!canProceed || isSubmitting"
              :class="[
                'flex-1 px-6 py-3 rounded-lg font-semibold transition-all duration-200',
                'flex items-center justify-center gap-2',
                canProceed && !isSubmitting
                  ? 'bg-primary text-primary-foreground hover:bg-primary/90 shadow-sm'
                  : 'bg-muted text-muted-foreground cursor-not-allowed opacity-60'
              ]"
            >
              <Loader2 v-if="isSubmitting" class="w-4 h-4 animate-spin" />
              <span v-if="isSubmitting">제출 중...</span>
              <span v-else-if="currentStep === totalSteps - 1">결과 보기</span>
              <span v-else>다음</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


