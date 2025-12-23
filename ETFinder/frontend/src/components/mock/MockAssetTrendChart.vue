<template>
  <div class="rounded-xl border border-border bg-card shadow-sm p-6 h-full flex flex-col">
    <h3 class="text-lg font-semibold text-foreground mb-1">자산 추이</h3>
    <p class="text-sm text-muted-foreground mb-4">최근 30일간의 자산 변동 내역입니다.</p>
    
    <div class="w-full min-h-[350px] flex-1 flex flex-col justify-center">
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
  
  // 변동이 작을 때 (20만원 미만) -> 위아래 10만원 고정 범위 사용
  if (maxValue - minValue < 200000) {
    const avg = (minValue + maxValue) / 2
    const fixedPadding = 100000 // 10만원
    return {
      min: Math.floor((avg - fixedPadding) / 10000) * 10000, // 1만원 단위
      max: Math.ceil((avg + fixedPadding) / 10000) * 10000
    }
  }
  
  // 변동이 클 때 -> 변동폭의 10% 패딩
  const range = maxValue - minValue
  const padding = range * 0.1 
  return {
    min: Math.floor((minValue - padding) / 10000) * 10000,
    max: Math.ceil((maxValue + padding) / 10000) * 10000
  }
}

const chartOptions = ref({
  chart: {
    type: 'area' as const,
    height: 350,
    zoom: { 
      enabled: true, 
      type: 'x' as const, 
      autoScaleYaxis: true  // Y축 자동 스케일링 활성화
    },
    toolbar: { 
      show: false
    },
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
      style: { colors: '#64748b', fontSize: '12px' },
      formatter: (value: any, timestamp?: number) => {
        // timestamp가 있으면 사용, 없으면 value 사용
        const date = timestamp ? new Date(timestamp) : new Date(value)
        
        // 원본 데이터가 ISO datetime (T 포함) 형식인지 확인
        // props.trendData 첫 요소의 baseDate를 체크
        const isMinuteMode = props.trendData && props.trendData.length > 0 && 
                             props.trendData[0].baseDate.includes('T')
        
        if (isMinuteMode) {
          // 분 단위 모드: HH:mm 형식
          const hours = date.getHours().toString().padStart(2, '0')
          const minutes = date.getMinutes().toString().padStart(2, '0')
          return `${hours}:${minutes}`
        } else {
          // 일별 모드: MM/dd 형식
          return `${date.getMonth() + 1}/${date.getDate()}`
        }
      }
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
    x: { 
      formatter: (value: number) => {
        const date = new Date(value)
        
        // 원본 데이터가 ISO datetime 형식인지 확인
        const isMinuteMode = props.trendData && props.trendData.length > 0 && 
                             props.trendData[0].baseDate.includes('T')
        
        if (isMinuteMode) {
          // 분 단위 모드: "HH:mm" 형식
          const hours = date.getHours().toString().padStart(2, '0')
          const minutes = date.getMinutes().toString().padStart(2, '0')
          return `${hours}:${minutes}`
        } else {
          // 일별 모드: "yyyy년 MM월 dd일" 형식
          return `${date.getFullYear()}년 ${date.getMonth() + 1}월 ${date.getDate()}일`
        }
      }
    },
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
