<template>
  <div class="min-h-screen bg-background flex flex-col items-center justify-center p-4">
    <!-- 배경 장식 -->
    <div class="fixed top-20 right-20 w-72 h-72 bg-primary/5 rounded-full blur-3xl -z-10"></div>
    <div class="fixed bottom-20 left-20 w-96 h-96 bg-primary/3 rounded-full blur-3xl -z-10"></div>

    <div class="max-w-md w-full bg-card rounded-2xl shadow-lg border border-border p-8 transition-all duration-300">
      
      <!-- 헤더 (Step 1일 때만 표시하거나, Step 2에서는 축하 메시지로 변경) -->
      <div class="text-center mb-8">
        <div v-if="step === 'form'">
          <h1 class="text-3xl font-bold text-foreground mb-2">가입을 축하합니다! 🎉</h1>
          <p class="text-muted-foreground">더 나은 서비스 제공을 위해 추가 정보를 입력해주세요.</p>
        </div>
        <div v-else>
          <div class="w-16 h-16 bg-green-100 text-green-600 rounded-full flex items-center justify-center mx-auto mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><path d="m9 11 3 3L22 4"/></svg>
          </div>
          <h1 class="text-3xl font-bold text-foreground mb-2">회원가입 완료!</h1>
          <p class="text-muted-foreground">이제 ETFinder의 모든 기능을 이용하실 수 있습니다.</p>
        </div>
      </div>

      <!-- Step 1: 정보 입력 폼 -->
      <div v-if="step === 'form'" class="space-y-6">
        <!-- 닉네임 입력 -->
        <div class="space-y-2">
          <label class="text-sm font-medium text-foreground">닉네임</label>
          <input 
            v-model="form.nickname"
            type="text" 
            placeholder="닉네임을 입력하세요"
            class="w-full px-4 py-3 bg-secondary/50 border border-input rounded-xl focus:outline-none focus:ring-2 focus:ring-primary/50 transition-all placeholder:text-muted-foreground/50 text-foreground"
          />
        </div>

        <!-- 이메일 입력 -->
        <div class="space-y-2">
          <label class="text-sm font-medium text-foreground">이메일</label>
          <input 
            v-model="form.email"
            type="email" 
            placeholder="example@email.com"
            class="w-full px-4 py-3 bg-secondary/50 border border-input rounded-xl focus:outline-none focus:ring-2 focus:ring-primary/50 transition-all placeholder:text-muted-foreground/50 text-foreground"
          />
        </div>

        <!-- 나이 입력 -->
        <div class="space-y-2">
          <label class="text-sm font-medium text-foreground">나이</label>
          <input 
            v-model="form.age"
            type="number" 
            placeholder="나이를 입력하세요"
            class="w-full px-4 py-3 bg-secondary/50 border border-input rounded-xl focus:outline-none focus:ring-2 focus:ring-primary/50 transition-all placeholder:text-muted-foreground/50 text-foreground arrow-hide"
          />
        </div>

        <button 
          @click="submitForm"
          :disabled="!isValid"
          class="w-full py-4 bg-primary text-primary-foreground font-bold rounded-xl shadow-lg shadow-primary/20 hover:shadow-xl hover:shadow-primary/30 active:scale-[0.98] transition-all disabled:opacity-50 disabled:cursor-not-allowed"
        >
          완료하고 시작하기
        </button>
      </div>

      <!-- Step 2: 완료 및 유도 화면 -->
      <div v-else class="space-y-4">
        <div class="bg-secondary/30 p-4 rounded-xl mb-6">
          <p class="text-sm text-foreground/80 leading-relaxed text-center">
            나에게 딱 맞는 ETF를 찾기 위해<br>
            <strong>투자 성향 테스트</strong>를 진행해보세요!
          </p>
        </div>

        <button 
          @click="goToPropensityTest"
          class="w-full py-4 bg-primary text-primary-foreground font-bold rounded-xl shadow-lg shadow-primary/20 hover:shadow-xl hover:shadow-primary/30 active:scale-[0.98] transition-all flex items-center justify-center gap-2"
        >
          <span>투자 성향 테스트 하러가기</span>
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12h14"/><path d="m12 5 7 7-7 7"/></svg>
        </button>

        <button 
          @click="goToHome"
          class="w-full py-3 bg-transparent text-muted-foreground font-medium hover:text-foreground transition-colors text-sm"
        >
          다음에 할게요 (홈으로 가기)
        </button>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import http from '@/util/http-common'

const router = useRouter()
const step = ref('form') // form, success

const form = reactive({
  nickname: '',
  email: '',
  age: null
})

// 유효성 검사
const isValid = computed(() => {
  return form.nickname?.trim() && form.email?.trim() && form.age > 0
})

onMounted(() => {
  // Router State에서 닉네임 가져오기 (없으면 빈 값)
  const stateNickname = history.state.nickname
  if (stateNickname) {
    form.nickname = stateNickname
  }
})

const submitForm = async () => {
  if (!isValid.value) return

  try {
    const payload = {
      nickname: form.nickname,
      email: form.email,
      age: parseInt(form.age)
    }

    // 정보 업데이트 요청
    await http.patch('/users/me', payload)
    
    // 성공 시 다음 단계로
    step.value = 'success'
    
  } catch (error) {
    console.error('정보 업데이트 실패:', error)
    alert('정보 저장 중 오류가 발생했습니다. 다시 시도해주세요.')
  }
}

const goToPropensityTest = () => {
  router.replace('/propensity/test')
}

const goToHome = () => {
  router.replace('/')
}
</script>

<style scoped>
/* Chrome, Safari, Edge, Opera */
.arrow-hide::-webkit-outer-spin-button,
.arrow-hide::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
.arrow-hide {
  -moz-appearance: textfield;
  appearance: textfield;
}
</style>
