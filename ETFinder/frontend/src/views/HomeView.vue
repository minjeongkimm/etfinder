<template>
  <div class="flex flex-col">
    <!-- Section 1: Hero (Main Title) -->
    <section class="min-h-screen flex flex-col items-center justify-center text-center px-6 py-20 relative overflow-hidden">
       <!-- Gradient Background for Hero -->
       <div class="absolute inset-0 bg-gradient-to-b from-transparent via-blue-50/30 to-white/0 pointer-events-none"></div>

       <ScrollReveal>
         <div class="mb-6 inline-flex items-center gap-2 px-4 py-2 rounded-full border border-slate-200 bg-white/50 backdrop-blur-sm text-sm font-semibold text-slate-600 shadow-sm">
           <span class="flex h-2 w-2 relative">
             <span class="animate-ping absolute inline-flex h-full w-full rounded-full bg-blue-400 opacity-75"></span>
             <span class="relative inline-flex rounded-full h-2 w-2 bg-blue-500"></span>
           </span>
           ETFinder
         </div>
         <h1 class="text-6xl md:text-8xl font-black tracking-tighter text-slate-900 mb-8 leading-tight">
           Investing,<br>
           <span class="text-transparent bg-clip-text bg-gradient-to-r from-blue-600 to-cyan-600">Reimagined.</span>
         </h1>
         <p class="text-xl md:text-2xl text-slate-500 max-w-2xl mx-auto font-medium leading-relaxed mb-12">
           복잡한 차트와 숫자는 이제 그만.<br>
           AI가 분석하는 가장 직관적인 ETF 투자 플랫폼.
         </p>
         

       </ScrollReveal>

       <!-- Moving Down Indicator -->
       <div class="absolute bottom-10 left-1/2 -translate-x-1/2 animate-bounce opacity-50">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-slate-400"><path d="M7 13 12 18 17 13"/><path d="M7 6 12 11 17 6"/></svg>
       </div>
    </section>

    <!-- Section 1.5: Feature Carousel (Single Card) -->
    <section class="py-24 relative overflow-hidden bg-white/30 backdrop-blur-sm border-y border-slate-100/50">
      <div class="absolute inset-0 bg-gradient-to-br from-blue-50/20 via-white/0 to-cyan-50/20 -z-10"></div>
      
      <div class="container mx-auto max-w-6xl px-6 relative z-10">
         <div class="relative h-[500px] flex items-center justify-center">
            <!-- Navigation Buttons -->
            <button 
               @click="prevSlide" 
               class="absolute left-4 md:left-64 top-1/2 -translate-y-1/2 z-30 p-2 text-slate-400 hover:text-slate-800 transition-all active:scale-95 bg-white/50 backdrop-blur-sm rounded-full shadow-sm hover:shadow-md"
            >
               <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" /></svg>
            </button>
            <button 
               @click="nextSlide" 
               class="absolute right-4 md:right-64 top-1/2 -translate-y-1/2 z-30 p-2 text-slate-400 hover:text-slate-800 transition-all active:scale-95 bg-white/50 backdrop-blur-sm rounded-full shadow-sm hover:shadow-md"
            >
               <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" /></svg>
            </button>

            <!-- Cards Container -->
            <div class="relative w-full h-full perspective-1000">
               <div 
                  v-for="(feature, idx) in features" 
                  :key="idx"
                  class="absolute top-0 left-0 right-0 bottom-0 transition-all duration-500 ease-out flex justify-center items-center pointer-events-none"
                  :style="{
                     zIndex: currentSlide === idx ? 20 : 10,
                     opacity: Math.abs(currentSlide - idx) > 1 && Math.abs(currentSlide - idx) !== features.length - 1 ? 0 : (currentSlide === idx ? 1 : 0.4),
                     transform: currentSlide === idx 
                        ? 'translateZ(0) scale(1)' 
                        : `translateX(${(idx - currentSlide) * 60}%) scale(0.85) translateZ(-100px)`
                  }"
               >
                  <!-- 
                      Card Structure: Wide Landscape
                      Glassmorphism: bg-white/60 backdrop-blur-md
                      Hover: Blur Content + Reveal Description
                  -->
                  <div 
                     class="glass-card-custom w-[600px] h-[380px] transition-transform duration-500 hover:scale-[1.02] hover:shadow-[0_20px_50px_-12px_rgba(0,0,0,0.25)] origin-center"
                     :class="[
                        currentSlide === idx ? 'pointer-events-auto group' : ''
                     ]"
                  >
                     <!-- Layer 1: Visible Content (Header + UI) -->
                     <div 
                        class="h-full flex flex-col p-10 transition-all duration-500 ease-in-out"
                        :class="[
                           currentSlide !== idx ? 'opacity-0' : 'group-hover:blur-md group-hover:opacity-40'
                        ]"
                     >
                        <!-- Header: Icon + Title (Left Aligned) -->
                        <div class="flex items-center gap-4 mb-8">
                           <div class="w-12 h-12 rounded-2xl bg-white shadow-lg shadow-blue-100/50 text-blue-600 flex items-center justify-center ring-1 ring-white/50">
                              <component :is="feature.icon" class="w-6 h-6 stroke-2" />
                           </div>
                           <h2 class="text-2xl font-bold text-slate-800 leading-tight">
                              {{ feature.title.replace('\\n', ' ') }}
                           </h2>
                        </div>

                        <!-- UI Snippet (Restored Detail) -->
                        <div class="flex-1 w-full relative perspective-500">
                           <div class="absolute inset-0 flex items-center justify-center transform transition-transform duration-500 group-hover:translate-z-[-20px]">
                                <!-- Slide 0: Insight -->
                                <div v-if="idx === 0" class="w-full max-w-md bg-white rounded-xl shadow-lg border border-slate-100 p-5 space-y-4">
                                   <div class="flex items-center gap-3 border-b border-slate-50 pb-3">
                                      <div class="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center"><Bot class="w-4 h-4 text-blue-600" /></div>
                                      <div>
                                         <div class="font-bold text-slate-800 text-sm">AI 인사이트</div>
                                         <div class="text-[10px] text-slate-400">방금 업데이트됨</div>
                                      </div>
                                   </div>
                                   <div class="space-y-2">
                                      <div class="h-2.5 bg-slate-100 rounded w-full"></div>
                                      <div class="h-2.5 bg-slate-100 rounded w-5/6"></div>
                                      <div class="h-2.5 bg-slate-100 rounded w-4/6"></div>
                                   </div>
                                   <div class="flex gap-2">
                                      <span class="px-2 py-1 bg-green-50 text-green-600 text-xs font-bold rounded-md">#성장성_높음</span>
                                      <span class="px-2 py-1 bg-blue-50 text-blue-600 text-xs font-bold rounded-md">#배당_안정</span>
                                   </div>
                                </div>

                                <!-- Slide 1: Diagnosis -->
                                <div v-if="idx === 1" class="flex gap-6 items-center">
                                   <div class="w-28 h-28 bg-white rounded-full flex items-center justify-center shadow-lg text-blue-500 ring-4 ring-white">
                                      <ClipboardCheck class="w-14 h-14" />
                                   </div>
                                   <div class="bg-white/80 backdrop-blur px-6 py-4 rounded-2xl shadow-md border border-white">
                                      <div class="text-sm text-slate-500 mb-1 font-bold">Your Type</div>
                                      <div class="text-2xl font-bold text-blue-600">안정 추구형</div>
                                      <div class="text-xs text-slate-400 mt-2">위험 회피 및 자산 방어 선호</div>
                                   </div>
                                </div>

                                <!-- Slide 2: Recommend (Algorithm Ranking) -->
                                <div v-if="idx === 2" class="w-full max-w-md bg-white rounded-xl shadow-lg border border-slate-100 overflow-hidden">
                                   <!-- Header -->
                                   <div class="bg-slate-50 px-4 py-3 border-b border-slate-100 flex justify-between items-center">
                                       <span class="text-xs font-bold text-slate-600 flex items-center gap-1">
                                          <Zap class="w-3 h-3 text-amber-500 fill-amber-500" />
                                          추천 결과
                                       </span>
                                       <span class="text-[10px] font-bold text-blue-600 bg-blue-50 px-2 py-0.5 rounded-full">분석 완료</span>
                                   </div>
                                   <!-- Ranked List -->
                                   <div class="p-3 space-y-2">
                                       <!-- Rank 1 -->
                                       <div class="flex items-center gap-3 p-3 bg-blue-50/50 rounded-xl border border-blue-100 shadow-sm transition-transform hover:-translate-y-0.5">
                                           <div class="w-8 h-8 rounded-full bg-gradient-to-br from-blue-500 to-blue-600 text-white flex items-center justify-center text-sm font-bold shadow-md ring-2 ring-blue-100">1</div>
                                           <div class="flex-1">
                                               <div class="text-sm font-bold text-slate-800">TIGER 미국배당다우존스</div>
                                               <div class="flex items-center gap-2 mt-1">
                                                  <div class="h-1.5 w-16 bg-slate-100 rounded-full overflow-hidden">
                                                     <div class="h-full bg-blue-500 w-[98%]"></div>
                                                  </div>
                                                  <span class="text-[10px] font-bold text-blue-600">98.5점</span>
                                               </div>
                                           </div>
                                           <div class="text-right">
                                              <span class="text-[10px] font-bold text-slate-400 block">적합도</span>
                                              <span class="text-xs font-bold text-blue-600">최우수</span>
                                           </div>
                                       </div>
                                       <!-- Rank 2 -->
                                       <div class="flex items-center gap-3 p-2 px-3 rounded-lg hover:bg-slate-50">
                                           <div class="w-6 h-6 rounded-full bg-slate-100 text-slate-500 flex items-center justify-center text-xs font-bold">2</div>
                                           <div class="flex-1">
                                               <div class="text-xs font-bold text-slate-700">ACE 미국S&P500</div>
                                           </div>
                                           <div class="text-[10px] font-bold text-slate-400">94.2점</div>
                                       </div>
                                       <!-- Rank 3 -->
                                       <div class="flex items-center gap-3 p-2 px-3 rounded-lg hover:bg-slate-50 opacity-70">
                                           <div class="w-6 h-6 rounded-full bg-slate-100 text-slate-500 flex items-center justify-center text-xs font-bold">3</div>
                                           <div class="flex-1">
                                               <div class="text-xs font-bold text-slate-700">KODEX 200TR</div>
                                           </div>
                                           <div class="text-[10px] font-bold text-slate-400">89.1점</div>
                                       </div>
                                   </div>
                                </div>

                                <!-- Slide 3: Simulation -->
                                <div v-if="idx === 3" class="w-full max-w-md h-[235px] bg-white rounded-xl shadow-lg border border-slate-100 overflow-hidden flex flex-col">
                                   <!-- Header -->
                                   <div class="bg-slate-50 px-4 py-3 border-b border-slate-100 flex justify-between items-center flex-shrink-0">
                                       <span class="text-xs font-bold text-slate-600 flex items-center gap-1">
                                          <Gamepad2 class="w-3 h-3 text-purple-500 fill-purple-500" />
                                          모의 투자 리포트
                                       </span>
                                       <span class="text-[10px] font-bold text-red-600 bg-red-50 px-2 py-0.5 rounded-full">+24.5% 수익</span>
                                   </div>
                                    <div class="flex-1 relative p-3">
                                      <svg class="w-full h-full" viewBox="0 0 300 100" preserveAspectRatio="none">
                                         <!-- Dynamic jagged line -->
                                         <path d="M0 80 L30 75 L50 90 L90 50 L120 70 L150 40 L180 50 L220 20 L250 35 L300 10" fill="none" stroke="#ef4444" stroke-width="3" stroke-linecap="round" vector-effect="non-scaling-stroke" stroke-linejoin="round" />
                                         <!-- Gradient fill -->
                                         <path d="M0 80 L30 75 L50 90 L90 50 L120 70 L150 40 L180 50 L220 20 L250 35 L300 10 V 100 H 0 Z" fill="url(#redGradient)" opacity="0.1" />
                                         <defs>
                                           <linearGradient id="redGradient" x1="0" y1="0" x2="0" y2="1">
                                              <stop offset="0%" stop-color="#ef4444"/>
                                              <stop offset="100%" stop-color="#ffffff" stop-opacity="0"/>
                                           </linearGradient>
                                         </defs>
                                      </svg>
                                   </div>
                                </div>
                           </div>
                        </div>
                     </div>

                     <!-- Layer 2: Description (Reveal on Hover) -->
                     <div 
                        class="absolute inset-0 flex items-center justify-center p-12 opacity-0 group-hover:opacity-100 transition-all duration-500 ease-in-out z-20"
                     >
                        <p class="text-xl text-slate-800 font-medium text-center leading-relaxed whitespace-pre-line drop-shadow-sm">
                           {{ feature.description }}
                        </p>
                     </div>
                  </div>
               </div>
            </div>
            
            <!-- Indicators -->
             <div class="absolute bottom-[-2rem] flex justify-center gap-2">
               <button 
                  v-for="(_, idx) in features" 
                  :key="idx"
                  @click="currentSlide = idx"
                  class="h-1.5 rounded-full transition-all duration-300 shadow-sm"
                  :class="currentSlide === idx ? 'w-8 bg-blue-600' : 'w-1.5 bg-slate-300 hover:bg-slate-400'"
               ></button>
            </div>
         </div>
      </div>
    </section>

    <!-- Section 4: Portfolio (Call to Action) -->
    <section class="min-h-screen flex items-center justify-center px-6 py-24 relative">
       <!-- Content remains same -->
       <ScrollReveal class="text-center max-w-3xl mx-auto">
            <!-- (omitted for brevity, keep existing code below) -->
            <div class="mb-8 mx-auto w-20 h-20 bg-blue-600 text-white rounded-3xl flex items-center justify-center shadow-2xl shadow-blue-200 rotate-3 hover:rotate-12 transition-transform duration-500">
               <Briefcase class="w-10 h-10 stroke-2" />
            </div>
           <h2 class="text-5xl md:text-7xl font-black tracking-tighter text-slate-900 mb-8 leading-tight">
             Your Wealth,<br>
             <span class="text-transparent bg-clip-text bg-gradient-to-r from-blue-600 to-cyan-500">Managed.</span>
           </h2>
           <p class="text-xl text-slate-500 mb-10 font-medium">
             이제 모든 기회를 당신의 것으로 만드세요.<br>
             ETFinder와 함께라면 투자가 더 쉬워집니다.
           </p>
           <div class="flex flex-col sm:flex-row gap-4 justify-center">
              <router-link :to="{ name: 'etfSearch' }" class="px-8 py-4 bg-white text-slate-900 border border-slate-200 rounded-full font-bold text-lg hover:bg-slate-50 transition-all hover:scale-105 shadow-lg">
                ETF 둘러보기
              </router-link>
              <router-link :to="{ name: 'propensityTest' }" class="px-8 py-4 bg-blue-600 text-white rounded-full font-bold text-lg hover:bg-blue-700 transition-all hover:scale-105 shadow-xl shadow-blue-200">
                내 성향 분석하기
              </router-link>
           </div>
       </ScrollReveal>
    </section>
  </div>
</template>

<script setup>
import ScrollReveal from '@/components/common/ScrollReveal.vue'
import { Sparkles, Zap, Bot, ClipboardCheck, Briefcase, Gamepad2 } from 'lucide-vue-next'
import { ref } from 'vue'

const currentSlide = ref(0)
/* Reordered features based on previous request */
const features = [
  { 
    icon: Sparkles, 
    title: 'AI 인사이트 브리핑', 
    description: '복잡한 상품 설명, 이제 읽지 마세요.\n생성형 AI가 핵심 정보와 배당/성장성을\n단 3줄로 요약해 드립니다.' 
  },
  { 
    icon: ClipboardCheck, 
    title: '데이터 기반\n맞춤 진단', 
    description: 'AI가 당신의 선택을 분석합니다.\n검증된 알고리즘으로 최적의 투자 성향을 찾아보세요.' 
  },
  { 
    icon: Zap, 
    title: '정교한\n알고리즘 큐레이션', 
    description: '시가총액, 수수료, 성장성 등 다각도 점수 시스템을 통해\n당신에게 꼭 필요한 ETF를 선별합니다.' 
  },
  { 
    icon: Gamepad2, 
    title: '전략 검증\n시뮬레이터', 
    description: '가상의 자본으로 리스크 없이 실전 감각을 키우세요.\n랭킹 시스템으로 검증이 가능합니다.' 
  }
]

const nextSlide = () => {
   if (currentSlide.value < features.length - 1) {
      currentSlide.value++
   } else {
      currentSlide.value = 0 // Infinite loop loop back to start
   }
}

const prevSlide = () => {
   if (currentSlide.value > 0) {
      currentSlide.value--
   } else {
      currentSlide.value = features.length - 1 // Infinite loop go to end
   }
}

</script>

<style scoped>
.glass-card-custom {
  background: rgba(255, 255, 255, 0.4); /* Slightly higher opacity for light mode visibility, user asked for less opaque than 0.6 though */
  backdrop-filter: blur(24px);
  -webkit-backdrop-filter: blur(24px);
  border-radius: 40px; /* Matching the 2.5rem */
  border: 1px solid rgba(255, 255, 255, 0.4);
  box-shadow: 
    0 20px 50px -12px rgba(0, 0, 0, 0.1), /* Drop shadow */
    inset 0 1px 0 rgba(255, 255, 255, 0.6), /* Top highlight */
    inset 0 -1px 0 rgba(255, 255, 255, 0.2), /* Bottom highlight */
    inset 0 0 20px 10px rgba(255, 255, 255, 0.2); /* Inner glow (reduced intensity for light bg) */
  position: relative;
  overflow: hidden;
  transition: transform 0.5s ease;
}

/* Gradients for borders/highlights */
.glass-card-custom::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.9),
    transparent
  );
  z-index: 10;
}

.glass-card-custom::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 1px;
  height: 100%;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.9),
    transparent,
    rgba(255, 255, 255, 0.4)
  );
  z-index: 10;
}
</style>