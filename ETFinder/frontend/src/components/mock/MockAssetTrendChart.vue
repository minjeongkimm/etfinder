<template>
  <div class="rounded-xl border border-border bg-card shadow-sm p-6">
    <h3 class="text-lg font-semibold text-foreground mb-1">자산 추이</h3>
    <p class="text-sm text-muted-foreground mb-4">최근 30일간의 자산 변동 내역입니다.</p>
    
    <div class="w-full h-full min-h-[350px]">
      <div v-if="!trendData || trendData.length === 0" class="flex flex-col justify-center items-center h-[350px] bg-muted/30 rounded-lg">
        <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-muted-foreground/50 mb-4">
          <path d="M3 3v18h18"/>
          <path d="m19 9-5 5-4-4-3 3"/>
        </svg>
        <div class="text-center">
          <div class="text-base font-medium text-muted-foreground mb-1">아직 자산 데이터가 없습니다</div>
          <div class="text-sm text-muted-foreground">거래를 시작하면 차트가 표시됩니다</div>
        </div>
      </div>
      
      <div v-else class="chart-container">
        <apexchart
          type="area"
          height="350"
          :options="chartOptions"
          :series="series"
        ></apexchart>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { DailyAssetPoint } from '@/types/mock';
import { ref, watchEffect } from 'vue';
import VueApexCharts from 'vue3-apexcharts';

const apexchart = VueApexCharts

const props = defineProps<{
  trendData: DailyAssetPoint[]
}>()

const series = ref<{name: string, data: {x: number, y: number}[]}[]>([])

// 동적 Y축 범위 계산 함수
const calculateYAxisRange = (dataPoints: {x: number, y: number}[]) => {
  if (!dataPoints || dataPoints.length === 0) {
    return { min: 0, max: 10000000 }
  }
  
  const values = dataPoints.map(p => p.y)
  const minValue = Math.min(...values)
  const maxValue = Math.max(...values)
  
  // 변동이 없거나 매우 작은 경우 (±10% 범위)
  if (maxValue - minValue < 100000) {
    const avg = (minValue + maxValue) / 2
    const padding = avg * 0.1 // ±10%
    return {
      min: Math.floor((avg - padding) / 100000) * 100000, // 10만원 단위로 반올림
      max: Math.ceil((avg + padding) / 100000) * 100000
    }
  }
  
  // 변동이 있는 경우
  const range = maxValue - minValue
  const padding = range * 0.1 // ±10% 패딩
  return {
    min: Math.floor((minValue - padding) / 100000) * 100000,
    max: Math.ceil((maxValue + padding) / 100000) * 100000
  }
}

const chartOptions = ref({
  chart: {
    type: 'area' as const,
    height: 350,
    zoom: { enabled: true, type: 'x' as const, autoScaleYaxis: false }, // autoScale 비활성화
    toolbar: { show: false },
    fontFamily: 'Inter, sans-serif',
    animations: {
      enabled: true,
      easing: 'easeinout' as const,
      speed: 800,
      animateGradually: {
        enabled: true,
        delay: 150
      },
      dynamicAnimation: {
        enabled: true,
        speed: 350
      }
    }
  },
  dataLabels: { enabled: false },
  stroke: {
    curve: 'smooth' as const,
    width: 2,
    lineCap: 'round' as const
  },
  fill: {
    type: 'gradient' as const,
    gradient: {
      shadeIntensity: 1,
      opacityFrom: 0.7,
      opacityTo: 0.2,
      stops: [0, 90, 100]
    }
  },
  xaxis: {
    type: 'datetime' as const,
    tooltip: { enabled: true },
    labels: {
      style: { colors: '#64748b', fontSize: '12px' }
    }
  },
  yaxis: {
    min: 0,
    max: 10000000,
    labels: {
      formatter: (value: number) => value.toLocaleString() + '원', // '원' 단위 추가
      style: { colors: '#64748b', fontSize: '12px' }
    }
  },
  theme: {
    mode: 'light' as const,
    monochrome: {
      enabled: true,
      color: '#2563eb',
      shadeTo: 'light' as const,
      shadeIntensity: 0.65
    },
  },
  grid: {
    borderColor: '#e2e8f0',
    strokeDashArray: 4,
  },
  tooltip: {
    x: { format: 'yyyy년 MM월 dd일' },
    y: {
      formatter: (value: number) => value.toLocaleString() + '원'
    }
  }
})

watchEffect(() => {
  if (props.trendData && props.trendData.length > 0) {
    const data = props.trendData
      .slice()
      .sort((a, b) => new Date(a.baseDate).getTime() - new Date(b.baseDate).getTime())
      .map(point => ({
        x: new Date(point.baseDate).getTime(),
        y: point.totalAsset
      }))
    
    series.value = [{
      name: '총 자산',
      data: data
    }]
    
    // Y축 범위 동적 계산 및 업데이트
    const yRange = calculateYAxisRange(data)
    chartOptions.value.yaxis.min = yRange.min
    chartOptions.value.yaxis.max = yRange.max
  } else {
    series.value = []
  }
})
</script>

<style scoped>
.chart-container {
  width: 100%;
}
</style>
