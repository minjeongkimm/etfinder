<template>
  <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
    <!-- 총 자산 평가액 -->
    <div class="rounded-xl border border-blue-200 bg-blue-50/50 shadow-sm p-6">
      <div class="text-sm text-slate-600 font-medium mb-2">총 자산 평가액</div>
      <div class="text-3xl font-bold text-slate-900 mb-2">
        {{ formatNumber(displayTotalAsset) }}원
      </div>
      <div 
        :class="[
          'text-sm font-semibold flex items-center gap-1',
          profitRate >= 0 ? 'text-emerald-600' : 'text-rose-600'
        ]"
      >
        <span>{{ profitRate >= 0 ? '↗' : '↘' }}</span>
        <span>{{ profitRate >= 0 ? '+' : '' }}{{ profitRate.toFixed(1) }}% ({{ formatNumber(profitAmount) }}원)</span>
      </div>
    </div>

    <!-- 가용 잔액 -->
    <div class="rounded-xl border border-emerald-200 bg-emerald-50/50 shadow-sm p-6">
      <div class="text-sm text-slate-600 font-medium mb-2">가용 잔액</div>
      <div class="text-3xl font-bold text-slate-900 mb-2">
        {{ formatNumber(wallet.balance) }}원
      </div>
      <div class="text-sm text-slate-500">
        즉시 주문 가능
      </div>
    </div>

    <!-- 실현 손익 -->
    <div class="rounded-xl border border-blue-200 bg-blue-50/50 shadow-sm p-6">
      <div class="text-sm text-slate-600 font-medium mb-2">실현 손익</div>
      <div 
        :class="[
          'text-3xl font-bold mb-2',
          wallet.realizedProfit > 0 ? 'text-emerald-600' : wallet.realizedProfit < 0 ? 'text-rose-600' : 'text-slate-900'
        ]"
      >
        {{ wallet.realizedProfit > 0 ? '+' : '' }}{{ formatNumber(wallet.realizedProfit) }}원
      </div>
      <div class="text-sm text-slate-500">
        이번 달 누적
      </div>
    </div>

    <!-- 나의 랭킹 -->
    <div class="rounded-xl border border-blue-200 bg-blue-50/50 shadow-sm p-6">
      <div class="text-sm text-slate-600 font-medium mb-2">나의 랭킹</div>
      <div class="text-3xl font-bold text-slate-900 mb-2">
        Top {{ ranking.topPercent }}%
      </div>
      <div class="text-sm text-slate-500">
        전체 {{ formatNumber(ranking.totalUsers) }}명 중 {{ formatNumber(ranking.myRank) }}위
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { MockRankingResponse, WalletResponse } from '@/types/mock';
import { computed } from 'vue';

const props = defineProps<{
  wallet: WalletResponse
  ranking: MockRankingResponse
  realtimeTotalAsset?: number
}>()

// 실시간 총 자산 또는 기본값 사용
const displayTotalAsset = computed(() => {
  return props.realtimeTotalAsset || props.wallet.totalAsset
})

// 초기 자본금 (가정: 10,000,000원 - 백엔드 로직에 따라 다를 수 있음. 
// 보통은 totalAsset - (totalAsset - profit) 이런 식이겠지만, 
// 여기서는 totalAsset과 초기자본의 차이를 보여줄 수도 있고,
// 단순히 totalAsset - realizedProfit이 아니라,
// (totalAsset - 10,000,000) / 10,000,000 * 100 로 계산하는게 일반적임 (모의투자 시)).
// 하지만 DTO에 profitRate가 없고, 목업에는 `+10.5% (1,050,000원)` 처럼 나옴.
// 10,000,000원이 초기 자금이라고 가정 (MockWalletService 로직상 보통 천만원)
const INITIAL_BALANCE = 10000000

const profitAmount = computed(() => {
  return displayTotalAsset.value - INITIAL_BALANCE
})

const profitRate = computed(() => {
  return (profitAmount.value / INITIAL_BALANCE) * 100
})

const formatNumber = (num: number) => {
  return num?.toLocaleString('ko-KR') ?? '0'
}
</script>
