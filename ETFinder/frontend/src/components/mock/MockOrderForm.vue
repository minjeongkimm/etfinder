<template>
  <div class="rounded-xl border border-border bg-card shadow-sm p-6 h-full">
    <h3 class="text-lg font-bold text-foreground mb-1">주문하기</h3>
    <p class="text-sm text-muted-foreground mb-4">실시간 시세로 모의 주문을 체결합니다.</p>

    <!-- 매수/매도 탭 -->
    <div class="bg-slate-100 p-1 rounded-lg flex mb-6">
      <button 
        @click="activeTab = 'BUY'"
        :class="[
          'flex-1 py-2.5 text-sm font-bold rounded-md transition-all',
          activeTab === 'BUY' 
            ? 'bg-emerald-500 text-white shadow-sm' 
            : 'text-slate-600 hover:text-slate-900'
        ]"
      >
        매수
      </button>
      <button 
        @click="activeTab = 'SELL'"
        :class="[
          'flex-1 py-2.5 text-sm font-bold rounded-md transition-all',
          activeTab === 'SELL' 
            ? 'bg-rose-500 text-white shadow-sm' 
            : 'text-slate-600 hover:text-slate-900'
        ]"
      >
        매도
      </button>
    </div>

    <!-- 매수 탭 UI -->
    <template v-if="activeTab === 'BUY'">
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

        <!-- 검색 결과 드롭다운 -->
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
          <span class="text-muted-foreground">매수 가능</span>
          <span class="font-medium text-foreground">
            {{ formatNumber(availBalance) }}
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
            min="1"
            placeholder="수량을 입력하세요"
            class="flex-1 px-4 py-3 border border-border rounded-lg bg-background text-foreground text-right font-mono focus:outline-none focus:ring-2 focus:ring-primary placeholder:text-gray-400 placeholder:text-sm placeholder:font-normal"
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
          {{ formatNumber(totalOrderAmount) }}원
        </span>
      </div>

      <!-- 매수 주문 버튼 -->
      <button 
        @click="onSubmit"
        :disabled="!canSubmit || isSubmitting"
        :class="[
          'w-full py-4 rounded-lg font-bold text-white text-base transition-all shadow-sm',
          !canSubmit || isSubmitting 
            ? 'bg-slate-300 text-slate-500 cursor-not-allowed'
            : 'bg-emerald-500 hover:bg-emerald-600'
        ]"
      >
        {{ isSubmitting ? '처리 중...' : '매수 주문' }}
      </button>
    </template>

    <!-- 매도 탭 UI -->
    <template v-else>
      <!-- 보유 종목 선택 -->
      <div class="mb-6">
        <label class="block text-sm font-medium text-foreground mb-2">보유 종목 선택</label>
        
        <!-- 보유 종목이 없을 때 -->
        <div v-if="!props.holdings || props.holdings.length === 0" class="mt-4 rounded-xl bg-slate-50 px-4 py-3 text-sm text-slate-500">
          현재 보유 중인 종목이 없습니다. 먼저 매수 후 매도 기능을 이용할 수 있어요.
        </div>

        <!-- 보유 종목 리스트 -->
        <div v-else>
          <div class="space-y-2 max-h-64 overflow-y-auto mb-2">
            <div 
              v-for="holding in props.holdings" 
              :key="holding.etfId"
              @click="onSelectSellHolding(holding)"
              :class="[
                'flex items-center justify-between rounded-xl border p-4 cursor-pointer transition-all',
                selectedSellEtfId === holding.etfId 
                  ? 'border-rose-500 bg-rose-50/60 shadow-sm' 
                  : 'border-slate-200 bg-white hover:border-rose-400'
              ]"
            >
              <!-- 왼쪽: 종목 정보 -->
              <div class="flex-1 min-w-0">
                <div class="text-sm font-bold text-foreground truncate">{{ holding.etfName }}</div>
                <div class="text-xs text-muted-foreground">{{ holding.etfCode }}</div>
                <div class="text-xs text-muted-foreground mt-1">
                  보유 {{ holding.quantity }}주
                </div>
              </div>
              
              <!-- 가운데: 현재가, 수익률 -->
              <div class="text-right mr-3">
                <div class="text-sm font-bold text-foreground">
                  {{ formatNumber(holding.currentPrice) }}원
                </div>
                <div 
                  :class="[
                    'text-xs font-medium',
                    holding.profitRate >= 0 ? 'text-emerald-600' : 'text-rose-600'
                  ]"
                >
                  {{ holding.profitRate >= 0 ? '+' : '' }}{{ holding.profitRate.toFixed(2) }}%
                </div>
              </div>

              <!-- 오른쪽: 선택 표시 -->
              <div class="flex-shrink-0">
                <div 
                  :class="[
                    'w-5 h-5 rounded-full border-2 flex items-center justify-center transition-all',
                    selectedSellEtfId === holding.etfId
                      ? 'border-rose-500 bg-rose-500'
                      : 'border-slate-300'
                  ]"
                >
                  <svg 
                    v-if="selectedSellEtfId === holding.etfId"
                    class="w-3 h-3 text-white" 
                    fill="currentColor" 
                    viewBox="0 0 20 20"
                  >
                    <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                </div>
              </div>
            </div>
          </div>

          <!-- 안내 문구 -->
          <p class="mt-2 text-sm text-gray-400 font-normal">
            한 번에 한 종목만 매도할 수 있어요. 매도할 종목을 선택한 뒤 아래에서 수량을 입력해주세요.
          </p>
        </div>
      </div>

      <!-- 수량 입력 -->
      <div class="mb-6">
        <label class="block text-sm font-medium text-foreground mb-2">수량</label>
        <div class="flex gap-2">
          <input 
            v-model.number="quantity"
            type="number"
            :min="1"
            :max="selectedHolding ? selectedHolding.quantity : undefined"
            :disabled="!selectedHolding"
            :placeholder="selectedHolding ? '수량을 입력하세요' : '먼저 매도할 종목을 선택해주세요'"
            class="flex-1 px-4 py-3 border border-border rounded-lg bg-background text-foreground text-right font-mono focus:outline-none focus:ring-2 focus:ring-rose-500 disabled:bg-slate-100 disabled:cursor-not-allowed placeholder:text-gray-400 placeholder:text-sm placeholder:font-normal"
            @input="validateSellQuantity"
          />
          <button 
            @click="setMaxSellQuantity"
            :disabled="!selectedHolding"
            class="px-4 py-2 border border-border rounded-lg text-sm font-medium hover:bg-muted transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            최대
          </button>
        </div>
        <!-- 수량 초과 경고 -->
        <p v-if="selectedHolding && quantity && quantity > selectedHolding.quantity" class="mt-1 text-xs text-rose-600">
          보유 수량({{ selectedHolding.quantity }}주)을 초과할 수 없습니다.
        </p>
      </div>

      <!-- 총 주문 금액 -->
      <div class="flex justify-between items-center mb-6 pt-4 border-t border-border">
        <span class="text-sm font-bold text-foreground">총 주문 금액</span>
        <span class="text-xl font-bold text-rose-600">
          {{ formatNumber(totalOrderAmount) }}원
        </span>
      </div>

      <!-- 매도 주문 버튼 -->
      <button 
        @click="onSubmit"
        :disabled="!canSubmit || isSubmitting"
        :class="[
          'w-full py-4 rounded-lg font-bold text-white text-base transition-all shadow-sm',
          !canSubmit || isSubmitting 
            ? 'bg-slate-300 text-slate-500 cursor-not-allowed'
            : 'bg-rose-500 hover:bg-rose-600'
        ]"
      >
        {{ isSubmitting ? '처리 중...' : '매도 주문' }}
      </button>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { searchEtfs } from '@/api/etf'
import { executeTrade } from '@/api/mock'
import type { TradeRequest, MockHoldingResponse } from '@/types/mock'

const props = defineProps<{
  balance: number
  holdings: MockHoldingResponse[]
}>()

const emit = defineEmits(['order-success'])

// 공통 상태
const activeTab = ref<'BUY' | 'SELL'>('BUY')
const quantity = ref<number | null>(null)
const isSubmitting = ref(false)

// 매수 전용 상태
const searchKeyword = ref('')
const searchResults = ref<any[]>([])
const selectedEtf = ref<any>(null)

// 매도 전용 상태
const selectedSellEtfId = ref<number | null>(null)

// 탭 전환 시 상태 초기화
watch(activeTab, () => {
  quantity.value = null
  selectedSellEtfId.value = null
  selectedEtf.value = null
  searchKeyword.value = ''
  searchResults.value = []
})

// === 매도 관련 로직 ===

// 선택된 보유 종목
const selectedHolding = computed(() => {
  if (!selectedSellEtfId.value) return null
  return props.holdings.find(h => h.etfId === selectedSellEtfId.value) || null
})

// 보유 종목 선택 핸들러
const onSelectSellHolding = (holding: MockHoldingResponse) => {
  if (selectedSellEtfId.value === holding.etfId) {
    // 다시 클릭하면 선택 해제
    selectedSellEtfId.value = null
    quantity.value = null
  } else {
    // 새 종목 선택
    selectedSellEtfId.value = holding.etfId
    quantity.value = null
  }
}

// 매도 수량 검증
const validateSellQuantity = () => {
  if (!selectedHolding.value || !quantity.value) return
  if (quantity.value > selectedHolding.value.quantity) {
    quantity.value = selectedHolding.value.quantity
  }
}

// 매도 최대 수량 설정
const setMaxSellQuantity = () => {
  if (!selectedHolding.value) return
  quantity.value = selectedHolding.value.quantity
}

// === 매수 관련 로직 ===

// 검색 핸들러
const handleSearch = async () => {
  if (!searchKeyword.value.trim()) return
  try {
    const res = await searchEtfs({ keyword: searchKeyword.value })
    const data = res.data.content || res.data
    searchResults.value = data.slice(0, 5)
  } catch (e) {
    console.error(e)
    searchResults.value = []
  }
}

const selectEtf = (etf: any) => {
  selectedEtf.value = etf
  searchResults.value = []
  searchKeyword.value = ''
  quantity.value = null
}

// 매수 가능 금액
const availBalance = computed(() => props.balance)

// 매수 최대 수량 설정
const setMaxQuantity = () => {
  if (!selectedEtf.value) return
  const price = selectedEtf.value.currentPrice
  quantity.value = Math.floor(availBalance.value / price)
}

// === 공통 로직 ===

// 총 주문 금액
const totalOrderAmount = computed(() => {
  if (!quantity.value || quantity.value <= 0) return 0

  if (activeTab.value === 'BUY') {
    const price = selectedEtf.value?.currentPrice ?? 0
    return price * quantity.value
  } else {
    if (!selectedHolding.value) return 0
    return selectedHolding.value.currentPrice * quantity.value
  }
})

// 주문 가능 여부
const canSubmit = computed(() => {
  if (!quantity.value || quantity.value <= 0) return false

  if (activeTab.value === 'BUY') {
    if (!selectedEtf.value) return false
    return totalOrderAmount.value <= availBalance.value
  } else {
    if (!selectedHolding.value) return false
    if (quantity.value > selectedHolding.value.quantity) return false
    return true
  }
})

// 주문 실행
const onSubmit = async () => {
  if (!canSubmit.value) return
  
  isSubmitting.value = true
  try {
    if (activeTab.value === 'SELL') {
      if (!selectedHolding.value) {
        alert('매도할 종목을 선택해주세요.')
        return
      }

      await executeTrade({
        etfId: selectedHolding.value.etfId,
        tradeType: 'SELL',
        quantity: quantity.value!
      })
      
      alert('매도 주문이 체결되었습니다.')
    } else {
      // 매수
      if (!selectedEtf.value) {
        alert('매수할 종목을 선택해주세요.')
        return
      }

      await executeTrade({
        etfId: selectedEtf.value.etfId,
        tradeType: 'BUY',
        quantity: quantity.value!
      })
      
      alert('매수 주문이 체결되었습니다.')
    }
    
    emit('order-success')
    
    // 상태 초기화
    quantity.value = null
    if (activeTab.value === 'SELL') {
      selectedSellEtfId.value = null
    } else {
      selectedEtf.value = null
    }
  } catch (err: any) {
    const msg = err.response?.data || err.message
    alert(`${activeTab.value === 'BUY' ? '매수' : '매도'} 주문 실패: ${msg}`)
  } finally {
    isSubmitting.value = false
  }
}

const formatNumber = (num: number) => {
  return num?.toLocaleString('ko-KR') ?? '0'
}
</script>
