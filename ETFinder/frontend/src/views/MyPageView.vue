<template>
  <div>
    <h3>MyPage</h3>

    <div v-if="store.user">
      <div class="profile-card">
        <h3>{{ store.user.nickname }}님 안녕하세요!</h3>
        <p><strong>투자 성향:</strong>{{ store.user.propensity }}</p>
        <p><strong>나이:</strong> {{ store.user.age }}세</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth';
import { onMounted } from 'vue';

const store = useAuthStore()

console.log("내 스토어:", store)

onMounted(()=>{
    store.getMyInfo()
})
</script>

<style scoped>

</style>