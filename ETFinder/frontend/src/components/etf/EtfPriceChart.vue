<template>
  <div class="w-full h-full">
    <!-- 로딩 상태 -->
    <div v-if="loading" class="flex justify-center items-center h-80 bg-muted/30 rounded-lg">
      <div class="text-muted-foreground animate-pulse">차트 데이터를 불러오는 중...</div>
    </div>

    <!-- 에러 상태 -->
    <div v-else-if="error" class="flex justify-center items-center h-80 bg-muted/30 rounded-lg">
      <div class="text-destructive">{{ error }}</div>
    </div>

    <!-- 데이터 없음 -->
    <div v-else-if="!series || series.length === 0 || series[0].data.length === 0" class="flex justify-center items-center h-80 bg-muted/30 rounded-lg">
      <div class="text-muted-foreground">표시할 차트 데이터가 없습니다.</div>
    </div>

    <!-- 차트 -->
    <div v-else class="chart-container">
      <apexchart
        type="area"
        height="350"
        :options="chartOptions"
        :series="series"
      ></apexchart>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { getEtfPriceHistory } from '@/api/etf';
import VueApexCharts from 'vue3-apexcharts';

// 컴포넌트 등록 (로컬 등록)
const apexchart = VueApexCharts;

const props = defineProps({
  etfId: {
    type: Number,
    required: true
  }
});

const loading = ref(false);
const error = ref(null);
const series = ref([]);
const chartOptions = ref({
  chart: {
    type: 'area',
    height: 350,
    zoom: {
      enabled: true, // 줌 기능 활성화
      type: 'x',
      autoScaleYaxis: true
    },
    toolbar: {
      show: false, // 툴바 숨김
    },
    fontFamily: 'Inter, sans-serif',
  },
  dataLabels: {
    enabled: false
  },
  stroke: {
    curve: 'smooth',
    width: 2
  },
  fill: {
    type: 'gradient',
    gradient: {
      shadeIntensity: 1,
      opacityFrom: 0.7,
      opacityTo: 0.2,
      stops: [0, 90, 100]
    }
  },
  xaxis: {
    type: 'datetime',
    tooltip: {
      enabled: true
    },
    labels: {
      style: {
        colors: '#64748b', // text-slate-500
        fontSize: '12px'
      }
    }
  },
  yaxis: {
    labels: {
      formatter: function (value) {
        return value.toLocaleString();
      },
      style: {
        colors: '#64748b',
        fontSize: '12px'
      }
    }
  },
  theme: {
    mode: 'light', 
    palette: 'palette1', 
    monochrome: {
      enabled: true,
      color: '#2563EB', // primary color (Blue-600)
      shadeTo: 'light',
      shadeIntensity: 0.65
    },
  },
  grid: {
    borderColor: '#e2e8f0', // border-slate-200
    strokeDashArray: 4,
  },
  tooltip: {
    x: {
      format: 'yyyy년 MM월 dd일'
    },
    y: {
      formatter: function (value) {
        return value.toLocaleString() + '원';
      }
    }
  } 
});

const fetchChartData = async () => {
  if (!props.etfId) return;

  loading.value = true;
  error.value = null;

  try {
    const response = await getEtfPriceHistory(props.etfId);
    const historyData = response.data; // List<EtfDailyHistory>

    if (historyData && historyData.length > 0) {
        // Area 차트용 데이터 변환: [[timestmap, price], ...]
        // 시간순 정렬 보장 (보통 DB에서 정렬되어 오지만 안전하게)
        const sortedData = historyData.sort((a, b) => new Date(a.baseDate) - new Date(b.baseDate));

        const chartData = sortedData.map(item => {
            return {
                x: new Date(item.baseDate).getTime(),
                y: item.closePrice
            };
        });

        series.value = [{
            name: '종가',
            data: chartData
        }];
        
        // 차트 색상 파란색 통일
        // 기본 옵션에 설정된 monochrome.color (#2563eb)가 적용됨

    } else {
        series.value = [];
    }
    
  } catch (err) {
    console.error("차트 데이터 조회 실패:", err);
    error.value = "차트 데이터를 불러오는데 실패했습니다.";
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchChartData();
});

// etfId가 변경되면 다시 조회
watch(() => props.etfId, () => {
  fetchChartData();
});
</script>

<style scoped>
.chart-container {
  width: 100%;
  min-height: 350px;
}
</style>
