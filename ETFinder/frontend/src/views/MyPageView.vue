<template>
  <div class="min-h-screen bg-gradient-to-b from-slate-50 to-slate-100">
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
        <div v-else class="rounded-2xl border border-gray-200 bg-white shadow-lg overflow-hidden">
          <!-- 프로필 헤더 -->
          <div class="p-6 border-b border-gray-100 bg-gradient-to-br from-blue-50/50 via-white to-white relative">
            <!-- 상단 하이라이트 -->
            <div class="absolute top-0 left-0 right-0 h-0.5 bg-gradient-to-r from-blue-400/0 via-blue-400/60 to-blue-400/0"></div>
            <div class="flex items-center gap-4">
              <div class="w-20 h-20 bg-gradient-to-br from-blue-500 to-blue-600 rounded-2xl flex items-center justify-center shadow-md ring-4 ring-blue-50">
                <span class="text-3xl font-bold text-white">
                  {{ getUserInitial() }}
                </span>
              </div>
              <div>
                <h2 class="text-2xl font-bold text-gray-900 mb-1">
                  {{ formData.nickname || '사용자' }}님
                </h2>
                <p class="text-sm text-gray-500">
                  ETFinder와 함께 스마트한 투자를 시작하세요
                </p>
              </div>
            </div>
          </div>

          <!-- 기본 정보 영역 -->
          <div class="p-6 space-y-6">
            <!-- 기본 정보 섹션 헤더 -->
            <div class="flex items-center justify-between mb-6">
              <div>
                <h3 class="text-xl font-bold text-gray-900 mb-1">기본 정보</h3>
                <p class="text-sm text-gray-500">
                  서비스 이용에 필요한 기본 정보를 관리합니다.
                </p>
              </div>
              <!-- Edit 버튼 (View 모드일 때만 표시) -->
              <button
                v-if="!isEditMode"
                @click="enterEditMode"
                class="px-5 py-2.5 text-sm font-semibold text-white bg-gradient-to-r from-blue-600 to-blue-700 hover:from-blue-700 hover:to-blue-800 rounded-lg transition-all duration-200 shadow-sm hover:shadow-md"
              >
                수정하기
              </button>
            </div>

            <!-- View 모드: 읽기 전용 -->
            <div v-if="!isEditMode" class="space-y-6">
              <!-- 닉네임 -->
              <div class="p-4 rounded-xl bg-gradient-to-br from-slate-50 to-white border border-slate-100">
                <label class="flex items-center gap-2 text-xs font-semibold text-gray-400 uppercase tracking-wider mb-2">
                  <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                  닉네임
                </label>
                <p class="text-lg font-semibold text-gray-900">{{ formData.nickname || '-' }}</p>
              </div>

              <!-- 이메일 -->
              <div class="p-4 rounded-xl bg-gradient-to-br from-slate-50 to-white border border-slate-100">
                <label class="flex items-center gap-2 text-xs font-semibold text-gray-400 uppercase tracking-wider mb-2">
                  <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="20" height="16" x="2" y="4" rx="2"/><path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"/></svg>
                  이메일
                </label>
                <p class="text-lg font-semibold text-gray-900 font-mono">{{ formData.email || '-' }}</p>
              </div>

              <!-- 나이 -->
              <div class="p-4 rounded-xl bg-gradient-to-br from-slate-50 to-white border border-slate-100">
                <label class="flex items-center gap-2 text-xs font-semibold text-gray-400 uppercase tracking-wider mb-2">
                  <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="18" height="18" x="3" y="4" rx="2" ry="2"/><line x1="16" x2="16" y1="2" y2="6"/><line x1="8" x2="8" y1="2" y2="6"/><line x1="3" x2="21" y1="10" y2="10"/></svg>
                  나이
                </label>
                <p class="text-lg font-semibold text-gray-900">{{ formData.age ? `${formData.age}세` : '-' }}</p>
              </div>

              <!-- 투자 성향 -->
              <div class="p-4 rounded-xl bg-gradient-to-br from-slate-50 to-white border border-slate-100">
                <label class="flex items-center gap-2 text-xs font-semibold text-gray-400 uppercase tracking-wider mb-2">
                  <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 3v18h18"/><path d="M18 17V9"/><path d="M13 17V5"/><path d="M8 17v-3"/></svg>
                  투자 성향
                </label>
                <p class="text-lg font-semibold text-gray-900">{{ formData.propensity || '-' }}</p>
              </div>
            </div>

            <!-- Edit 모드: 편집 가능한 폼 -->
            <form v-else @submit.prevent="handleSubmit" class="space-y-6">
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
                    class="w-full px-4 py-3 bg-background border border-input rounded-lg text-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:border-transparent transition-all duration-200 cursor-pointer"
                  >
                    <option value="" disabled>투자 성향을 선택하세요</option>
                    <option value="STABLE">안정형 (Stable)</option>
                    <option value="NEUTRAL">중립형 (Neutral)</option>
                    <option value="AGGRESSIVE">공격형 (Aggressive)</option>
                  </select>
                  <p class="text-xs text-muted-foreground mt-2">
                    투자 성향에 따라 최적의 ETF 추천을 받을 수 있습니다.
                  </p>
                </div>
              </div>

              <!-- 버튼 영역 -->
              <div class="flex gap-3 pt-6">
                <button
                  type="submit"
                  :disabled="saving || !hasChanges"
                  class="flex-1 flex items-center justify-center gap-2 px-6 py-3.5 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-lg font-semibold hover:from-blue-700 hover:to-blue-800 transition-all duration-200 shadow-md hover:shadow-lg hover:-translate-y-0.5 disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none"
                >
                  <svg v-if="!saving" xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"/><polyline points="17 21 17 13 7 13 7 21"/><polyline points="7 3 7 8 15 8"/></svg>
                  <div v-else class="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                  <span>{{ saving ? '저장 중...' : '변경사항 저장' }}</span>
                </button>

                <button
                  type="button"
                  @click="cancelEdit"
                  :disabled="saving"
                  class="px-6 py-3.5 bg-gray-100 text-gray-700 rounded-lg font-semibold hover:bg-gray-200 transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  취소
                </button>
              </div>
            </form>
          </div>
        </div>

        <!-- 추가 정보 카드 -->
        <div class="mt-8 grid grid-cols-1 md:grid-cols-3 gap-4">
          <!-- 가입일 -->
          <div class="rounded-xl border border-blue-100 bg-gradient-to-br from-blue-50 to-white shadow-sm hover:shadow-md transition-all duration-250 p-5">
            <div class="flex items-center gap-2 text-xs font-semibold text-blue-600 uppercase tracking-wider mb-2">
              <div class="w-8 h-8 rounded-lg bg-blue-100 flex items-center justify-center">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><rect width="18" height="18" x="3" y="4" rx="2" ry="2"/><line x1="16" x2="16" y1="2" y2="6"/><line x1="8" x2="8" y1="2" y2="6"/><line x1="3" x2="21" y1="10" y2="10"/></svg>
              </div>
              가입일
            </div>
            <p class="text-xl font-bold text-gray-900 font-mono">
              {{ formatDate(authStore.user?.createdAt) }}
            </p>
          </div>

          <!-- 투자 성향 -->
          <div class="rounded-xl border border-emerald-100 bg-gradient-to-br from-emerald-50 to-white shadow-sm hover:shadow-md transition-all duration-250 p-5">
            <div class="flex items-center gap-2 text-xs font-semibold text-emerald-600 uppercase tracking-wider mb-2">
              <div class="w-8 h-8 rounded-lg bg-emerald-100 flex items-center justify-center">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M3 3v18h18"/><path d="M18 17V9"/><path d="M13 17V5"/><path d="M8 17v-3"/></svg>
              </div>
              투자 성향
            </div>
            <p class="text-xl font-bold text-gray-900">
              {{ formData.propensity || '-' }}
            </p>
          </div>

          <!-- 로그인 방식 -->
          <div class="rounded-xl border border-slate-200 bg-gradient-to-br from-slate-100 to-white shadow-sm hover:shadow-md transition-all duration-250 p-5">
            <div class="flex items-center gap-2 text-xs font-semibold text-slate-600 uppercase tracking-wider mb-2">
              <div class="w-8 h-8 rounded-lg bg-slate-200 flex items-center justify-center">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M12.22 2h-.44a2 2 0 0 0-2 2v.18a2 2 0 0 1-1 1.73l-.43.25a2 2 0 0 1-2 0l-.15-.08a2 2 0 0 0-2.73.73l-.22.38a2 2 0 0 0 .73 2.73l.15.1a2 2 0 0 1 1 1.72v.51a2 2 0 0 1-1 1.74l-.15.09a2 2 0 0 0-.73 2.73l.22.38a2 2 0 0 0 2.73.73l.15-.08a2 2 0 0 1 2 0l.43.25a2 2 0 0 1 1 1.73V20a2 2 0 0 0 2 2h.44a2 2 0 0 0 2-2v-.18a2 2 0 0 1 1-1.73l.43-.25a2 2 0 0 1 2 0l.15.08a2 2 0 0 0 2.73-.73l.22-.39a2 2 0 0 0-.73-2.73l-.15-.08a2 2 0 0 1-1-1.74v-.5a2 2 0 0 1 1-1.74l.15-.09a2 2 0 0 0 .73-2.73l-.22-.38a2 2 0 0 0-2.73-.73l-.15.08a2 2 0 0 1-2 0l-.43-.25a2 2 0 0 1-1-1.73V4a2 2 0 0 0-2-2z"/><circle cx="12" cy="12" r="3"/></svg>
              </div>
              로그인 방식
            </div>
            <p class="text-xl font-bold text-gray-900">
              카카오
            </p>
          </div>
        </div>

        <!-- 추천 ETF 섹션 -->
        <div class="mt-8 rounded-2xl border border-gray-200 bg-white shadow-lg p-6">
          <!-- 섹션 헤더 -->
          <div class="flex items-center justify-between mb-6">
            <div>
              <h3 class="text-xl font-bold text-gray-900 mb-1">추천 ETF</h3>
              <p class="text-sm text-gray-500">
                회원님의 투자 성향에 맞는 ETF를 추천합니다
              </p>
            </div>
            <button
              v-if="formData.propensity"
              @click="router.push({ name: 'propensityTest' })"
              class="text-sm text-gray-600 hover:text-gray-900 font-semibold transition whitespace-nowrap flex items-center gap-1"
            >
              <span>다시 분석하기</span>
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="m9 18 6-6-6-6"/>
              </svg>
            </button>
          </div>

          <!-- 로딩 상태 -->
          <div v-if="recommendedLoading" class="flex justify-center items-center py-12">
            <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
          </div>

          <!-- 투자 성향 미보유 시 안내 (New) -->
          <div v-else-if="!formData.propensity" class="text-center py-12 px-4">
            <div class="w-20 h-20 mx-auto mb-5 rounded-2xl bg-gradient-to-br from-blue-50 to-sky-50 flex items-center justify-center shadow-inner">
              <svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-blue-500">
                <path d="M3 3v18h18"/><path d="M18 17V9"/><path d="M13 17V5"/><path d="M8 17v-3"/>
              </svg>
            </div>
            <h4 class="text-lg font-bold text-gray-900 mb-2">
              나만의 맞춤형 ETF를 추천받아보세요!
            </h4>
            <p class="text-sm text-gray-600 mb-8 max-w-sm mx-auto leading-relaxed">
              투자 성향을 분석하면 회원님의 목표와 위험 선호도에 딱 맞는<br />
              <span class="font-semibold text-blue-600">최적의 ETF</span>를 추천해 드립니다.
            </p>
            <button
              @click="router.push({ name: 'propensityTest' })"
              class="px-8 py-3.5 bg-gradient-to-r from-blue-600 to-sky-600 text-white rounded-xl font-bold hover:from-blue-700 hover:to-sky-700 transition-all duration-200 shadow-lg hover:shadow-xl hover:-translate-y-0.5 flex items-center gap-2 mx-auto"
            >
              <span>투자 성향 테스트 하러 가기</span>
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                <path d="M5 12h14" />
                <path d="m12 5 7 7-7 7" />
              </svg>
            </button>
          </div>

          <!-- 에러 상태 -->
          <div v-else-if="recommendedError" class="text-center py-8">
            <p class="text-sm text-muted-foreground mb-4">{{ recommendedError }}</p>
            <button
              @click="loadRecommendedEtfs"
              class="text-sm text-primary hover:text-primary/80 font-medium"
            >
              다시 시도
            </button>
          </div>

          <!-- 추천 ETF 목록 (상위 3개) -->
          <div v-else-if="recommendedEtfs.length > 0">
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-6">
              <article
                v-for="etf in recommendedEtfs.slice(0, 3)"
                :key="etf.etfId"
                @click="goToRecommendedEtfDetail(etf.etfId)"
                class="rounded-xl border border-gray-200 bg-gradient-to-br from-white to-slate-50 hover:border-blue-300 hover:-translate-y-1 hover:shadow-xl transition-all duration-250 cursor-pointer overflow-hidden"
              >
                <!-- 카드 헤더 -->
                <header class="px-5 pt-5 pb-4 border-b border-gray-100">
                  <div class="flex items-center justify-between mb-3">
                    <span class="px-2.5 py-1 text-xs font-bold rounded-full bg-blue-100 text-blue-700">
                      {{ etf.country || etf.market }}
                    </span>
                    <span v-if="etf.priority" class="px-2.5 py-1 text-xs font-bold rounded-full bg-emerald-100 text-emerald-700">
                      #{{ etf.priority }}
                    </span>
                  </div>
                  <h4 class="text-lg font-bold text-gray-900 mb-2 line-clamp-2 leading-tight">
                    {{ etf.etfName }}
                  </h4>
                  <p class="text-xs font-mono text-gray-500 font-semibold tracking-tight">{{ etf.etfCode }}</p>
                </header>

                <!-- 카드 본문 -->
                <section class="px-5 py-4 space-y-3">
                  <div class="grid grid-cols-2 gap-3">
                    <div class="bg-white/80 rounded-lg p-2.5">
                      <div class="text-xs text-gray-400 font-semibold mb-1">총보수</div>
                      <div class="text-base font-bold text-gray-900 font-mono">{{ etf.expenseRatio }}%</div>
                    </div>
                    <div class="bg-white/80 rounded-lg p-2.5">
                      <div class="text-xs text-gray-400 font-semibold mb-1">시가총액</div>
                      <div class="text-base font-bold text-gray-900 font-mono">{{ formatAum(etf.netAsset) }}</div>
                    </div>
                  </div>
                  
                  <div v-if="etf.returnRate1y !== null" class="flex items-center justify-between pt-2 border-t border-gray-100">
                    <span class="text-xs text-gray-500 font-semibold">1년 수익률</span>
                    <span
                      :class="[
                        'px-3 py-1 text-sm font-bold font-mono rounded-full',
                        etf.returnRate1y > 0 ? 'bg-emerald-100 text-emerald-700' : etf.returnRate1y < 0 ? 'bg-red-100 text-red-700' : 'bg-gray-100 text-gray-700'
                      ]"
                    >
                      {{ etf.returnRate1y > 0 ? '+' : '' }}{{ etf.returnRate1y }}%
                    </span>
                  </div>
                </section>

                <!-- 카드 푸터 -->
                <footer class="px-5 py-3 bg-slate-50 border-t border-gray-100">
                  <button class="w-full text-sm font-semibold text-blue-600 hover:text-blue-700 transition flex items-center justify-center gap-1.5">
                    <span>자세히 보기</span>
                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                      <path d="m9 18 6-6-6-6"/>
                    </svg>
                  </button>
                </footer>
              </article>
            </div>

            <!-- 하단 액션 버튼 -->
            <div class="flex gap-3">
              <button
                @click="router.push({ name: 'recommend' })"
                class="flex-1 px-4 py-3.5 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-lg font-semibold hover:from-blue-700 hover:to-blue-800 transition-all duration-200 shadow-md hover:shadow-lg hover:-translate-y-0.5 flex items-center justify-center gap-2"
              >
                <span>전체 추천 ETF 보기</span>
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="m9 18 6-6-6-6"/>
                </svg>
              </button>
              <button
                @click="router.push({ name: 'propensityTest' })"
                class="px-6 py-3.5 bg-gray-100 text-gray-700 rounded-lg font-semibold hover:bg-gray-200 transition-all duration-200 whitespace-nowrap"
              >
                다시 분석하기
              </button>
            </div>
          </div>

          <!-- 빈 상태 -->
          <div v-else class="text-center py-16">
            <div class="w-20 h-20 mx-auto mb-5 rounded-2xl bg-gradient-to-br from-slate-100 to-slate-200 flex items-center justify-center shadow-inner">
              <svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-slate-400">
                <path d="M3 3v18h18"/>
                <path d="m19 9-5 5-4-4-3 3"/>
              </svg>
            </div>
            <p class="text-base font-bold text-gray-900 mb-2">추천 가능한 ETF가 없습니다</p>
            <p class="text-sm text-gray-500 mb-6">투자 성향 테스트를 먼저 진행해주세요</p>
            <button
              @click="router.push({ name: 'propensityTest' })"
              class="px-6 py-3 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-lg font-semibold hover:from-blue-700 hover:to-blue-800 transition-all duration-200 shadow-md hover:shadow-lg text-sm"
            >
              투자 성향 분석하기
            </button>
          </div>
        </div>

        <!-- 계정 삭제 섹션 -->
        <div class="mt-8 rounded-xl border border-red-200 bg-red-50 shadow-inner p-6">
          <div class="flex items-center justify-between">
            <!-- 왼쪽: 제목 + 설명 -->
            <div>
              <h3 class="text-base font-bold text-gray-900 mb-1">계정 삭제</h3>
              <p class="text-sm text-gray-600">
                회원 탈퇴 시 모든 데이터가 삭제되며 복구할 수 없습니다.
              </p>
            </div>
            
            <!-- 오른쪽: 탈퇴 버튼 -->
            <button
              @click="showWithdrawalModal = true"
              class="px-5 py-2.5 bg-red-600 text-white text-sm rounded-lg font-semibold hover:bg-red-700 transition-all duration-200 shadow-md hover:shadow-lg whitespace-nowrap"
            >
              회원 탈퇴
            </button>
          </div>
        </div>
      </div>
    </main>

    <!-- 회원 탈퇴 확인 모달 -->
    <Teleport to="body">
      <Transition
        enter-active-class="transition duration-200 ease-out"
        enter-from-class="opacity-0"
        enter-to-class="opacity-100"
        leave-active-class="transition duration-150 ease-in"
        leave-from-class="opacity-100"
        leave-to-class="opacity-0"
      >
        <div
          v-if="showWithdrawalModal"
          class="fixed inset-0 bg-black/50 flex items-center justify-center z-[100] p-4"
          @click.self="closeWithdrawalModal"
        >
          <div class="bg-white rounded-2xl shadow-2xl max-w-md w-full p-6 space-y-5 scale-100 transition-transform">
            <!-- 모달 헤더 -->
            <div class="flex items-start gap-4">
              <div class="w-12 h-12 rounded-full bg-red-100 flex items-center justify-center flex-shrink-0">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-red-600">
                  <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
                  <line x1="12" y1="9" x2="12" y2="13"/>
                  <line x1="12" y1="17" x2="12.01" y2="17"/>
                </svg>
              </div>
              <div class="flex-1">
                <h2 class="text-xl font-bold text-gray-900">정말 탈퇴하시겠어요?</h2>
                <p class="text-sm text-gray-600 mt-1">
                  탈퇴하시면 아래의 모든 데이터가 영구적으로 삭제됩니다.
                </p>
              </div>
            </div>

            <!-- 경고 내용 -->
            <div class="bg-gray-50 rounded-lg p-4 space-y-2">
              <ul class="space-y-2 text-sm text-gray-700">
                <li class="flex items-start gap-2">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-red-500 flex-shrink-0 mt-0.5">
                    <circle cx="12" cy="12" r="10"/>
                    <line x1="15" y1="9" x2="9" y2="15"/>
                    <line x1="9" y1="9" x2="15" y2="15"/>
                  </svg>
                  <span>저장된 모든 포트폴리오</span>
                </li>
                <li class="flex items-start gap-2">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-red-500 flex-shrink-0 mt-0.5">
                    <circle cx="12" cy="12" r="10"/>
                    <line x1="15" y1="9" x2="9" y2="15"/>
                    <line x1="9" y1="9" x2="15" y2="15"/>
                  </svg>
                  <span>관심 ETF 북마크</span>
                </li>
                <li class="flex items-start gap-2">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-red-500 flex-shrink-0 mt-0.5">
                    <circle cx="12" cy="12" r="10"/>
                    <line x1="15" y1="9" x2="9" y2="15"/>
                    <line x1="9" y1="9" x2="15" y2="15"/>
                  </svg>
                  <span>투자 성향 및 추천 기록</span>
                </li>
                <li class="flex items-start gap-2">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-red-500 flex-shrink-0 mt-0.5">
                    <circle cx="12" cy="12" r="10"/>
                    <line x1="15" y1="9" x2="9" y2="15"/>
                    <line x1="9" y1="9" x2="15" y2="15"/>
                  </svg>
                  <span>작성한 댓글 및 활동 기록</span>
                </li>
              </ul>
            </div>

            <!-- 확인 체크박스 -->
            <label class="flex items-start gap-3 cursor-pointer">
              <input
                v-model="withdrawalConfirmed"
                type="checkbox"
                class="mt-1 w-4 h-4 text-red-600 bg-gray-100 border-gray-300 rounded focus:ring-red-500 focus:ring-2 cursor-pointer"
              />
              <span class="text-sm text-gray-700 select-none">
                위 내용을 확인했으며, 모든 데이터가 삭제되는 것에 동의합니다.
              </span>
            </label>

            <!-- 버튼 영역 -->
            <div class="flex gap-3 pt-2">
              <button
                @click="closeWithdrawalModal"
                :disabled="isWithdrawing"
                class="flex-1 px-4 py-3 bg-gray-100 text-gray-700 rounded-lg font-semibold hover:bg-gray-200 transition disabled:opacity-50 disabled:cursor-not-allowed"
              >
                취소
              </button>
              <button
                @click="handleWithdrawal"
                :disabled="!withdrawalConfirmed || isWithdrawing"
                class="flex-1 px-4 py-3 bg-red-600 text-white rounded-lg font-semibold hover:bg-red-700 transition disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
              >
                <div v-if="isWithdrawing" class="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
                <span>{{ isWithdrawing ? '처리 중...' : '탈퇴하기' }}</span>
              </button>
            </div>
          </div>
        </div>
      </Transition>
    </Teleport>
  </div>
</template>

<script setup>
import { deleteUser, getMyRecommendedEtfs, updateMyInfo } from '@/api/user'
import { useAuthStore } from '@/stores/auth'
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()
const route = useRoute()

const loading = ref(true)
const saving = ref(false)

// View/Edit 모드 관리
const isEditMode = ref(false)

// 회원 탈퇴 관련 상태
const showWithdrawalModal = ref(false)
const withdrawalConfirmed = ref(false)
const isWithdrawing = ref(false)

// 추천 ETF 관련 상태
const recommendedEtfs = ref([])
const recommendedLoading = ref(false)
const recommendedError = ref(null)

// 현재 폼 데이터
const formData = reactive({
  nickname: '',
  email: '',
  age: null,
  propensity: ''
})

// 원본 데이터 스냅샷 (변경 감지용)
const originalData = reactive({
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

// 변경사항 감지
const hasChanges = computed(() => {
  return (
    formData.nickname !== originalData.nickname ||
    formData.age !== originalData.age ||
    formData.propensity !== originalData.propensity
  )
})

// 폼 초기화 (데이터 로드 시)
const resetForm = () => {
  if (authStore.user) {
    formData.nickname = authStore.user.nickname || ''
    formData.email = authStore.user.email || ''
    formData.age = authStore.user.age || null
    formData.propensity = authStore.user.propensity || ''
    
    // originalData도 업데이트
    originalData.nickname = authStore.user.nickname || ''
    originalData.email = authStore.user.email || ''
    originalData.age = authStore.user.age || null
    originalData.propensity = authStore.user.propensity || ''
  }
}

// Edit 모드 진입
const enterEditMode = () => {
  // 현재 데이터를 originalData에 스냅샷 저장
  originalData.nickname = formData.nickname
  originalData.email = formData.email
  originalData.age = formData.age
  originalData.propensity = formData.propensity
  
  isEditMode.value = true
  console.log('[Edit 모드 진입] originalData 스냅샷:', { ...originalData })
}

// Edit 모드 취소
const cancelEdit = () => {
  // originalData로 복원
  formData.nickname = originalData.nickname
  formData.email = originalData.email
  formData.age = originalData.age
  formData.propensity = originalData.propensity
  
  isEditMode.value = false
  console.log('[Edit 모드 취소] 데이터 복원됨')
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

// 폼 제출 (변경된 필드만 PATCH)
const handleSubmit = async () => {
  // 변경사항 없으면 API 호출하지 않음
  if (!hasChanges.value) {
    alert('변경된 내용이 없습니다.')
    return
  }

  saving.value = true
  try {
    // 변경된 필드만 포함하는 PATCH payload 생성
    const patchPayload = {}
    
    if (formData.nickname !== originalData.nickname && formData.nickname.trim() !== '') {
      patchPayload.nickname = formData.nickname
    }
    
    if (formData.age !== originalData.age && formData.age !== null) {
      patchPayload.age = formData.age
    }
    
    if (formData.propensity !== originalData.propensity && formData.propensity !== null) {
      patchPayload.propensity = formData.propensity
    }
    
    // ⚠️ email은 절대 포함하지 않음 (백엔드 정책)
    
    console.log('[PATCH Payload] 변경된 필드만 전송:', patchPayload)
    
    // PATCH 요청
    const response = await updateMyInfo(patchPayload)
    
    if (response.status === 200) {
      // 최신 사용자 정보 다시 가져오기
      await authStore.getMyInfo()
      
      // formData 및 originalData 업데이트
      resetForm()
      
      // View 모드로 전환
      isEditMode.value = false
      
      alert('회원 정보가 성공적으로 업데이트되었습니다.')
      console.log('[업데이트 성공] View 모드로 전환')
    }
  } catch (error) {
    console.error('정보 업데이트 실패:', error)
    alert(error.response?.data || '정보 업데이트에 실패했습니다.')
  } finally {
    saving.value = false
  }
}

// 회원 탈퇴 모달 닫기
const closeWithdrawalModal = () => {
  if (!isWithdrawing.value) {
    showWithdrawalModal.value = false
    withdrawalConfirmed.value = false
    document.body.style.overflow = '' // Restore scroll
  }
}

// 회원 탈퇴 모달 열기 (template @click을 이걸로 교체해야 하지만, template 수정이 이미 큼. Watcher로 처리하는게 안전)
import { watch } from 'vue'
watch(showWithdrawalModal, (newVal) => {
  if (newVal) {
    document.body.style.overflow = 'hidden'
  } else {
    document.body.style.overflow = ''
  }
})

// 추천 ETF 조회
// 추천 ETF 조회
const loadRecommendedEtfs = async () => {
  recommendedLoading.value = true
  recommendedError.value = null

  try {
    const response = await getMyRecommendedEtfs()

    // 1) 응답에서 "배열"을 먼저 뽑아내기
    const rawList = Array.isArray(response.data)
      ? response.data
      : (response.data?.recommendedETFs ?? [])

    // 2) 백엔드 필드명 -> 프론트에서 쓰는 필드명으로 매핑
    recommendedEtfs.value = rawList.map((e) => ({
      ...e,
      etfId: e.etfId ?? e.etfid ?? e.etf_id ?? e.id ?? e.etfID ?? null,
      expenseRatio: e.expenseRatio ?? e.fee ?? null,
      netAsset: e.netAsset ?? e.aum ?? null,
      returnRate1y: e.returnRate1y ?? e.return1yr ?? e.return1Y ?? null
    }))

    console.log('추천 ETF 조회 성공:', recommendedEtfs.value)
  } catch (error) {
    console.error('추천 ETF 조회 실패:', error)

    if (error.response?.status === 404) {
      recommendedError.value = '아직 투자 성향 테스트를 진행하지 않았습니다.'
    } else if (error.response?.status === 401) {
      recommendedError.value = '로그인이 필요합니다.'
    } else {
      recommendedError.value = '추천 ETF를 불러올 수 없습니다.'
    }

    recommendedEtfs.value = []
  } finally {
    recommendedLoading.value = false
  }
}


// 추천 ETF 상세로 이동 (from/returnTo 전달)
const goToRecommendedEtfDetail = (etfId) => {
  console.log('[추천→상세] etfId =', etfId) // 디버깅용 로그
  router.push({
    name: 'etfDetail',
    params: { etfId },
    query: {
      from: 'recommended',
      returnTo: route.fullPath
    }
  })
}

// AUM 포맷팅 함수
// AUM 포맷팅 함수 (단위: 원)
const formatAum = (aum) => {
  if (!aum) return '-'
  
  // 조 단위 계산 (1조 = 1,000,000,000,000원 = 10,000억원)
  const trillion = Math.floor(aum / 1000000000000)
  const billion = Math.round((aum % 1000000000000) / 100000000)
  
  if (trillion > 0 && billion > 0) {
    return `${trillion.toLocaleString()}조 ${billion.toLocaleString()}억원`
  } else if (trillion > 0) {
    return `${trillion.toLocaleString()}조원`
  } else if (billion > 0) {
    return `${billion.toLocaleString()}억원`
  } else {
    return `${aum.toLocaleString()}원`
  }
}

// 회원 탈퇴 처리
const handleWithdrawal = async () => {
  if (!withdrawalConfirmed.value) {
    return
  }

  isWithdrawing.value = true

  try {
    // 1. 회원 탈퇴 API 호출
    const response = await deleteUser()
    
    // 2. 성공 시
    if (response.status === 200) {
      alert('회원 탈퇴가 완료되었습니다.')
      
      // 3. 토큰 및 사용자 정보 제거 (기존 logout 로직 활용)
      authStore.logout()
      
      // 4. 로그인 페이지로 리다이렉트
      router.push('/login')
    }
  } catch (error) {
    console.error('회원 탈퇴 실패:', error)
    
    // 에러 처리
    if (error.response?.status === 401) {
      alert('로그인이 만료되었습니다. 다시 로그인해주세요.')
      authStore.logout()
      router.push('/login')
    } else if (error.message === 'Network Error' || !error.response) {
      alert('네트워크 연결을 확인해주세요.')
    } else {
      alert('회원 탈퇴에 실패했습니다. 잠시 후 다시 시도해주세요.')
    }
  } finally {
    isWithdrawing.value = false
    closeWithdrawalModal()
  }
}

// 초기 로드
onMounted(() => {
  loadUserData()
  loadRecommendedEtfs()
})
</script>

<style scoped>
/* Tailwind로 처리 */
</style>