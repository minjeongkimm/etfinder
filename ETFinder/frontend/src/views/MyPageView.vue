<template>
  <div class="min-h-screen bg-background">
    <!-- 메인 콘텐츠 -->
    <main class="flex-1 p-6 lg:p-8">
      <div class="container max-w-4xl">
        <!-- 헤더 -->
        <header class="mb-8">
          <div class="flex items-center gap-3 mb-3">
            <div class="w-10 h-10 rounded-xl bg-primary flex items-center justify-center">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-primary-foreground"><path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
            </div>
            <h1 class="text-3xl font-bold tracking-tight text-foreground">마이페이지</h1>
          </div>
          <p class="text-sm text-muted-foreground">내 정보를 확인하고 수정할 수 있습니다.</p>
        </header>

        <!-- 로딩 상태 -->
        <div v-if="loading" class="flex justify-center items-center py-20">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
        </div>

        <!-- 메인 카드 -->
        <div v-else class="rounded-xl border border-border bg-card shadow-sm">
          <!-- 프로필 헤더 -->
          <div class="p-6 border-b border-border bg-gradient-to-br from-primary/5 to-transparent">
            <div class="flex items-center gap-4">
              <div class="w-16 h-16 bg-primary rounded-full flex items-center justify-center">
                <span class="text-2xl font-bold text-primary-foreground">
                  {{ getUserInitial() }}
                </span>
              </div>
              <div>
                <h2 class="text-2xl font-bold text-foreground mb-1">
                  {{ formData.nickname || '사용자' }}님
                </h2>
                <p class="text-sm text-muted-foreground">
                  ETFinder와 함께 스마트한 투자를 시작하세요
                </p>
              </div>
            </div>
          </div>

          <!-- 폼 영역 -->
          <form @submit.prevent="handleSubmit" class="p-6 space-y-6">
            <!-- 기본 정보 섹션 -->
            <div>
              <h3 class="text-lg font-semibold text-foreground mb-4">기본 정보 수정</h3>
              <p class="text-sm text-muted-foreground mb-6">
                서비스 이용에 필요한 기본 정보를 관리합니다.
              </p>

              <div class="space-y-5">
                <!-- 닉네임 -->
                <div>
                  <label class="flex items-center gap-2 text-sm font-medium text-foreground mb-2">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                    닉네임
                  </label>
                  <input
                    v-model="formData.nickname"
                    type="text"
                    required
                    placeholder="닉네임을 입력하세요"
                    class="w-full px-4 py-3 bg-background border border-input rounded-lg text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:border-transparent transition-all duration-200"
                  />
                </div>

                <!-- 이메일 (읽기 전용) -->
                <div>
                  <label class="flex items-center gap-2 text-sm font-medium text-foreground mb-2">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="20" height="16" x="2" y="4" rx="2"/><path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"/></svg>
                    이메일
                  </label>
                  <input
                    v-model="formData.email"
                    type="email"
                    disabled
                    class="w-full px-4 py-3 bg-muted border border-border rounded-lg text-muted-foreground cursor-not-allowed"
                  />
                  <p class="text-xs text-muted-foreground mt-2">이메일은 변경할 수 없습니다.</p>
                </div>

                <!-- 나이 -->
                <div>
                  <label class="flex items-center gap-2 text-sm font-medium text-foreground mb-2">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="18" height="18" x="3" y="4" rx="2" ry="2"/><line x1="16" x2="16" y1="2" y2="6"/><line x1="8" x2="8" y1="2" y2="6"/><line x1="3" x2="21" y1="10" y2="10"/></svg>
                    나이
                  </label>
                  <input
                    v-model.number="formData.age"
                    type="number"
                    required
                    min="1"
                    max="120"
                    placeholder="나이를 입력하세요"
                    class="w-full px-4 py-3 bg-background border border-input rounded-lg text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:border-transparent transition-all duration-200"
                  />
                </div>

                <!-- 투자 성향 -->
                <div>
                  <label class="flex items-center gap-2 text-sm font-medium text-foreground mb-2">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 3v18h18"/><path d="M18 17V9"/><path d="M13 17V5"/><path d="M8 17v-3"/></svg>
                    투자 성향
                  </label>
                  <select
                    v-model="formData.propensity"
                    required
                    class="w-full px-4 py-3 bg-background border border-input rounded-lg text-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:border-transparent transition-all duration-200 cursor-pointer"
                  >
                    <option value="" disabled>투자 성향을 선택하세요</option>
                    <option value="안정형">안정형 (Stable)</option>
                    <option value="중립형">중립형 (Neutral)</option>
                    <option value="공격형">공격형 (Aggressive)</option>
                  </select>
                  <p class="text-xs text-muted-foreground mt-2">
                    투자 성향에 따라 최적의 ETF 추천을 받을 수 있습니다.
                  </p>
                </div>
              </div>
            </div>

            <!-- 버튼 영역 -->
            <div class="flex gap-3 pt-4">
              <button
                type="submit"
                :disabled="saving"
                class="flex-1 flex items-center justify-center gap-2 px-6 py-3 bg-primary text-primary-foreground rounded-lg font-semibold hover:bg-primary/90 transition-all duration-200 shadow-sm hover:shadow-md disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <svg v-if="!saving" xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"/><polyline points="17 21 17 13 7 13 7 21"/><polyline points="7 3 7 8 15 8"/></svg>
                <div v-else class="animate-spin rounded-full h-5 w-5 border-b-2 border-primary-foreground"></div>
                <span>{{ saving ? '저장 중...' : '변경사항 저장' }}</span>
              </button>

              <button
                type="button"
                @click="resetForm"
                :disabled="saving"
                class="px-6 py-3 bg-muted text-foreground rounded-lg font-semibold hover:bg-muted/80 transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                취소
              </button>
            </div>
          </form>
        </div>

        <!-- 추가 정보 카드 -->
        <div class="mt-6 grid grid-cols-1 md:grid-cols-3 gap-4">
          <!-- 가입일 -->
          <div class="rounded-xl border border-border bg-card shadow-sm p-4">
            <div class="flex items-center gap-2 text-sm text-muted-foreground mb-1">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="18" height="18" x="3" y="4" rx="2" ry="2"/><line x1="16" x2="16" y1="2" y2="6"/><line x1="8" x2="8" y1="2" y2="6"/><line x1="3" x2="21" y1="10" y2="10"/></svg>
              가입일
            </div>
            <p class="text-lg font-semibold text-foreground">
              {{ formatDate(authStore.user?.createdAt) }}
            </p>
          </div>

          <!-- 투자 성향 -->
          <div class="rounded-xl border border-border bg-card shadow-sm p-4">
            <div class="flex items-center gap-2 text-sm text-muted-foreground mb-1">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 3v18h18"/><path d="M18 17V9"/><path d="M13 17V5"/><path d="M8 17v-3"/></svg>
              현재 투자 성향
            </div>
            <p class="text-lg font-semibold text-foreground">
              {{ formData.propensity || '-' }}
            </p>
          </div>

          <!-- 로그인 방식 -->
          <div class="rounded-xl border border-border bg-card shadow-sm p-4">
            <div class="flex items-center gap-2 text-sm text-muted-foreground mb-1">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12.22 2h-.44a2 2 0 0 0-2 2v.18a2 2 0 0 1-1 1.73l-.43.25a2 2 0 0 1-2 0l-.15-.08a2 2 0 0 0-2.73.73l-.22.38a2 2 0 0 0 .73 2.73l.15.1a2 2 0 0 1 1 1.72v.51a2 2 0 0 1-1 1.74l-.15.09a2 2 0 0 0-.73 2.73l.22.38a2 2 0 0 0 2.73.73l.15-.08a2 2 0 0 1 2 0l.43.25a2 2 0 0 1 1 1.73V20a2 2 0 0 0 2 2h.44a2 2 0 0 0 2-2v-.18a2 2 0 0 1 1-1.73l.43-.25a2 2 0 0 1 2 0l.15.08a2 2 0 0 0 2.73-.73l.22-.39a2 2 0 0 0-.73-2.73l-.15-.08a2 2 0 0 1-1-1.74v-.5a2 2 0 0 1 1-1.74l.15-.09a2 2 0 0 0 .73-2.73l-.22-.38a2 2 0 0 0-2.73-.73l-.15.08a2 2 0 0 1-2 0l-.43-.25a2 2 0 0 1-1-1.73V4a2 2 0 0 0-2-2z"/><circle cx="12" cy="12" r="3"/></svg>
              로그인 방식
            </div>
            <p class="text-lg font-semibold text-foreground">
              카카오
            </p>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { updateMyInfo } from '@/api/user'
import { useAuthStore } from '@/stores/auth'
import { onMounted, reactive, ref } from 'vue'

const authStore = useAuthStore()

const loading = ref(true)
const saving = ref(false)

const formData = reactive({
  nickname: '',
  email: '',
  age: null,
  propensity: ''
})

// 사용자 초기 가져오기
const getUserInitial = () => {
  const nickname = formData.nickname || '사용자'
  return nickname.charAt(0).toUpperCase()
}

// 날짜 포맷팅 (2024.11.03 형식)
const formatDate = (dateString) => {
  if (!dateString) return '-'
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}.${month}.${day}`
}

// 폼 초기화
const resetForm = () => {
  if (authStore.user) {
    formData.nickname = authStore.user.nickname || ''
    formData.email = authStore.user.email || ''
    formData.age = authStore.user.age || null
    formData.propensity = authStore.user.propensity || ''
  }
}

// 데이터 로드
const loadUserData = async () => {
  loading.value = true
  try {
    await authStore.getMyInfo()
    resetForm()
  } catch (error) {
    console.error('사용자 정보 로드 실패:', error)
    alert('사용자 정보를 불러오는데 실패했습니다.')
  } finally {
    loading.value = false
  }
}

// 폼 제출
const handleSubmit = async () => {
  saving.value = true
  try {
    const updateData = {
      nickname: formData.nickname,
      age: formData.age,
      propensity: formData.propensity
    }

    // 1. 업데이트 요청
    const response = await updateMyInfo(updateData)
    
    if (response.status === 200) {
      // 2. 최신 사용자 정보를 다시 가져와서 동기화
      const updatedUser = await authStore.getMyInfo()
      
      // 3. 폼 데이터를 최신 데이터로 업데이트
      if (updatedUser) {
        formData.nickname = updatedUser.nickname || ''
        formData.email = updatedUser.email || ''
        formData.age = updatedUser.age || null
        formData.propensity = updatedUser.propensity || ''
      }
      
      alert('회원 정보가 성공적으로 업데이트되었습니다.')
    }
  } catch (error) {
    console.error('정보 업데이트 실패:', error)
    alert(error.response?.data || '정보 업데이트에 실패했습니다.')
  } finally {
    saving.value = false
  }
}

// 초기 로드
onMounted(() => {
  loadUserData()
})
</script>

<style scoped>
/* Tailwind로 처리 */
</style>