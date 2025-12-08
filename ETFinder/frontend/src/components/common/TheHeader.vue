<template>
    <div>
        <nav>
            <router-link to="/">Home</router-link> |
            <router-link :to="{name: 'etfSearch'}">etf 검색</router-link> |
            <router-link :to="{name: 'portfolio'}">포트폴리오</router-link> |
            <router-link :to="{name: 'myPage'}">마이페이지</router-link> |
            <router-link :to="{name: 'recommend'}">맞춤형 etf 추천</router-link> |
        </nav>
    </div>
</template>

<script setup>

</script>

<style scoped>

</style>