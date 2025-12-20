<template>
  <div class="rounded-xl border border-border bg-card shadow-sm p-6 h-full">
    <h3 class="text-lg font-bold text-foreground mb-4 flex items-center gap-2">
      <svg class="w-5 h-5 text-muted-foreground" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
      최근 거래 내역
    </h3>

    <div v-if="trades.length === 0" class="text-center py-8 text-muted-foreground text-sm">
      거래 내역이 없습니다.
    </div>

    <div v-else class="space-y-3">
      <div 
        v-for="trade in trades" 
        :key="trade.tradeId"
        class="flex items-center justify-between py-2 border-b border-border last:border-0"
      >
        <div class="flex items-center gap-3">
          <!-- 매수/매도 뱃지 -->
          <span 
            :class="[
              'px-2 py-1 text-xs font-bold rounded',
              trade.tradeType === 'BUY' 
                ? 'bg-emerald-100 text-emerald-700' 
                : 'bg-rose-100 text-rose-700'
            ]"
          >
            {{ trade.tradeType === 'BUY' ? '매수' : '매도' }}
          </span>
          
          <div>
            <div class="font-bold text-sm text-foreground">{{ trade.etfName }}</div>
            <div class="text-xs text-muted-foreground">{{ formatDate(trade.createdAt) }}</div>
          </div>
        </div>

        <div class="text-right">
          <div class="font-bold text-sm text-foreground">{{ formatNumber(trade.price) }}원</div>
          <div class="text-xs text-muted-foreground">{{ trade.quantity }}주</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { TradeHistoryResponse } from '@/types/mock'

defineProps<{
  trades: TradeHistoryResponse[]
}>()

const formatNumber = (num: number) => {
  return num?.toLocaleString('ko-KR') ?? '0'
}

const formatDate = (isoString: string) => {
  const date = new Date(isoString)
  const mm = String(date.getMonth() + 1).padStart(2, '0')
  const dd = String(date.getDate()).padStart(2, '0')
  const hh = String(date.getHours()).padStart(2, '0')
  const min = String(date.getMinutes()).padStart(2, '0')
  return `${mm}.${dd} ${hh}:${min}`
}
</script>
