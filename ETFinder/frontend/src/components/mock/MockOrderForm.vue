<template>
  <div class="rounded-xl border border-border bg-card shadow-sm p-6 h-full">
    <h3 class="text-lg font-bold text-foreground mb-1">주문하기</h3>
    <p class="text-sm text-muted-foreground mb-4">실시간 시세로 모의 주문을 체결합니다.</p>

    <!-- 매수/매도 탭 -->
    <div class="bg-slate-100 p-1 rounded-lg flex mb-6">
      <button 
        @click="tradeType = 'BUY'"
        :class="[
          'flex-1 py-2.5 text-sm font-bold rounded-md transition-all',
          tradeType === 'BUY' 
            ? 'bg-emerald-500 text-white shadow-sm' 
            : 'text-slate-600 hover:text-slate-900'
        ]"
      >
        매수
      </button>
      <button 
        @click="tradeType = 'SELL'"
        :class="[
          'flex-1 py-2.5 text-sm font-bold rounded-md transition-all',
          tradeType === 'SELL' 
            ? 'bg-rose-500 text-white shadow-sm' 
            : 'text-slate-600 hover:text-slate-900'
        ]"
      >
        매도
      </button>
    </div>

    <!-- 종목 검색 -->
    <div class="mb-6">
      <label class="block text-sm font-medium text-foreground mb-2">종목 검색</label>
      <div class="relative">
        <input 
          v-model="searchKeyword"
          type="text" 
          placeholder="종목명 또는 티커 입력"
          class="w-full pl-4 pr-12 py-3 border border-border rounded-lg bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary text-sm"
          @keyup.enter="handleSearch"
        />
        <button 
          @click="handleSearch"
          class="absolute right-2 top-1/2 -translate-y-1/2 p-1.5 text-muted-foreground hover:text-primary transition-colors"
        >
          <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
        </button>
      </div>

      <!-- 검색 결과 드롭다운 (간단 구현) -->
      <div v-if="searchResults.length > 0" class="absolute z-50 w-full mt-1 bg-card border border-border rounded-lg shadow-lg max-h-60 overflow-y-auto">
         <div 
           v-for="etf in searchResults" 
           :key="etf.etfId"
           @click="selectEtf(etf)"
           class="p-3 hover:bg-muted cursor-pointer flex justify-between items-center"
         >
           <div>
             <div class="text-sm font-bold text-foreground">{{ etf.etfName }}</div>
             <div class="text-xs text-muted-foreground">{{ etf.etfCode }}</div>
           </div>
           <div class="text-sm font-medium">{{ etf.currentPrice?.toLocaleString() }}원</div>
         </div>
      </div>
    </div>

    <!-- 선택된 ETF 정보 -->
    <div v-if="selectedEtf" class="bg-muted/50 rounded-lg p-4 mb-6">
      <div class="flex justify-between items-center mb-2">
        <span class="text-sm font-bold text-foreground">{{ selectedEtf.etfName }}</span>
        <span class="text-xs text-muted-foreground">{{ selectedEtf.etfCode }}</span>
      </div>
      <div class="flex justify-between text-sm mb-1">
        <span class="text-muted-foreground">현재가</span>
        <span class="font-bold text-foreground">{{ selectedEtf.currentPrice?.toLocaleString() ?? 0 }}원</span>
      </div>
      <div class="flex justify-between text-sm">
        <span class="text-muted-foreground">{{ tradeType === 'BUY' ? '매수 가능' : '매도 가능' }}</span>
        <span class="font-medium text-foreground">
          {{ tradeType === 'BUY' ? formatNumber(availBalance) : formatNumber(availQuantity) + '주' }}
        </span>
      </div>
    </div>
    
    <!-- ETF 미선택 시 안내 -->
    <div v-else class="bg-muted/30 rounded-lg p-4 mb-6 text-center text-sm text-muted-foreground">
      종목을 검색하여 선택해주세요.
    </div>

    <!-- 수량 입력 -->
    <div class="mb-6">
      <label class="block text-sm font-medium text-foreground mb-2">수량</label>
      <div class="flex gap-2">
        <input 
          v-model.number="quantity"
          type="number"
          min="0"
          placeholder="0"
          class="flex-1 px-4 py-3 border border-border rounded-lg bg-background text-foreground text-right font-mono focus:outline-none focus:ring-2 focus:ring-primary"
        />
        <button 
          @click="setMaxQuantity"
          class="px-4 py-2 border border-border rounded-lg text-sm font-medium hover:bg-muted transition-colors"
        >
          최대
        </button>
      </div>
    </div>

    <!-- 총 주문 금액 -->
    <div class="flex justify-between items-center mb-6 pt-4 border-t border-border">
      <span class="text-sm font-bold text-foreground">총 주문 금액</span>
      <span class="text-xl font-bold text-foreground">
        {{ formatNumber(totalAmount) }}원
      </span>
    </div>

    <!-- 주문 버튼 -->
    <button 
      @click="submitOrder"
      :disabled="!isValidOrder || isSubmitting"
      :class="[
        'w-full py-4 rounded-lg font-bold text-white text-base transition-all shadow-sm',
        !isValidOrder || isSubmitting 
          ? 'bg-slate-300 text-slate-500 cursor-not-allowed'
          : tradeType === 'BUY' ? 'bg-emerald-500 hover:bg-emerald-600' : 'bg-rose-500 hover:bg-rose-600'
      ]"
    >
      {{ isSubmitting ? '처리 중...' : (tradeType === 'BUY' ? '매수 주문' : '매도 주문') }}
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { searchEtfs } from '@/api/etf'
import type { TradeRequest, MockHoldingResponse } from '@/types/mock'

const props = defineProps<{
  balance: number
  holdings: MockHoldingResponse[]
}>()

const emit = defineEmits(['order-success'])

const tradeType = ref<'BUY' | 'SELL'>('BUY')
const searchKeyword = ref('')
const searchResults = ref<any[]>([])
const selectedEtf = ref<any>(null)
const quantity = ref<number>(0)
const isSubmitting = ref(false)

// 검색 핸들러
const handleSearch = async () => {
  if (!searchKeyword.value.trim()) return
  try {
    // 기존 api/etf.js의 searchEtfs 활용
    // params: { keyword: ... }
    const res = await searchEtfs({ keyword: searchKeyword.value })
    // 백엔드가 PageImpl 등을 줄 수 있으므로 확인 필요
    // 보통 content 배열에 있음
    const data = res.data.content || res.data // 구조 확인 필요, 일단 content 가정
    searchResults.value = data.slice(0, 5) // 상위 5개만
  } catch (e) {
    console.error(e)
    searchResults.value = []
  }
}

const selectEtf = (etf: any) => {
  selectedEtf.value = etf
  searchResults.value = []
  searchKeyword.value = ''
  quantity.value = 0
}

// 매수 가능 금액 (잔액)
const availBalance = computed(() => props.balance)

// 매도 가능 수량 (보유량)
const availQuantity = computed(() => {
  if (!selectedEtf.value) return 0
  const holding = props.holdings.find(h => h.etfId === selectedEtf.value.etfId)
  return holding ? holding.quantity : 0
})

const totalAmount = computed(() => {
  const price = selectedEtf.value?.currentPrice ?? 0
  return price * (quantity.value || 0)
})

const isValidOrder = computed(() => {
  if (!selectedEtf.value || !quantity.value || quantity.value <= 0) return false
  if (tradeType.value === 'BUY') {
    return totalAmount.value <= availBalance.value
  } else {
    return quantity.value <= availQuantity.value
  }
})

const setMaxQuantity = () => {
  if (!selectedEtf.value) return
  const price = selectedEtf.value.currentPrice
  if (tradeType.value === 'BUY') {
    quantity.value = Math.floor(availBalance.value / price)
  } else {
    quantity.value = availQuantity.value
  }
}

const submitOrder = async () => {
  if (!isValidOrder.value) return
  
  isSubmitting.value = true
  try {
    // 부모 컴포넌트에게 주문 실행 위임
    const orderData: TradeRequest = {
      etfId: selectedEtf.value.etfId,
      tradeType: tradeType.value,
      quantity: quantity.value
    }
    
    // 이 컴포넌트는 API를 직접 부르지 않고 이벤트만 발생시키거나,
    // 여기서 직접 부를 수도 있음. 계획상 직접 부르는게 좋을듯하나 
    // View에서 Refresh가 필요하므로 View가 처리하는게 깔끔함.
    // 하지만 "Component가 API를 부르고 성공하면 emit" 하는 패턴으로 구현
    
    const { executeTrade } = await import('@/api/mock')
    await executeTrade(orderData)
    
    alert('주문이 체결되었습니다.')
    emit('order-success') // 부모에게 데이터 갱신 요청
    
    // 초기화
    quantity.value = 0
    if (tradeType.value === 'SELL') { 
      // 매도 후에도 종목 선택 유지할지 여부 -> 유지하되 보유량 갱신되어야 함
      // 일단 유지
    }
  } catch (err: any) {
    const msg = err.response?.data || err.message
    alert('주문 실패: ' + msg)
  } finally {
    isSubmitting.value = false
  }
}

const formatNumber = (num: number) => {
  return num?.toLocaleString('ko-KR') ?? '0'
}
</script>
