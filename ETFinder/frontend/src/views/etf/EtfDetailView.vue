<template>
  <div class="flex min-h-screen bg-background">
    <main class="flex-1 p-6 lg:p-8">
      <div class="container">
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

        <!-- ETF 상세 -->
        <div v-else-if="etf">
          <!-- 헤더 -->
          <header class="mb-6">
            <button
              @click="$router.push('/etfs')"
              class="flex items-center gap-2 text-sm text-muted-foreground hover:text-foreground mb-4 transition-all duration-200"
            >
              <span>←</span>
              <span>목록으로 돌아가기</span>
            </button>

            <div class="flex items-start justify-between mb-3">
              <div class="flex-1">
                <div class="flex items-center gap-2 mb-2">
                  <span class="text-sm font-mono text-muted-foreground">{{ etf.etfCode }}</span>
                  <span class="px-2 py-1 text-xs font-medium rounded bg-primary/10 text-primary">
                    {{ etf.market }}
                  </span>
                </div>
                <h1 class="text-3xl font-bold tracking-tight text-foreground mb-2">
                  {{ etf.etfName }}
                </h1>
                <p v-if="etf.description" class="text-sm text-muted-foreground">
                  {{ etf.description }}
                </p>
              </div>

              <!-- 액션 버튼 -->
              <div class="flex items-center gap-2">
                <!-- 좋아요 수 표시 -->
                <div class="flex items-center gap-1 px-3 py-2 rounded-lg border border-border bg-muted/30">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="currentColor" class="text-destructive">
                    <path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z"/>
                  </svg>
                  <span class="text-sm font-medium text-foreground">{{ etf.likeCount || 0 }}</span>
                </div>

                <!-- 공유 버튼 -->
                <button 
                  @click="handleShare"
                  class="px-4 py-2 rounded-lg border border-input bg-background hover:bg-accent hover:text-accent-foreground transition-all duration-200 flex items-center gap-2"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 12v8a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-8"/><polyline points="16 6 12 2 8 6"/><line x1="12" x2="12" y1="2" y2="15"/></svg>
                  <span class="text-sm font-medium">공유</span>
                </button>

                <!-- 찜하기 버튼 -->
                <button 
                  @click="handleToggleLike"
                  :disabled="likeLoading"
                  :class="[
                    'px-4 py-2 rounded-lg border transition-all duration-200 flex items-center gap-2',
                    etf.likedByMe 
                      ? 'border-destructive bg-destructive text-destructive-foreground hover:bg-destructive/90'
                      : 'border-input bg-background hover:bg-accent hover:text-accent-foreground'
                  ]"
                >
                  <svg 
                    xmlns="http://www.w3.org/2000/svg" 
                    width="18" 
                    height="18" 
                    viewBox="0 0 24 24" 
                    :fill="etf.likedByMe ? 'currentColor' : 'none'" 
                    stroke="currentColor" 
                    stroke-width="2" 
                    stroke-linecap="round" 
                    stroke-linejoin="round"
                  >
                    <path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z"/>
                  </svg>
                  <span class="text-sm font-medium">{{ etf.likedByMe ? '찜 해제' : '찜하기' }}</span>
                </button>

                <!-- 포트폴리오 담기 버튼 -->
                <button 
                  @click="handleToggleBookmark"
                  :disabled="bookmarkLoading"
                  :class="[
                    'px-6 py-2 rounded-lg transition-all duration-200 font-medium',
                    isBookmarked
                      ? 'bg-muted border border-primary text-primary hover:bg-primary/10'
                      : 'bg-primary text-primary-foreground hover:bg-primary/90'
                  ]"
                >
                  {{ isBookmarked ? '포트폴리오에 있음' : '포트폴리오 담기' }}
                </button>
              </div>
            </div>
          </header>

          <!-- 메인 정보 카드 -->
          <div class="rounded-xl border border-border bg-card shadow-sm mb-6">
            <div class="p-6">
              <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
                <!-- 현재가 -->
                <div>
                  <div class="text-sm text-muted-foreground mb-1">현재가</div>
                  <div class="text-3xl font-mono font-bold text-foreground mb-1">
                    {{ formatPrice(etf.currentPrice) }}원
                  </div>
                  <div
                    v-if="etf.return1mo !== null"
                    :class="[
                      'text-sm font-mono font-medium flex items-center gap-1',
                      etf.return1mo > 0 ? 'text-chart-1' : etf.return1mo < 0 ? 'text-destructive' : 'text-chart-2'
                    ]"
                  >
                    <span>{{ etf.return1mo > 0 ? '↗' : etf.return1mo < 0 ? '↘' : '→' }}</span>
                    <span>{{ etf.return1mo > 0 ? '+' : '' }}{{ etf.return1mo }}% (360원)</span>
                  </div>
                </div>

                <!-- 시가총액 -->
                <div>
                  <div class="text-sm text-muted-foreground mb-1">시가총액</div>
                  <div class="text-xl font-mono font-semibold text-foreground">
                    {{ formatAumDetail(etf.aum) }}
                  </div>
                </div>

                <!-- 총보수 -->
                <div>
                  <div class="text-sm text-muted-foreground mb-1">총보수</div>
                  <div class="text-xl font-mono font-semibold text-foreground">
                    {{ etf.fee }}%
                  </div>
                </div>

                <!-- 분배금 -->
                <div>
                  <div class="text-sm text-muted-foreground mb-1">분배금</div>
                  <div class="text-xl font-mono font-semibold text-foreground">
                    분기지급
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 탭 영역 -->
          <div class="rounded-xl border border-border bg-card shadow-sm">
            <!-- 탭 헤더 -->
            <div class="border-b border-border">
              <nav class="flex gap-1 px-2" aria-label="Tabs">
                <button
                  v-for="tab in tabs"
                  :key="tab.id"
                  @click="activeTab = tab.id"
                  :class="[
                    'px-4 py-3 text-sm font-medium transition-all duration-200 rounded-t-lg',
                    activeTab === tab.id
                      ? 'text-primary border-b-2 border-primary'
                      : 'text-muted-foreground hover:text-foreground'
                  ]"
                >
                  {{ tab.label }}
                </button>
              </nav>
            </div>

            <!-- 탭 컨텐츠 -->
            <div class="p-6">
              <!-- 차트/수익률 탭 -->
              <div v-if="activeTab === 'chart'">
                <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
                  <!-- 차트 영역 -->
                  <div class="lg:col-span-2">
                    <h3 class="text-lg font-semibold text-foreground mb-4">수익률 추이</h3>
                    <div class="rounded-lg border border-border bg-muted/30 h-80 flex items-center justify-center">
                      <p class="text-muted-foreground text-sm">차트 영역 (추후 구현)</p>
                    </div>
                  </div>

                  <!-- AI 견해 -->
                  <div class="rounded-lg border border-border bg-accent/50 p-4">
                    <div class="flex items-center gap-2 mb-3">
                      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 8V4H8"/><rect width="16" height="12" x="4" y="8" rx="2"/><path d="M2 14h2"/><path d="M20 14h2"/><path d="M15 13v2"/><path d="M9 13v2"/></svg>
                      <h4 class="font-semibold text-foreground">AI 견해</h4>
                    </div>
                    <p class="text-sm text-muted-foreground mb-4">
                      이 ETF는 기술주 중심의 공격적인 투자를 선호하는 투자자에게 적합합니다. 최근 AI 산업 성장과 반의 높은 수익률 기대감이 있으며, 변동성도 다소 높은 편입니다.
                    </p>
                    <div class="space-y-2">
                      <div class="flex justify-between text-sm">
                        <span class="text-muted-foreground">변동성</span>
                        <span class="font-medium text-destructive">높음</span>
                      </div>
                      <div class="flex justify-between text-sm">
                        <span class="text-muted-foreground">성장성</span>
                        <span class="font-medium text-chart-1">매우 높음</span>
                      </div>
                      <div class="flex justify-between text-sm">
                        <span class="text-muted-foreground">배당수익</span>
                        <span class="font-medium text-chart-2">낮음</span>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 수익률 그리드 -->
                <div class="mt-6 grid grid-cols-2 md:grid-cols-5 gap-4">
                  <div v-if="etf.return1mo !== null" class="rounded-lg border border-border bg-muted/30 p-4 text-center">
                    <div class="text-sm text-muted-foreground mb-2">1개월</div>
                    <div
                      :class="[
                        'text-xl font-mono font-bold',
                        etf.return1mo >= 0 ? 'text-chart-1' : 'text-destructive'
                      ]"
                    >
                      {{ etf.return1mo }}%
                    </div>
                  </div>
                  <div v-if="etf.return3mo !== null" class="rounded-lg border border-border bg-muted/30 p-4 text-center">
                    <div class="text-sm text-muted-foreground mb-2">3개월</div>
                    <div
                      :class="[
                        'text-xl font-mono font-bold',
                        etf.return3mo >= 0 ? 'text-chart-1' : 'text-destructive'
                      ]"
                    >
                      {{ etf.return3mo }}%
                    </div>
                  </div>
                  <div v-if="etf.return6mo !== null" class="rounded-lg border border-border bg-muted/30 p-4 text-center">
                    <div class="text-sm text-muted-foreground mb-2">6개월</div>
                    <div
                      :class="[
                        'text-xl font-mono font-bold',
                        etf.return6mo >= 0 ? 'text-chart-1' : 'text-destructive'
                      ]"
                    >
                      {{ etf.return6mo }}%
                    </div>
                  </div>
                  <div v-if="etf.return1yr !== null" class="rounded-lg border border-border bg-muted/30 p-4 text-center">
                    <div class="text-sm text-muted-foreground mb-2">1년</div>
                    <div
                      :class="[
                        'text-xl font-mono font-bold',
                        etf.return1yr >= 0 ? 'text-chart-1' : 'text-destructive'
                      ]"
                    >
                      {{ etf.return1yr }}%
                    </div>
                  </div>
                  <div v-if="etf.return3yr !== null" class="rounded-lg border border-border bg-muted/30 p-4 text-center">
                    <div class="text-sm text-muted-foreground mb-2">3년</div>
                    <div
                      :class="[
                        'text-xl font-mono font-bold',
                        etf.return3yr >= 0 ? 'text-chart-1' : 'text-destructive'
                      ]"
                    >
                      {{ etf.return3yr }}%
                    </div>
                  </div>
                </div>
              </div>

              <!-- 종목 정보 탭 -->
              <div v-if="activeTab === 'info'">
                <h3 class="text-lg font-semibold text-foreground mb-4">기본 정보</h3>
                <div class="space-y-3">
                  <div class="flex justify-between py-3 border-b border-border">
                    <span class="text-sm text-muted-foreground">ETF 코드</span>
                    <span class="text-sm font-mono font-medium text-foreground">{{ etf.etfCode }}</span>
                  </div>
                  <div class="flex justify-between py-3 border-b border-border">
                    <span class="text-sm text-muted-foreground">ETF 명</span>
                    <span class="text-sm font-medium text-foreground">{{ etf.etfName }}</span>
                  </div>
                  <div class="flex justify-between py-3 border-b border-border">
                    <span class="text-sm text-muted-foreground">시장</span>
                    <span class="text-sm font-medium text-foreground">{{ etf.market }}</span>
                  </div>
                  <div class="flex justify-between py-3 border-b border-border">
                    <span class="text-sm text-muted-foreground">테마</span>
                    <span class="text-sm font-medium text-foreground">{{ etf.theme || '-' }}</span>
                  </div>
                  <div class="flex justify-between py-3 border-b border-border">
                    <span class="text-sm text-muted-foreground">위험등급</span>
                    <span class="text-sm font-medium text-foreground">{{ etf.riskRating ? `${etf.riskRating}등급` : '-' }}</span>
                  </div>
                  <div class="flex justify-between py-3 border-b border-border">
                    <span class="text-sm text-muted-foreground">총보수 (연)</span>
                    <span class="text-sm font-mono font-medium text-foreground">{{ etf.fee }}%</span>
                  </div>
                  <div class="flex justify-between py-3 border-b border-border">
                    <span class="text-sm text-muted-foreground">시가총액</span>
                    <span class="text-sm font-mono font-medium text-foreground">{{ formatAumDetail(etf.aum) }}</span>
                  </div>
                </div>
              </div>

              <!-- 구성 종목 탭 -->
              <div v-if="activeTab === 'holdings'">
                <h3 class="text-lg font-semibold text-foreground mb-4">보유 종목 비중</h3>
                <p class="text-sm text-muted-foreground text-center py-8">
                  보유 종목 정보는 준비 중입니다.
                </p>
              </div>

              <!-- 한줄평 탭 -->
              <div v-if="activeTab === 'comments'">
                <h3 class="text-lg font-semibold text-foreground mb-6">투자자 한줄평</h3>
                
                <!-- AI 댓글 분석 섹션 -->
                <div v-if="!commentsLoading" class="rounded-xl border border-border bg-card shadow-sm p-8 mb-6">
                  <!-- 헤더 -->
                  <div class="flex items-center justify-between mb-8">
                    <div class="flex items-center gap-3">
                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-primary">
                        <path d="M12 8V4H8"/>
                        <rect width="16" height="12" x="4" y="8" rx="2"/>
                        <path d="M2 14h2"/>
                        <path d="M20 14h2"/>
                        <path d="M15 13v2"/>
                        <path d="M9 13v2"/>
                      </svg>
                      <h4 class="text-lg font-bold text-foreground">AI 댓글 분석</h4>
                    </div>
                    <button class="px-4 py-1.5 text-xs font-semibold rounded-md bg-blue-100 text-blue-700 hover:bg-blue-200 transition-colors">
                      최신 분석
                    </button>
                  </div>

                  <!-- 데이터 부족 상태 -->
                  <div v-if="!shouldShowChart" class="text-center py-12">
                    <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" class="mx-auto mb-4 text-muted-foreground/30">
                      <circle cx="12" cy="12" r="10"/>
                      <path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"/>
                      <path d="M12 17h.01"/>
                    </svg>
                    <p class="text-base font-medium text-muted-foreground mb-2">데이터 부족</p>
                    <p class="text-sm text-muted-foreground mb-6">댓글이 5개 이상일 때 AI 분석이 표시됩니다</p>
                    
                    <!-- moodMessage는 항상 표시 -->
                    <div v-if="aiAnalysis.moodMessage" class="mt-6 p-4 rounded-lg bg-amber-50 border border-amber-200 max-w-md mx-auto">
                      <div class="flex items-start gap-3">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-amber-600 mt-0.5 flex-shrink-0">
                          <path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"/>
                          <path d="M12 9v4"/>
                          <path d="M12 17h.01"/>
                        </svg>
                        <p class="text-sm text-amber-800 leading-relaxed">
                          {{ aiAnalysis.moodMessage }}
                        </p>
                      </div>
                    </div>
                  </div>

                  <!-- 분석 결과 (totalCnt >= 5) -->
                  <div v-else class="grid grid-cols-1 lg:grid-cols-2 gap-8">
                    <!-- 왼쪽: 전체 감정 분석 -->
                    <div class="space-y-6">
                      <h5 class="text-sm font-medium text-muted-foreground">전체 감정 분석</h5>
                      
                      <!-- 바 차트 (2행 구조, 100% 겹침 방지) -->
                      <div class="space-y-2">
                        <!-- Row A: 라벨 행 (각 세그먼트 너비와 동일) -->
                        <div class="flex gap-0">
                          <!-- 긍정 라벨 영역 -->
                          <div 
                            class="flex justify-start items-end pb-1 transition-all duration-500"
                            :style="{ width: aiAnalysis.positivePercent + '%' }"
                          >
                            <div 
                              v-if="aiAnalysis.positivePercent >= 8"
                              class="text-3xl sm:text-4xl font-bold text-green-600 whitespace-nowrap pl-1"
                            >
                              {{ aiAnalysis.positivePercent }}%
                            </div>
                          </div>
                          
                          <!-- 중립 라벨 영역 -->
                          <div 
                            class="flex justify-start items-end pb-1 transition-all duration-500"
                            :style="{ width: neutralPercent + '%' }"
                          >
                            <div 
                              v-if="neutralPercent >= 8"
                              class="text-3xl sm:text-4xl font-bold text-gray-500 whitespace-nowrap pl-1"
                            >
                              {{ neutralPercent }}%
                            </div>
                          </div>
                          
                          <!-- 부정 라벨 영역 -->
                          <div 
                            class="flex justify-start items-end pb-1 transition-all duration-500"
                            :style="{ width: aiAnalysis.negativePercent + '%' }"
                          >
                            <div 
                              v-if="aiAnalysis.negativePercent >= 8"
                              class="text-3xl sm:text-4xl font-bold text-red-600 whitespace-nowrap pl-1"
                            >
                              {{ aiAnalysis.negativePercent }}%
                            </div>
                          </div>
                        </div>
                        
                        <!-- Row B: 바 행 (단일 스택 바) -->
                        <div class="h-3 rounded-full overflow-hidden shadow-inner flex">
                          <div 
                            class="bg-green-500 transition-all duration-500"
                            :style="{ width: aiAnalysis.positivePercent + '%' }"
                          ></div>
                          <div 
                            class="bg-gray-400 transition-all duration-500"
                            :style="{ width: neutralPercent + '%' }"
                          ></div>
                          <div 
                            class="bg-red-500 transition-all duration-500"
                            :style="{ width: aiAnalysis.negativePercent + '%' }"
                          ></div>
                        </div>
                        
                        <!-- 하단 범례 (모든 퍼센트 항상 표시) -->
                        <div class="flex items-center justify-between text-xs text-gray-500 pt-1">
                          <span>긍정 ({{ aiAnalysis.positivePercent }}%)</span>
                          <span>중립 ({{ neutralPercent }}%)</span>
                          <span>부정 ({{ aiAnalysis.negativePercent }}%)</span>
                        </div>
                      </div>

                      <!-- 통계 카드 3개 -->
                      <div class="grid grid-cols-3 gap-4 pt-4">
                        <div class="text-center p-4 rounded-lg bg-blue-50 border border-blue-100">
                          <div class="text-3xl font-bold text-blue-600 mb-1">{{ aiAnalysis.totalCnt }}</div>
                          <div class="text-xs text-blue-700 font-medium">분석된 댓글</div>
                        </div>
                        <div class="text-center p-4 rounded-lg bg-green-50 border border-green-100">
                          <div class="text-3xl font-bold text-green-600 mb-1">{{ positiveCount }}</div>
                          <div class="text-xs text-green-700 font-medium">긍정 댓글</div>
                        </div>
                        <div class="text-center p-4 rounded-lg bg-red-50 border border-red-100">
                          <div class="text-3xl font-bold text-red-600 mb-1">{{ negativeCount }}</div>
                          <div class="text-xs text-red-700 font-medium">부정 댓글</div>
                        </div>
                      </div>
                    </div>

                    <!-- 오른쪽: AI 인사이트 카드 (variant 기반) -->
                    <div class="flex items-center justify-center">
                      <div 
                        :class="[
                          'w-full rounded-xl border p-6 shadow-sm',
                          'flex flex-col items-center justify-center text-center gap-3',
                          variantConfig.bgClass,
                          variantConfig.borderClass
                        ]"
                      >
                        <!-- 상단 배지 -->
                        <div 
                          :class="[
                            'px-3 py-1 rounded-full text-xs font-semibold',
                            variantConfig.badgeBg,
                            variantConfig.badgeText
                          ]"
                        >
                          AI 인사이트
                        </div>

                        <!-- 아이콘 -->
                        <component 
                          :is="variantConfig.icon" 
                          :size="48" 
                          :class="variantConfig.iconColor"
                          :stroke-width="2"
                        />

                        <!-- 타이틀 (헤드라인) -->
                        <h3 :class="['text-xl font-bold', variantConfig.textColor]">
                          {{ variantConfig.headline }}
                        </h3>

                        <!-- 메시지 본문 -->
                        <p :class="['text-sm leading-relaxed', variantConfig.textColor, 'opacity-90']">
                          {{ aiAnalysis.moodMessage || '댓글 데이터를 분석 중입니다...' }}
                        </p>

                        <!-- 푸터 (표본 개수) - totalCnt >= 5일 때만 표시 -->
                        <div 
                          v-if="shouldShowChart" 
                          :class="['text-xs mt-2', variantConfig.textColor, 'opacity-60']"
                        >
                          표본 {{ aiAnalysis.totalCnt }}개 기준
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                
                <!-- 한줄평 작성 폼 -->
                <div class="rounded-lg border border-border bg-muted/30 p-4 mb-6">
                  <textarea
                    v-model="newCommentContent"
                    :disabled="!authStore.isAuthenticated || commentSubmitting"
                    :placeholder="authStore.isAuthenticated ? '이 ETF에 대한 의견을 남겨주세요' : '로그인이 필요합니다'"
                    class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-primary resize-none disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200"
                    rows="3"
                    @keydown.ctrl.enter="handleSubmitComment"
                    @keydown.meta.enter="handleSubmitComment"
                  ></textarea>
                  <div class="flex justify-between items-center mt-2">
                    <p v-if="!authStore.isAuthenticated" class="text-xs text-muted-foreground">
                      💡 한줄평을 작성하려면 로그인이 필요합니다
                    </p>
                    <p v-else class="text-xs text-muted-foreground">
                      Ctrl+Enter로 빠르게 등록
                    </p>
                    <button 
                      @click="handleSubmitComment"
                      :disabled="!authStore.isAuthenticated || !newCommentContent.trim() || commentSubmitting"
                      class="px-4 py-2 text-sm font-medium rounded-lg bg-primary text-primary-foreground hover:bg-primary/90 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200"
                    >
                      {{ commentSubmitting ? '등록 중...' : '등록하기' }}
                    </button>
                  </div>
                </div>

                <!-- 한줄평 목록 -->
                <div v-if="commentsLoading" class="text-center py-8">
                  <p class="text-sm text-muted-foreground">한줄평을 불러오는 중...</p>
                </div>

                <div v-else-if="comments.length === 0" class="text-center py-12">
                  <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="mx-auto mb-4 text-muted-foreground/50">
                    <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                  </svg>
                  <p class="text-sm text-muted-foreground">아직 작성된 한줄평이 없습니다.</p>
                  <p class="text-xs text-muted-foreground mt-1">첫 번째 한줄평을 남겨보세요!</p>
                </div>

                <div v-else class="space-y-3">
                  <div 
                    v-for="comment in comments" 
                    :key="comment.commentId"
                    class="rounded-lg border border-border bg-card p-4 hover:shadow-sm transition-all duration-200"
                  >
                    <!-- 수정 모드 -->
                    <div v-if="editingCommentId === comment.commentId" class="space-y-2">
                      <textarea
                        v-model="editingContent"
                        class="w-full rounded-lg border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-primary resize-none transition-all duration-200"
                        rows="3"
                      ></textarea>
                      <div class="flex justify-end gap-2">
                        <button
                          @click="cancelEdit"
                          class="px-3 py-1.5 text-xs font-medium rounded-lg border border-input bg-background hover:bg-accent hover:text-accent-foreground transition-all duration-200"
                        >
                          취소
                        </button>
                        <button
                          @click="handleUpdateComment(comment.commentId)"
                          :disabled="!editingContent.trim() || commentSubmitting"
                          class="px-3 py-1.5 text-xs font-medium rounded-lg bg-primary text-primary-foreground hover:bg-primary/90 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200"
                        >
                          {{ commentSubmitting ? '저장 중...' : '저장' }}
                        </button>
                      </div>
                    </div>

                    <!-- 일반 표시 모드 -->
                    <div v-else>
                      <div class="flex items-start justify-between mb-2">
                        <div class="flex items-center gap-2">
                          <span class="text-sm font-medium text-foreground">{{ comment.nickname }}</span>
                          <span v-if="comment.edited" class="text-xs text-muted-foreground px-2 py-0.5 rounded bg-muted/50">
                            수정됨
                          </span>
                        </div>
                        <div class="flex items-center gap-2">
                          <span class="text-xs font-mono text-muted-foreground">
                            {{ formatCommentDate(comment) }}
                          </span>
                          <!-- 본인 댓글인 경우 수정/삭제 버튼 -->
                          <div v-if="authStore.isAuthenticated && authStore.user && authStore.user.nickname === comment.nickname" class="flex gap-1">
                            <button
                              @click="startEdit(comment)"
                              class="text-xs text-muted-foreground hover:text-primary transition-all duration-200"
                            >
                              수정
                            </button>
                            <span class="text-xs text-muted-foreground">·</span>
                            <button
                              @click="handleDeleteComment(comment.commentId)"
                              class="text-xs text-muted-foreground hover:text-destructive transition-all duration-200"
                            >
                              삭제
                            </button>
                          </div>
                        </div>
                      </div>
                      <p class="text-sm text-foreground leading-relaxed whitespace-pre-wrap">{{ comment.content }}</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 관리자 액션 (하단) -->
          <div v-if="authStore.isAdmin" class="mt-6 flex justify-end gap-2">
            <button
              @click="handleEdit"
              class="px-4 py-2 text-sm font-medium rounded-lg border border-input bg-background hover:bg-accent hover:text-accent-foreground transition-all duration-200"
            >
              수정
            </button>
            <button
              @click="handleDelete"
              class="px-4 py-2 text-sm font-medium rounded-lg bg-destructive text-destructive-foreground hover:bg-destructive/90 transition-all duration-200"
            >
              삭제
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { addComment, deleteComment, getComments, updateComment } from '@/api/comments'
import { deleteEtf, getEtfDetail } from '@/api/etf'
import { useAuthStore } from '@/stores/auth'
import { useBookmarkStore } from '@/stores/bookmark'
import { useLikeStore } from '@/stores/like'
import { Chart, registerables } from 'chart.js'
import {
    BarChart3,
    Info,
    MessageSquare,
    Scale,
    ShieldAlert,
    Sparkles,
    TrendingDown,
    TrendingUp
} from 'lucide-vue-next'
import { computed, nextTick, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

// Chart.js 전역 등록
Chart.register(...registerables)

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const bookmarkStore = useBookmarkStore()
const likeStore = useLikeStore()

const etf = ref(null)
const loading = ref(true)
const error = ref(null)
const activeTab = ref('chart')
const likeLoading = ref(false)
const bookmarkLoading = ref(false)

// 한줄평 관련 상태
const comments = ref([])
const commentsLoading = ref(false)
const newCommentContent = ref('')
const commentSubmitting = ref(false)
const editingCommentId = ref(null)
const editingContent = ref('')

// AI 댓글 분석 관련 상태
const aiAnalysis = ref({
  positivePercent: 0,
  negativePercent: 0,
  totalCnt: 0,
  moodMessage: ''
})

// Chart.js 관련
const sentimentChartCanvas = ref(null)
let sentimentChartInstance = null

// 중립 비율 계산 (프론트엔드에서 계산)
const neutralPercent = computed(() => {
  const positive = aiAnalysis.value.positivePercent || 0
  const negative = aiAnalysis.value.negativePercent || 0
  const neutral = 100 - (positive + negative)
  // 음수 방지 (데이터 오류 대비)
  return Math.max(0, neutral)
})

// 감정별 개수 계산 (백엔드에서 제공하지 않으므로 프론트엔드에서 계산)
const positiveCount = computed(() => {
  return Math.round(aiAnalysis.value.totalCnt * aiAnalysis.value.positivePercent / 100)
})

const negativeCount = computed(() => {
  return Math.round(aiAnalysis.value.totalCnt * aiAnalysis.value.negativePercent / 100)
})

const neutralCount = computed(() => {
  // 반올림 오차 조정
  return aiAnalysis.value.totalCnt - positiveCount.value - negativeCount.value
})

// 차트가 표시되어야 하는지 여부
const shouldShowChart = computed(() => {
  return aiAnalysis.value.totalCnt >= 5
})

// moodMessage variant 감지 (우선순위 순서)
const moodVariant = computed(() => {
  const msg = aiAnalysis.value.moodMessage || ''
  const total = aiAnalysis.value.totalCnt
  
  // 1. cold: totalCnt < 5 OR contains "데이터가 부족"
  if (total < 5 || msg.includes('데이터가 부족')) return 'cold'
  
  // 2. neutral: contains "(중립 우세)"
  if (msg.includes('(중립 우세)')) return 'neutral'
  
  // 3. strongBuy: contains "매수세" or "📈"
  if (msg.includes('매수세') || msg.includes('📈')) return 'strongBuy'
  
  // 4. positive: contains "긍정적인 전망"
  if (msg.includes('긍정적인 전망')) return 'positive'
  
  // 5. freeze: contains "냉각" or "주의" or "❄️"
  if (msg.includes('냉각') || msg.includes('주의') || msg.includes('❄️')) return 'freeze'
  
  // 6. negative: contains "하락"
  if (msg.includes('하락')) return 'negative'
  
  // 7. balanced: contains "팽팽" or "⚖️"
  if (msg.includes('팽팽') || msg.includes('⚖️')) return 'balanced'
  
  // 8. default
  return 'default'
})

// variant별 설정
const variantConfig = computed(() => {
  const configs = {
    cold: {
      icon: MessageSquare,
      headline: '아직 데이터가 부족해요',
      bgClass: 'bg-gray-50',
      borderClass: 'border-gray-200',
      iconColor: 'text-gray-500',
      textColor: 'text-gray-900',
      badgeBg: 'bg-gray-100',
      badgeText: 'text-gray-700'
    },
    neutral: {
      icon: Info,
      headline: '관망 분위기',
      bgClass: 'bg-slate-50',
      borderClass: 'border-slate-200',
      iconColor: 'text-slate-600',
      textColor: 'text-slate-900',
      badgeBg: 'bg-slate-100',
      badgeText: 'text-slate-700'
    },
    strongBuy: {
      icon: TrendingUp,
      headline: '강한 매수 심리',
      bgClass: 'bg-emerald-50',
      borderClass: 'border-emerald-300',
      iconColor: 'text-emerald-600',
      textColor: 'text-emerald-900',
      badgeBg: 'bg-emerald-100',
      badgeText: 'text-emerald-700'
    },
    positive: {
      icon: Sparkles,
      headline: '긍정 우세',
      bgClass: 'bg-green-50',
      borderClass: 'border-green-300',
      iconColor: 'text-green-600',
      textColor: 'text-green-900',
      badgeBg: 'bg-green-100',
      badgeText: 'text-green-700'
    },
    freeze: {
      icon: ShieldAlert,
      headline: '주의 구간',
      bgClass: 'bg-blue-50',
      borderClass: 'border-blue-300',
      iconColor: 'text-blue-600',
      textColor: 'text-blue-900',
      badgeBg: 'bg-blue-100',
      badgeText: 'text-blue-700'
    },
    negative: {
      icon: TrendingDown,
      headline: '하락 우려',
      bgClass: 'bg-rose-50',
      borderClass: 'border-rose-300',
      iconColor: 'text-rose-600',
      textColor: 'text-rose-900',
      badgeBg: 'bg-rose-100',
      badgeText: 'text-rose-700'
    },
    balanced: {
      icon: Scale,
      headline: '의견 팽팽',
      bgClass: 'bg-violet-50',
      borderClass: 'border-violet-300',
      iconColor: 'text-violet-600',
      textColor: 'text-violet-900',
      badgeBg: 'bg-violet-100',
      badgeText: 'text-violet-700'
    },
    default: {
      icon: BarChart3,
      headline: '시장 분위기',
      bgClass: 'bg-gray-50',
      borderClass: 'border-gray-200',
      iconColor: 'text-gray-600',
      textColor: 'text-gray-900',
      badgeBg: 'bg-gray-100',
      badgeText: 'text-gray-700'
    }
  }
  
  return configs[moodVariant.value] || configs.default
})

// 차트 초기화 및 업데이트
const updateSentimentChart = async () => {
  // 차트를 보여주지 않아야 하면 기존 차트 삭제
  if (!shouldShowChart.value) {
    if (sentimentChartInstance) {
      sentimentChartInstance.destroy()
      sentimentChartInstance = null
    }
    return
  }

  // DOM이 렌더링될 때까지 대기
  await nextTick()

  if (!sentimentChartCanvas.value) {
    console.warn('차트 캔버스를 찾을 수 없습니다.')
    return
  }

  const ctx = sentimentChartCanvas.value.getContext('2d')

  // 기존 차트가 있으면 데이터만 업데이트
  if (sentimentChartInstance) {
    sentimentChartInstance.data.datasets[0].data = [
      aiAnalysis.value.positivePercent,
      neutralPercent.value,
      aiAnalysis.value.negativePercent
    ]
    sentimentChartInstance.update()
    return
  }

  // 새 차트 생성
  sentimentChartInstance = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: ['긍정', '중립', '부정'],
      datasets: [{
        data: [
          aiAnalysis.value.positivePercent,
          neutralPercent.value,
          aiAnalysis.value.negativePercent
        ],
        backgroundColor: [
          'rgba(16, 185, 129, 0.9)',  // 녹색 (긍정) #10b981
          'rgba(209, 213, 219, 0.9)', // 회색 (중립) #d1d5db
          'rgba(239, 68, 68, 0.9)'    // 빨강 (부정) #ef4444
        ],
        borderColor: [
          'rgba(16, 185, 129, 1)',
          'rgba(209, 213, 219, 1)',
          'rgba(239, 68, 68, 1)'
        ],
        borderWidth: 2
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: true,
      cutout: '65%',
      plugins: {
        legend: {
          display: false  // 범례 숨김 (커스텀 범례 사용)
        },
        tooltip: {
          callbacks: {
            label: function(context) {
              return context.label + ': ' + context.parsed + '%'
            }
          }
        }
      }
    }
  })
}

const tabs = [
  { id: 'chart', label: '차트/수익률' },
  { id: 'info', label: '종목 정보' },
  { id: 'holdings', label: '구성 종목' },
  { id: 'comments', label: '한줄평' }
]

// 북마크 여부 확인
const isBookmarked = computed(() => {
  return bookmarkStore.isBookmarked(etf.value?.etfId)
})

const formatPrice = (price) => {
  if (!price) return '0'
  return price.toLocaleString('ko-KR')
}

const formatAumDetail = (aum) => {
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

const fetchEtfDetail = async () => {
  try {
    loading.value = true
    const response = await getEtfDetail(route.params.etfId)
    etf.value = response.data
    
    // 북마크 및 좋아요 목록 로드 (로그인 상태인 경우)
    if (authStore.isAuthenticated) {
      await Promise.all([
        bookmarkStore.fetchBookmarks(),
        likeStore.fetchLikes()
      ])
      
      // likedByMe 상태 업데이트
      etf.value.likedByMe = likeStore.isLiked(etf.value.etfId)
    }
  } catch (err) {
    console.error('ETF 상세 조회 실패:', err)
    error.value = 'ETF 정보를 불러오는데 실패했습니다.'
  } finally {
    loading.value = false
  }
}

// 좋아요 토글
const handleToggleLike = async () => {
  // 로그인 체크
  const token = localStorage.getItem('accessToken')
  if (!authStore.isAuthenticated || !token) {
    console.warn('❌ 로그인 필요:', { 
      isAuthenticated: authStore.isAuthenticated, 
      hasToken: !!token 
    })
    alert('로그인이 필요합니다.')
    router.push('/login')
    return
  }

  console.log('✅ 좋아요 요청 시작:', { etfId: etf.value.etfId, hasToken: true })

  likeLoading.value = true
  try {
    const result = await likeStore.toggleLike(etf.value.etfId)
    
    if (result.success) {
      // UI 즉시 업데이트
      etf.value.likedByMe = !etf.value.likedByMe
      etf.value.likeCount = etf.value.likedByMe 
        ? (etf.value.likeCount || 0) + 1 
        : Math.max((etf.value.likeCount || 0) - 1, 0)
      
      console.log('✅ 좋아요 성공:', { 
        likedByMe: etf.value.likedByMe, 
        likeCount: etf.value.likeCount 
      })
    } else {
      console.error('❌ 좋아요 실패:', result.message)
      alert(result.message)
    }
  } catch (err) {
    console.error('❌ 좋아요 에러:', err)
    if (err.response?.status === 403) {
      alert('권한이 없습니다. 다시 로그인해주세요.')
      localStorage.removeItem('accessToken')
      router.push('/login')
    } else {
      alert('좋아요 처리에 실패했습니다.')
    }
  } finally {
    likeLoading.value = false
  }
}

// 북마크 토글
const handleToggleBookmark = async () => {
  // 로그인 체크
  const token = localStorage.getItem('accessToken')
  if (!authStore.isAuthenticated || !token) {
    console.warn('❌ 로그인 필요:', { 
      isAuthenticated: authStore.isAuthenticated, 
      hasToken: !!token 
    })
    alert('로그인이 필요합니다.')
    router.push('/login')
    return
  }

  console.log('✅ 북마크 요청 시작:', { etfId: etf.value.etfId, hasToken: true })

  bookmarkLoading.value = true
  try {
    let result
    if (isBookmarked.value) {
      result = await bookmarkStore.removeBookmark(etf.value.etfId)
    } else {
      result = await bookmarkStore.addBookmark(etf.value.etfId)
    }
    
    if (result.success) {
      console.log('✅ 북마크 성공')
    } else {
      console.error('❌ 북마크 실패:', result.message)
      alert(result.message)
    }
  } catch (err) {
    console.error('❌ 북마크 에러:', err)
    if (err.response?.status === 403) {
      alert('권한이 없습니다. 다시 로그인해주세요.')
      localStorage.removeItem('accessToken')
      router.push('/login')
    } else {
      alert('포트폴리오 처리에 실패했습니다.')
    }
  } finally {
    bookmarkLoading.value = false
  }
}

// 공유하기
const handleShare = () => {
  const url = window.location.href
  if (navigator.share) {
    navigator.share({
      title: etf.value.etfName,
      text: `${etf.value.etfName} - ETFinder에서 확인하세요`,
      url: url
    }).catch(() => {
      // 공유 취소시 무시
    })
  } else {
    // Web Share API 미지원시 클립보드에 복사
    navigator.clipboard.writeText(url).then(() => {
      alert('링크가 클립보드에 복사되었습니다.')
    })
  }
}

const handleEdit = () => {
  router.push(`/etfs/${etf.value.etfId}/edit`)
}

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

// ==============================
// 한줄평 관련 함수
// ==============================

// 한줄평 목록 조회
const fetchComments = async () => {
  if (!route.params.etfId) return
  
  try {
    commentsLoading.value = true
    console.log('[한줄평 조회]', route.params.etfId)
    
    // CommentListResponse 구조로 응답 받기
    const response = await getComments(route.params.etfId)
    
    // 댓글 목록 설정
    comments.value = response.comments || []
    
    // AI 분석 데이터 설정
    aiAnalysis.value = {
      positivePercent: response.positivePercent || 0,
      negativePercent: response.negativePercent || 0,
      totalCnt: response.totalCnt || 0,
      moodMessage: response.moodMessage || ''
    }
    
    console.log('[한줄평 조회 성공]', comments.value.length, '개')
    console.log('[AI 분석]', aiAnalysis.value)
    
    // 감정 비율 합계 검증 (100%인지 확인)
    await nextTick()
    const total = aiAnalysis.value.positivePercent + neutralPercent.value + aiAnalysis.value.negativePercent
    console.log('[감정 비율 검증]', {
      긍정: aiAnalysis.value.positivePercent + '%',
      중립: neutralPercent.value + '%',
      부정: aiAnalysis.value.negativePercent + '%',
      합계: total + '%',
      검증: total === 100 ? '✅ 정상' : '⚠️ 오류'
    })
    
    // 차트 업데이트 (한줄평 탭이 활성화된 경우에만)
    if (activeTab.value === 'comments') {
      await updateSentimentChart()
    }
  } catch (err) {
    console.error('한줄평 조회 실패:', err)
    // 사용자에게는 에러 표시 안 함 (빈 목록으로 처리)
    comments.value = []
    aiAnalysis.value = {
      positivePercent: 0,
      negativePercent: 0,
      totalCnt: 0,
      moodMessage: ''
    }
  } finally {
    commentsLoading.value = false
  }
}

// 한줄평 등록
const handleSubmitComment = async () => {
  // 로그인 체크
  if (!authStore.isAuthenticated) {
    alert('로그인이 필요합니다.')
    router.push('/login')
    return
  }

  // 내용 검증
  if (!newCommentContent.value.trim()) {
    alert('한줄평 내용을 입력해주세요.')
    return
  }

  try {
    commentSubmitting.value = true
    console.log('[한줄평 등록 시작]', { 
      etfId: route.params.etfId, 
      content: newCommentContent.value.substring(0, 30) + '...' 
    })
    
    const response = await addComment(route.params.etfId, newCommentContent.value)
    
    console.log('[한줄평 등록 성공]', response.data)
    
    // 성공 메시지 표시
    alert(response.data || '한줄평이 등록되었습니다.')
    
    // 입력 필드 초기화
    newCommentContent.value = ''
    
    // 목록 새로고침
    await fetchComments()
  } catch (err) {
    console.error('한줄평 등록 실패:', err)
    
    if (err.response?.status === 401) {
      alert('로그인이 필요합니다.')
      router.push('/login')
    } else if (err.response?.data) {
      alert(err.response.data)
    } else {
      alert('한줄평 등록에 실패했습니다.')
    }
  } finally {
    commentSubmitting.value = false
  }
}

// 수정 모드 시작
const startEdit = (comment) => {
  editingCommentId.value = comment.commentId
  editingContent.value = comment.content
}

// 수정 취소
const cancelEdit = () => {
  editingCommentId.value = null
  editingContent.value = ''
}

// 한줄평 수정
const handleUpdateComment = async (commentId) => {
  if (!editingContent.value.trim()) {
    alert('내용을 입력해주세요.')
    return
  }

  try {
    commentSubmitting.value = true
    console.log('[한줄평 수정 시작]', { commentId })
    
    const response = await updateComment(route.params.etfId, commentId, editingContent.value)
    
    console.log('[한줄평 수정 성공]', response.data)
    alert(response.data || '한줄평이 수정되었습니다.')
    
    // 수정 모드 종료
    cancelEdit()
    
    // 목록 새로고침
    await fetchComments()
  } catch (err) {
    console.error('한줄평 수정 실패:', err)
    
    if (err.response?.status === 401) {
      alert('로그인이 필요합니다.')
      router.push('/login')
    } else if (err.response?.status === 403) {
      alert('수정 권한이 없거나 댓글이 존재하지 않습니다.')
    } else if (err.response?.data) {
      alert(err.response.data)
    } else {
      alert('한줄평 수정에 실패했습니다.')
    }
  } finally {
    commentSubmitting.value = false
  }
}

// 한줄평 삭제
const handleDeleteComment = async (commentId) => {
  if (!confirm('이 한줄평을 삭제하시겠습니까?')) return

  try {
    console.log('[한줄평 삭제 시작]', { commentId })
    
    const response = await deleteComment(route.params.etfId, commentId)
    
    console.log('[한줄평 삭제 성공]', response.data)
    alert(response.data || '한줄평이 삭제되었습니다.')
    
    // 목록 새로고침
    await fetchComments()
  } catch (err) {
    console.error('한줄평 삭제 실패:', err)
    
    if (err.response?.status === 401) {
      alert('로그인이 필요합니다.')
      router.push('/login')
    } else if (err.response?.status === 403) {
      alert('삭제 권한이 없거나 댓글이 존재하지 않습니다.')
    } else if (err.response?.data) {
      alert(err.response.data)
    } else {
      alert('한줄평 삭제에 실패했습니다.')
    }
  }
}

// 시간 포맷팅 (edited인 경우 updatedAt, 아니면 createdAt)
const formatCommentDate = (comment) => {
  const dateStr = comment.edited ? comment.updatedAt : comment.createdAt
  if (!dateStr) return ''
  
  try {
    const date = new Date(dateStr)
    const now = new Date()
    const diffMs = now - date
    const diffMins = Math.floor(diffMs / 60000)
    const diffHours = Math.floor(diffMs / 3600000)
    const diffDays = Math.floor(diffMs / 86400000)
    
    if (diffMins < 1) return '방금 전'
    if (diffMins < 60) return `${diffMins}분 전`
    if (diffHours < 24) return `${diffHours}시간 전`
    if (diffDays < 7) return `${diffDays}일 전`
    
    // 7일 이상이면 날짜 표시
    return date.toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    }).replace(/\. /g, '.').replace(/\.$/, '')
  } catch (err) {
    console.error('날짜 포맷팅 실패:', err)
    return dateStr
  }
}

// 한줄평 탭 활성화 시 댓글 로드 및 차트 업데이트
watch(activeTab, async (newTab) => {
  if (newTab === 'comments') {
    if (comments.value.length === 0 && !commentsLoading.value) {
      await fetchComments()
    } else {
      // 이미 데이터가 있으면 차트만 업데이트
      await updateSentimentChart()
    }
  }
})

onMounted(async () => {
  await fetchEtfDetail()
  
  // 사용자 정보 로드 (댓글 수정/삭제 권한 확인용)
  if (authStore.isAuthenticated && !authStore.user) {
    try {
      await authStore.getMyInfo()
    } catch (err) {
      console.error('사용자 정보 조회 실패:', err)
    }
  }
  
  // 초기 로드 시 한줄평 탭이 활성화되어 있으면 댓글 로드
  if (activeTab.value === 'comments') {
    fetchComments()
  }
})
</script>

<style scoped>
/* Tailwind로 처리 */
</style>
