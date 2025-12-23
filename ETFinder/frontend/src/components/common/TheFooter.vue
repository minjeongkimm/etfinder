<template>
  <footer class="bg-slate-50 border-t border-slate-100 py-10 mt-auto relative z-10">
    <div class="container mx-auto max-w-7xl px-6">
      
      <!-- Disclaimer (Centered & Larger) -->
      <div class="mb-8 text-center">
        <p class="text-xs text-slate-500 leading-relaxed word-keep break-keep">
          ETFinder는 초보 투자자의 이해를 돕기 위한 참고용 정보를 제공합니다.<br class="hidden md:block" />
          투자 판단과 결과에 대한 책임은 사용자 본인에게 있습니다.
        </p>
      </div>

      <!-- Bottom Row: Copyright & Links -->
      <div class="flex flex-col-reverse md:flex-row justify-between items-center gap-4">
        <!-- Copyright -->
        <p class="text-[10px] text-slate-400">
          © {{ new Date().getFullYear() }} ETFinder. All rights reserved.
        </p>

        <!-- Links -->
        <div class="flex gap-6 text-[11px] text-slate-500 font-medium cursor-pointer">
          <span @click="openModal('terms')" class="hover:text-indigo-600 transition-colors">이용약관</span>
          <span @click="openModal('privacy')" class="hover:text-indigo-600 transition-colors">개인정보처리방침</span>
          <span @click="openModal('contact')" class="hover:text-indigo-600 transition-colors">문의하기</span>
        </div>
      </div>
    </div>

    <!-- Modal Portal -->
    <Teleport to="body">
      <Transition
        enter-active-class="transition duration-200 ease-out"
        enter-from-class="opacity-0"
        enter-to-class="opacity-100"
        leave-active-class="transition duration-150 ease-in"
        leave-from-class="opacity-100"
        leave-to-class="opacity-0"
      >
        <div v-if="modalOpen" class="fixed inset-0 z-[100] z-500 flex items-center justify-center p-4 bg-black/50" @click.self="closeModal">
          <div class="bg-white rounded-2xl w-full max-w-2xl max-h-[80vh] flex flex-col shadow-2xl overflow-hidden scale-100 transition-transform">
            <!-- Header -->
            <div class="px-6 py-4 border-b border-slate-100 flex justify-between items-center bg-slate-50/50">
              <h3 class="font-bold text-lg text-slate-800">{{ currentModalTitle }}</h3>
              <button @click="closeModal" class="p-1 rounded-full hover:bg-slate-200 text-slate-500 transition-colors">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>
              </button>
            </div>
            
            <!-- Content (Scrollable) -->
            <div class="p-6 overflow-y-auto custom-scrollbar text-sm text-slate-600 leading-relaxed whitespace-pre-line">
              <div v-html="currentModalContent"></div>
            </div>

            <!-- Footer -->
            <div class="px-6 py-4 border-t border-slate-100 bg-slate-50/50 flex justify-end">
              <button @click="closeModal" class="px-4 py-2 bg-slate-800 text-white text-xs font-bold rounded-lg hover:bg-slate-700 transition-colors">
                닫기
              </button>
            </div>
          </div>
        </div>
      </Transition>
    </Teleport>
  </footer>
</template>

<script setup>
import { ref, computed } from 'vue'

const modalOpen = ref(false)
const currentModalType = ref('')

const openModal = (type) => {
  currentModalType.value = type
  modalOpen.value = true
  document.body.style.overflow = 'hidden' // Prevent background scrolling
}

const closeModal = () => {
  modalOpen.value = false
  currentModalType.value = ''
  document.body.style.overflow = ''
}

const titles = {
  terms: '이용 약관',
  privacy: '개인정보 처리방침',
  contact: '문의하기'
}

const currentModalTitle = computed(() => titles[currentModalType.value] || '')

// Markdown-like content (converted to HTML for formatting)
const contents = {
  terms: `
    <h4 class="text-base font-bold text-slate-900 mb-2 mt-4">제1조 (목적)</h4>
    <p class="mb-4">본 약관은 ETFinder(이하 “회사” 또는 “서비스”)가 제공하는 ETF 및 금융 관련 정보 서비스의 이용과 관련하여, 회사와 이용자 간의 권리·의무 및 책임사항을 규정함을 목적으로 합니다.</p>

    <h4 class="text-base font-bold text-slate-900 mb-2 mt-4">제2조 (서비스의 성격 및 범위)</h4>
    <p class="mb-2">1. 본 서비스는 ETF 및 금융상품에 대한 <strong>일반적인 정보 제공 및 학습 목적의 콘텐츠</strong>를 제공합니다.</p>
    <p class="mb-4">2. 본 서비스는 「자본시장과 금융투자업에 관한 법률」에 따른 투자자문, 투자일임 또는 금융투자상품의 매수·매도를 권유하는 행위를 포함하지 않습니다.</p>

    <h4 class="text-base font-bold text-slate-900 mb-2 mt-4">제3조 (투자 판단 및 책임)</h4>
    <p class="mb-2">1. 본 서비스에서 제공되는 정보는 이용자의 투자 판단을 보조하기 위한 참고 자료로서 제공됩니다.</p>
    <p class="mb-4">2. 투자에 대한 최종 의사결정은 이용자 본인의 판단에 따라 이루어지며, 그에 따른 투자 결과 및 손실에 대한 책임은 전적으로 이용자 본인에게 귀속됩니다.</p>

    <h4 class="text-base font-bold text-slate-900 mb-2 mt-4">제4조 (정보의 정확성 및 한계)</h4>
    <p class="mb-2">1. 본 서비스에서 제공하는 데이터 및 정보는 <strong>각 데이터 제공 기관이 공개한 자료 및 수집 시점의 정보를 기준으로</strong> 작성됩니다.</p>
    <p class="mb-2">2. 데이터의 특성상 최신 정보와 차이가 발생할 수 있으며, 오류, 누락 또는 지연이 존재할 수 있습니다.</p>
    <p class="mb-4">3. 이용자는 실제 투자 결정을 내리기 전에 증권사, 금융기관 등 공식 채널을 통해 최신 정보 및 세부 내용을 반드시 확인하여야 합니다.</p>

    <h4 class="text-base font-bold text-slate-900 mb-2 mt-4">제5조 (면책 조항)</h4>
    <p class="mb-4">회사는 본 서비스에서 제공하는 정보의 이용과 관련하여 발생한 이용자의 투자 손실 또는 기타 손해에 대하여 법적 책임을 부담하지 않습니다.</p>

    <h4 class="text-base font-bold text-slate-900 mb-2 mt-4">제6조 (서비스의 변경 및 중단)</h4>
    <p class="mb-4">회사는 시스템 점검, 데이터 제공처의 정책 변경, 기타 불가피한 사유가 발생한 경우 서비스의 전부 또는 일부를 변경하거나 중단할 수 있습니다.</p>
  `,
  privacy: `
    <p class="mb-4">ETFinder는 개인정보 보호 관련 법령을 준수하며, 이용자의 개인정보를 안전하게 관리합니다.</p>

    <h4 class="text-base font-bold text-slate-900 mb-2 mt-4">1. 수집하는 개인정보 항목</h4>
    <ul class="list-disc pl-5 mb-4 space-y-1">
      <li>필수 항목: 이메일, 닉네임</li>
      <li>선택 항목: 투자 성향, 관심 ETF 등 이용자가 서비스 이용 과정에서 제공한 정보</li>
      <li>자동 수집 항목: 서비스 이용 기록, 접속 로그, 쿠키 정보(사용 시)</li>
    </ul>

    <h4 class="text-base font-bold text-slate-900 mb-2 mt-4">2. 개인정보의 수집 및 이용 목적</h4>
    <ul class="list-disc pl-5 mb-4 space-y-1">
      <li>회원 식별 및 서비스 제공</li>
      <li>맞춤형 콘텐츠 제공 및 서비스 품질 개선</li>
      <li>서비스 이용에 대한 통계 분석</li>
    </ul>

    <h4 class="text-base font-bold text-slate-900 mb-2 mt-4">3. 개인정보의 보유 및 이용 기간</h4>
    <ul class="list-disc pl-5 mb-4 space-y-1">
      <li>이용자가 회원 탈퇴를 요청한 경우, 해당 개인정보는 지체 없이 파기합니다.</li>
      <li>단, 관련 법령에 따라 보관이 필요한 정보는 해당 법령에서 정한 기간 동안 보관합니다.</li>
    </ul>

    <h4 class="text-base font-bold text-slate-900 mb-2 mt-4">4. 개인정보의 제3자 제공</h4>
    <p class="mb-4">회사는 이용자의 개인정보를 원칙적으로 제3자에게 제공하지 않습니다. 다만, 법령에 따라 제공이 요구되는 경우에는 예외로 합니다.</p>

    <h4 class="text-base font-bold text-slate-900 mb-2 mt-4">5. 이용자의 권리</h4>
    <p class="mb-4">이용자는 언제든지 본인의 개인정보에 대하여 열람, 수정 또는 삭제를 요청할 수 있습니다.</p>

    <h4 class="text-base font-bold text-slate-900 mb-2 mt-4">6. 개인정보의 보호조치</h4>
    <p class="mb-4">회사는 개인정보의 안전성을 확보하기 위하여 기술적·관리적 보호조치를 시행합니다.</p>
  `,
  contact: `
    <p class="mb-4">ETFinder 서비스와 관련하여 문의사항, 오류 신고 또는 개선 제안이 있는 경우 아래 문의 채널을 통해 접수해 주시기 바랍니다.</p>
    <p class="mb-4">접수된 문의는 영업일 기준 1~2일 이내에 검토 후 안내드릴 예정입니다.</p>
    
    <div class="bg-indigo-50 p-4 rounded-lg border border-indigo-100 mt-4">
      <p class="font-bold text-indigo-900 mb-1">📧 이메일 문의</p>
      <a href="mailto:support@etfinder.com" class="text-indigo-600 hover:underline">support@etfinder.com</a>
    </div>
  `
}

const currentModalContent = computed(() => contents[currentModalType.value] || '')
</script>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #cbd5e1;
  border-radius: 3px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background-color: #94a3b8;
}
</style>
