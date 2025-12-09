<template>
  <div class="search-container">
    <div class="header-row">
      <h2>ETF 검색</h2>
      
      <button 
        v-if="authStore.isAdmin" 
        class="admin-btn register-btn"
        @click="goToRegisterPage"
      >
        + ETF 등록
      </button>
    </div>

    <div class="input-area">
      <input type="text" v-model="keyword" @keyup.enter="searchEtfs" />
      <button @click="searchEtfs">검색</button>
    </div>

    <div class="result-area">
      <ul v-if="etfList.length > 0">
        <li v-for="etf in etfList" :key="etf.etfId" class="etf-item">
          
          <div class="etf-info-left">
            <strong>{{ etf.etfName }}</strong> 
            <span class="code">{{ etf.etfCode }}</span>
          </div>

          <div class="etf-info-center">
            <span :class="{'plus': etf.return1mo > 0, 'minus': etf.return1mo < 0}">
              1개월: {{ etf.return1mo }}%
            </span>
            <br>
            <small>1년: {{ etf.return1yr }}%</small>
          </div>

          <div v-if="authStore.isAdmin" class="admin-actions">
            <button @click="openEditModal(etf)">수정</button>
            <button @click="handleDelete(etf.etfCode)" class="delete-btn">삭제</button>
          </div>

        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth'; // 스토어 가져오기
import { getEtfList, deleteEtf } from '@/api/etf';

const authStore = useAuthStore(); // 스토어 사용
const keyword = ref('');
const etfList = ref([]);

const searchEtfs = async () => {
  try {
    console.log("검색 요청 시작! 검색어:", keyword.value); // 로그 찍어서 확인

    // 1. API 호출 (etf.js에 있는 함수 실행)
    const response = await getEtfList(keyword.value);
    
    // 2. 받아온 데이터를 화면 변수에 넣기
    etfList.value = response.data;
    
    console.log("받아온 데이터:", response.data); // 데이터 들어왔는지 확인

  } catch (error) {
    console.error("에러 발생:", error);
    alert("검색 중 오류가 발생했습니다.");
  }
};

// 관리자용 삭제 함수
const handleDelete = async (code) => {
  if(!confirm('정말 이 ETF를 삭제하시겠습니까?')) return;

  try {
    await deleteEtf(code); // API 호출
    alert('삭제되었습니다.');
    searchEtfs(); // 목록 새로고침
  } catch (error) {
    console.error(error);
    alert('삭제 실패!');
  }
};

// 등록 페이지 이동 or 모달 열기
const goToRegisterPage = () => {
    // router.push('/admin/register') 처럼 이동하거나 모달 띄우기
    alert("등록 모달을 띄웁니다!");
};

const openEditModal = (etf) => {
    alert(`${etf.name} 수정 모달을 띄웁니다!`);
};
</script>

<style scoped>
.header-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.admin-btn {
    background-color: #333;
    color: white;
    padding: 8px 16px;
    border-radius: 4px;
}
.etf-item {
    display: flex;
    justify-content: space-between; /* 정보는 왼쪽, 관리자버튼은 오른쪽 */
    padding: 15px;
    border-bottom: 1px solid #eee;
}
.delete-btn {
    background-color: #ff4d4f;
    color: white;
    border: none;
    margin-left: 5px;
}
</style>