<template>
    <div>
        <nav>
            <router-link to="/">Home</router-link> |
            <template v-if="!authStore.isAuthenticated">
                <router-link :to="{name: 'login'}">로그인</router-link> |
            </template>

            <template v-else>
                <a href="#" @click.prevent="handleLogout">로그아웃</a> |
                <router-link :to="{name: 'myPage'}">마이페이지</router-link> |
            </template>
            <router-link :to="{name: 'etfSearch'}">etf 검색</router-link> |
            <router-link :to="{name: 'portfolio'}">포트폴리오</router-link> |
            <router-link :to="{name: 'myPage'}">마이페이지</router-link> |
            <router-link :to="{name: 'recommend'}">맞춤형 etf 추천</router-link> |
        </nav>
    </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const handleLogout = () => {
    // 스토어의 로그아웃 함수 실행
    authStore.logout();
    alert("로그아웃 되었습니다.");
    // 홈으로 보내기
    router.push('/');
};
</script>

<style scoped>

</style>