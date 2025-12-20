<template>
  <div class="rounded-xl border border-border bg-card shadow-sm p-6">
    <h3 class="text-lg font-semibold text-foreground mb-4">보유 종목 현황</h3>
    
    <div v-if="holdings.length === 0" class="text-center py-12 text-muted-foreground">
      보유 중인 ETF가 없습니다.
    </div>

    <div v-else class="space-y-4">
      <div 
        v-for="item in holdings" 
        :key="item.etfId"
        class="flex flex-col sm:flex-row sm:items-center justify-between p-4 rounded-lg bg-muted/30 border border-border gap-4"
      >
        <!-- 왼쪽: 종목 정보 -->
        <div class="flex items-start gap-3">
          <!-- 종목 아이콘/이니셜 (optional) -->
          <div class="w-10 h-10 rounded-full bg-background border border-border flex items-center justify-center font-bold text-muted-foreground shrink-0">
             {{ item.etfName.substring(0, 1) }}
          </div>
          <div>
            <div class="flex items-center gap-2">
              <span class="font-bold text-foreground">{{ item.etfName }}</span>
              <span class="text-xs px-1.5 py-0.5 rounded bg-muted text-muted-foreground font-mono">{{ item.etfCode }}</span>
            </div>
            <div class="text-sm text-muted-foreground mt-1">
              {{ item.quantity }}주 보유 · 평단 {{ formatNumber(item.averagePrice) }}원
            </div>
          </div>
        </div>

        <!-- 오른쪽: 평가 금액/손익 -->
        <div class="text-right">
          <div 
            :class="[
              'font-bold text-lg',
              item.profitRate >= 0 ? 'text-emerald-600' : 'text-rose-600'
            ]"
          >
            {{ item.profitRate >= 0 ? '+' : '' }}{{ item.profitRate.toFixed(1) }}% 
            <span class="text-sm font-normal">
              ({{ item.profitRate >= 0 ? '+' : '' }}{{ formatNumber(item.profitAmount) }}원)
            </span>
          </div>
          <div class="text-sm font-semibold text-slate-900 mt-1">
            {{ formatNumber(item.currentPrice) }}원
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { MockHoldingResponse } from '@/types/mock'

defineProps<{
  holdings: MockHoldingResponse[]
}>()

const formatNumber = (num: number) => {
  return num?.toLocaleString('ko-KR') ?? '0'
}
</script>
