<template>
  <div class="flex flex-col">
    <!-- Section 1: Hero (Main Title) -->
    <section class="min-h-screen flex flex-col items-center justify-center text-center px-6 py-20 relative overflow-hidden">
       <!-- Gradient Background for Hero -->
       <div class="absolute inset-0 bg-gradient-to-b from-transparent via-indigo-50/30 to-white/0 pointer-events-none"></div>

       <ScrollReveal>
         <div class="mb-6 inline-flex items-center gap-2 px-4 py-2 rounded-full border border-slate-200 bg-white/50 backdrop-blur-sm text-sm font-semibold text-slate-600 shadow-sm">
           <span class="flex h-2 w-2 relative">
             <span class="animate-ping absolute inline-flex h-full w-full rounded-full bg-indigo-400 opacity-75"></span>
             <span class="relative inline-flex rounded-full h-2 w-2 bg-indigo-500"></span>
           </span>
           ETFinder 2.0
         </div>
         <h1 class="text-6xl md:text-8xl font-black tracking-tighter text-slate-900 mb-8 leading-tight">
           Investing,<br>
           <span class="text-transparent bg-clip-text bg-gradient-to-r from-indigo-600 to-violet-600">Reimagined.</span>
         </h1>
         <p class="text-xl md:text-2xl text-slate-500 max-w-2xl mx-auto font-medium leading-relaxed mb-12">
           복잡한 차트와 숫자는 이제 그만.<br>
           AI가 분석하는 가장 직관적인 ETF 투자 플랫폼.
         </p>
         
         <div class="flex flex-col sm:flex-row gap-4 justify-center">
            <router-link :to="{ name: 'propensityTest' }" class="px-8 py-4 bg-slate-900 text-white rounded-full font-bold text-lg hover:bg-slate-800 transition-all hover:scale-105 shadow-xl">
              내 성향 분석하기
            </router-link>
            <router-link :to="{ name: 'etfSearch' }" class="px-8 py-4 bg-white text-slate-900 border border-slate-200 rounded-full font-bold text-lg hover:bg-slate-50 transition-all hover:scale-105 shadow-lg">
              둘러보기
            </router-link>
         </div>
       </ScrollReveal>

       <!-- Moving Down Indicator -->
       <div class="absolute bottom-10 left-1/2 -translate-x-1/2 animate-bounce opacity-50">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-slate-400"><path d="M7 13 12 18 17 13"/><path d="M7 6 12 11 17 6"/></svg>
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
             <router-link :to="{ name: 'etfSearch' }" class="text-indigo-600 font-bold text-lg hover:underline underline-offset-4 inline-flex items-center gap-2">
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
                         <div class="w-8 h-8 rounded-full bg-indigo-100 flex items-center justify-center text-xs font-bold text-indigo-600">AI</div>
                         <div class="bg-slate-100 rounded-2xl rounded-tl-none p-3 text-sm text-slate-600">
                            최근 시장 변동성을 고려할 때,<br>
                            <span class="font-bold text-indigo-600">방어적인 포트폴리오</span>가 유리합니다.
                         </div>
                      </div>
                      <div class="flex items-start gap-3 justify-end">
                         <div class="bg-indigo-600 rounded-2xl rounded-tr-none p-3 text-sm text-white">
                            추천 종목은 뭐야?
                         </div>
                      </div>
                      <div class="flex items-start gap-3">
                         <div class="w-8 h-8 rounded-full bg-indigo-100 flex items-center justify-center text-xs font-bold text-indigo-600">AI</div>
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
             <router-link :to="{ name: 'propensityTest' }" class="text-indigo-600 font-bold text-lg hover:underline underline-offset-4 inline-flex items-center gap-2 justify-end md:justify-start">
               AI 진단 받아보기
               <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12h14"/><path d="m12 5 7 7-7 7"/></svg>
             </router-link>
          </ScrollReveal>
       </div>
    </section>

    <!-- Section 4: Portfolio (Call to Action) -->
    <section class="min-h-screen flex items-center justify-center px-6 py-24 relative">
       <ScrollReveal class="text-center max-w-3xl mx-auto">
          <div class="mb-8 mx-auto w-20 h-20 bg-slate-900 text-white rounded-3xl flex items-center justify-center text-4xl shadow-2xl rotate-3 hover:rotate-12 transition-transform duration-500">
             💼
          </div>
          <h2 class="text-5xl md:text-7xl font-black tracking-tighter text-slate-900 mb-8 leading-tight">
            Your Wealth,<br>
            Managed.
          </h2>
          <p class="text-xl text-slate-500 mb-10 font-medium">
            이제 모든 기회를 당신의 것으로 만드세요.<br>
            ETFinder와 함께라면 투자가 더 쉬워집니다.
          </p>
          <div class="flex flex-col sm:flex-row gap-4 justify-center">
             <router-link :to="{ name: 'etfSearch' }" class="px-8 py-4 bg-white text-slate-900 border border-slate-200 rounded-full font-bold text-lg hover:bg-slate-50 transition-all hover:scale-105 shadow-lg">
               ETF 둘러보기
             </router-link>
             <router-link :to="{ name: 'login' }" class="px-8 py-4 bg-slate-900 text-white rounded-full font-bold text-lg hover:bg-slate-800 transition-all hover:scale-105 shadow-xl">
               내 자산 분석하기
             </router-link>
          </div>
       </ScrollReveal>
    </section>
  </div>
</template>

<script setup>
import ScrollReveal from '@/components/common/ScrollReveal.vue'
</script>

<style scoped>
/* 필요한 경우 추가 스타일 작성 */
</style>