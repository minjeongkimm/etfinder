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
           ETFinder 2.0
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

    <!-- Section 1.5: Sticky Scroll Feature Showcase -->
    <section class="relative bg-white/30 backdrop-blur-sm border-y border-slate-100/50">
      <div class="absolute inset-0 bg-gradient-to-br from-blue-50/20 via-white/0 to-cyan-50/20 -z-10"></div>
      
      <div class="container mx-auto max-w-7xl px-6">
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-24 relative">
           
           <!-- Left Column: Scrolling Text -->
           <div class="py-24 lg:py-0">
              <div 
                 v-for="(feature, idx) in scrollFeatures" 
                 :key="idx" 
                 :ref="el => featureRefs[idx] = el"
                 class="min-h-screen flex flex-col justify-center transition-opacity duration-500"
                 :class="{'opacity-0 blur-none scale-95': activeFeature !== idx, 'opacity-100 scale-100': activeFeature === idx}"
              >
                 <div class="mb-6 inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-blue-100 text-blue-600 mb-6">
                    <component :is="feature.icon" class="w-8 h-8 stroke-2" />
                 </div>
                 <h2 class="text-3xl md:text-5xl font-bold text-slate-900 mb-6 leading-tight whitespace-pre-line">
                    {{ feature.title }}
                 </h2>
                 <p class="text-lg md:text-xl text-slate-500 leading-relaxed max-w-lg font-medium whitespace-pre-line">
                    {{ feature.description }}
                 </p>
              </div>
           </div>

           <!-- Right Column: Sticky Laptop Frame -->
           <div class="hidden lg:block relative">
              <div class="sticky top-0 h-screen flex items-center justify-center perspective-1000">
                 <!-- Laptop Base -->
                 <div class="relative w-full max-w-2xl transform transition-transform duration-700 hover:rotate-x-2">
                    <!-- Lid/Screen -->
                    <div class="relative bg-slate-900 rounded-[1.5rem] p-3 shadow-2xl border border-slate-700 ring-1 ring-white/20">
                       <!-- Bezel -->
                       <div class="absolute top-0 left-1/2 -translate-x-1/2 w-32 h-4 bg-slate-900 rounded-b-xl z-20 flex justify-center items-center gap-2">
                          <div class="w-1.5 h-1.5 rounded-full bg-slate-700"></div>
                          <div class="w-1 h-1 rounded-full bg-green-500 shadow-[0_0_5px_rgba(34,197,94,0.8)]"></div>
                       </div>
                       
                       <!-- Screen Content -->
                       <div class="relative w-full aspect-[16/10] bg-white rounded-xl overflow-hidden group">
                          <!-- Feature 0: AI Insight Briefing UI -->
                          <div class="absolute inset-0 flex items-center justify-center bg-slate-50 transition-opacity duration-700 px-12" :class="activeFeature === 0 ? 'opacity-100 z-10' : 'opacity-0 z-0'">
                             <div class="w-full bg-white rounded-2xl shadow-lg border border-blue-100 p-6 space-y-4 transform transition-transform duration-700" :class="activeFeature === 0 ? 'translate-y-0' : 'translate-y-10'">
                                <div class="flex items-center gap-3 border-b border-slate-100 pb-4">
                                   <div class="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center"><Bot class="w-5 h-5 text-blue-600" /></div>
                                   <div>
                                      <div class="font-bold text-slate-900">AI 인사이트</div>
                                      <div class="text-xs text-slate-400">방금 업데이트됨</div>
                                   </div>
                                </div>
                                <div class="space-y-3">
                                   <div class="h-4 bg-slate-100 rounded w-3/4"></div>
                                   <div class="h-4 bg-slate-100 rounded w-full"></div>
                                   <div class="h-4 bg-slate-100 rounded w-5/6"></div>
                                </div>
                                <div class="flex gap-2 pt-2">
                                   <span class="px-3 py-1 bg-green-50 text-green-600 text-xs font-bold rounded-full">#성장성_높음</span>
                                   <span class="px-3 py-1 bg-blue-50 text-blue-600 text-xs font-bold rounded-full">#배당_안정</span>
                                </div>
                             </div>
                          </div>

                          <!-- Feature 1: Investment Test UI -->
                          <div class="absolute inset-0 flex items-center justify-center bg-blue-50 transition-opacity duration-700" :class="activeFeature === 1 ? 'opacity-100 z-10' : 'opacity-0 z-0'">
                             <div class="text-center transform transition-transform duration-700" :class="activeFeature === 1 ? 'scale-100' : 'scale-90'">
                                <div class="w-24 h-24 mx-auto bg-white rounded-full flex items-center justify-center shadow-lg mb-6 text-blue-600">
                                   <ClipboardCheck class="w-12 h-12 stroke-2" />
                                </div>
                                <h3 class="text-2xl font-bold text-slate-900 mb-2">당신의 투자 성향은?</h3>
                                <div class="inline-block px-4 py-2 bg-blue-600 text-white rounded-lg font-bold shadow-lg shadow-blue-200">안정 추구형</div>
                             </div>
                          </div>

                          <!-- Feature 2: Recommendation UI -->
                          <div class="absolute inset-0 flex items-center justify-center bg-slate-50 transition-opacity duration-700" :class="activeFeature === 2 ? 'opacity-100 z-10' : 'opacity-0 z-0'">
                             <div class="w-full max-w-sm space-y-4 px-8 transform transition-transform duration-700" :class="activeFeature === 2 ? 'translate-x-0' : 'translate-x-10'">
                                <div class="bg-white p-4 rounded-xl shadow-md border border-slate-100 flex items-center gap-4">
                                   <div class="w-12 h-12 rounded-lg bg-red-50 text-red-600 flex items-center justify-center font-bold text-lg">TIGER</div>
                                   <div class="flex-1">
                                      <div class="font-bold text-slate-900">미국배당다우존스</div>
                                      <div class="text-xs text-slate-500">배당수익률 3.8%</div>
                                   </div>
                                   <div class="text-blue-600 font-bold">+12%</div>
                                </div>
                                <div class="bg-white p-4 rounded-xl shadow-md border border-slate-100 flex items-center gap-4 opacity-75">
                                   <div class="w-12 h-12 rounded-lg bg-blue-50 text-blue-600 flex items-center justify-center font-bold text-lg">KODEX</div>
                                   <div class="flex-1">
                                      <div class="font-bold text-slate-900">삼성전자채권</div>
                                      <div class="text-xs text-slate-500">안정성 최우수</div>
                                   </div>
                                   <div class="text-slate-400 font-bold">+2%</div>
                                </div>
                             </div>
                          </div>
                          
                       </div>
                    </div>
                    <!-- Keyboard Deck -->
                    <div class="relative mx-auto bg-slate-800 w-[110%] h-4 -mt-2 rounded-b-lg shadow-2xl z-0">
                       <div class="absolute top-0 left-1/2 -translate-x-1/2 w-40 h-1.5 bg-slate-700 rounded-b-md"></div>
                    </div>
                 </div>
              </div>
           </div>

        </div>
      </div>
    </section>

    <!-- Section 2: Smart Search feature -->
    <section class="min-h-screen flex items-center justify-center px-6 py-24 relative">
       <div class="container max-w-6xl mx-auto grid grid-cols-1 md:grid-cols-2 gap-16 items-center">
          <!-- Text Content -->
          <ScrollReveal>
             <h2 class="text-5xl md:text-7xl font-bold tracking-tight text-slate-900 mb-6 leading-tight">
               Find it.<br>
               Instantly.
             </h2>
             <p class="text-xl text-slate-500 leading-relaxed mb-8 max-w-md">
               원하는 조건만 선택하세요.<br>
               수천 개의 ETF 중에서 당신에게 딱 맞는 상품을<br>
               0.1초 만에 찾아냅니다.
             </p>
             <router-link :to="{ name: 'etfSearch' }" class="text-blue-600 font-bold text-lg hover:underline underline-offset-4 inline-flex items-center gap-2">
               스마트 검색 경험하기
               <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12h14"/><path d="m12 5 7 7-7 7"/></svg>
             </router-link>
          </ScrollReveal>
          
          <!-- Visual Content (Abstract UI Representation) -->
          <ScrollReveal :delay="200" class="relative">
             <div class="aspect-square rounded-[3rem] p-12 flex items-center justify-center relative overflow-hidden group hover:scale-[1.02] transition-transform duration-700 border border-white/50 bg-white/30 shadow-2xl">
                <!-- Icons/Graphics -->
                <div class="relative z-10 text-center">
                   <div class="w-32 h-32 bg-white rounded-3xl shadow-2xl flex items-center justify-center text-6xl mb-6 mx-auto text-orange-500 rotate-3 group-hover:rotate-6 transition-transform duration-500">
                      ⚡️
                   </div>
                   <div class="px-6 py-3 bg-white/80 backdrop-blur-sm rounded-xl shadow-lg text-lg font-bold text-slate-800 transform -rotate-2 group-hover:-rotate-3 transition-transform duration-500">
                      #고배당 #기술주
                   </div>
                </div>
             </div>
          </ScrollReveal>
       </div>
    </section>

    <!-- Section 3: AI Analysis -->
    <section class="min-h-screen flex items-center justify-center px-6 py-24 relative overflow-hidden">
       <div class="container max-w-6xl mx-auto grid grid-cols-1 md:grid-cols-2 gap-16 items-center">
          <!-- Visual Content (Left on Desktop) -->
          <ScrollReveal :delay="200" class="order-2 md:order-1 relative">
             <div class="aspect-square rounded-[3rem] p-12 flex items-center justify-center relative overflow-hidden group hover:scale-[1.02] transition-transform duration-700 border border-white/50 bg-white/30 shadow-2xl">
                
                <div class="relative z-10 w-full max-w-sm bg-white rounded-2xl shadow-xl p-6 transform transition-transform duration-500 group-hover:-translate-y-2">
                   <!-- Fake Chat UI -->
                   <div class="space-y-4">
                      <div class="flex items-start gap-3">
                         <div class="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center text-xs font-bold text-blue-600">AI</div>
                         <div class="bg-slate-100 rounded-2xl rounded-tl-none p-3 text-sm text-slate-600">
                            최근 시장 변동성을 고려할 때,<br>
                            <span class="font-bold text-blue-600">방어적인 포트폴리오</span>가 유리합니다.
                         </div>
                      </div>
                      <div class="flex items-start gap-3 justify-end">
                         <div class="bg-blue-600 rounded-2xl rounded-tr-none p-3 text-sm text-white">
                            추천 종목은 뭐야?
                         </div>
                      </div>
                      <div class="flex items-start gap-3">
                         <div class="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center text-xs font-bold text-blue-600">AI</div>
                         <div class="bg-slate-100 rounded-2xl rounded-tl-none p-3 text-sm text-slate-600">
                            미국 배당성장 ETF와<br>금 현물 ETF를 조합해보세요.
                         </div>
                      </div>
                   </div>
                </div>
             </div>
          </ScrollReveal>

          <!-- Text Content -->
          <ScrollReveal class="order-1 md:order-2 text-right md:text-left">
             <h2 class="text-5xl md:text-7xl font-bold tracking-tight text-slate-900 mb-6 leading-tight">
               Data-driven.<br>
               Insightful.
             </h2>
             <p class="text-xl text-slate-500 leading-relaxed mb-8 max-w-md ml-auto md:ml-0">
               단순한 데이터 나열이 아닙니다.<br>
               AI가 시장의 맥락을 읽고,<br>
               당신에게 꼭 필요한 인사이트를 제공합니다.
             </p>
             <router-link :to="{ name: 'propensityTest' }" class="text-blue-600 font-bold text-lg hover:underline underline-offset-4 inline-flex items-center gap-2 justify-end md:justify-start">
               AI 진단 받아보기
               <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12h14"/><path d="m12 5 7 7-7 7"/></svg>
             </router-link>
          </ScrollReveal>
       </div>
    </section>

    <!-- Section 4: Portfolio (Call to Action) -->
    <section class="min-h-screen flex items-center justify-center px-6 py-24 relative">
       <ScrollReveal class="text-center max-w-3xl mx-auto">
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
import { Sparkles, Zap, Search, PieChart, Coins, Bot, ClipboardCheck, Briefcase } from 'lucide-vue-next'
import { ref, onMounted, onUnmounted } from 'vue'

const activeFeature = ref(0)
const featureRefs = ref([])

const scrollFeatures = [
  { 
    icon: Sparkles, 
    title: 'AI 인사이트 브리핑', 
    description: '복잡한 상품 설명, 이제 읽지 마세요.\n생성형 AI가 핵심 정보와 배당/성장성을\n단 3줄로 요약해 드립니다.' 
  },
  { 
    icon: PieChart, 
    title: '데이터 기반\n맞춤 진단', 
    description: 'AI가 아닌, 당신의 선택을 분석합니다.\n검증된 알고리즘으로\n최적의 투자 성향을 찾아보세요.' 
  },
  { 
    icon: Zap, 
    title: '정교한\n알고리즘 큐레이션', 
    description: '시가총액, 수수료, 성장성 등\n다각도 점수 시스템을 통해\n당신에게 꼭 필요한 ETF를 선별합니다.' 
  }
]

let observer = null

onMounted(() => {
  observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        // Find which feature is currently visible
        const index = featureRefs.value.indexOf(entry.target)
        if (index !== -1) {
           activeFeature.value = index
        }
      }
    })
  }, {
    root: null,
    threshold: 0.5, // 50% visible triggers change
    rootMargin: "-20% 0px -20% 0px"
  })

  featureRefs.value.forEach((el) => {
    if (el) observer.observe(el)
  })
})

onUnmounted(() => {
  if (observer) observer.disconnect()
})
</script>

<style scoped>
/* 필요한 경우 추가 스타일 작성 */
</style>