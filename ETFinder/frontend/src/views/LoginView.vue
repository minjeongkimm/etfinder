<template>
  <div>
    <h1>로그인 페이지</h1>
    <img :src="kakaoLoginBtn" alt="카카오 로그인" @click="kakaoLogin" class="kakao-img-btn"/>
  </div>
</template>

<script setup>
import kakaoLoginBtn from '@/assets/images/kakao_login_medium_wide.png';

// import.meta.env 객체를 통해 .env 파일의 값을 가져옴
const REST_API_KEY = import.meta.env.VITE_KAKAO_API_KEY;
const REDIRECT_URI = import.meta.env.VITE_REDIRECT_URI;

const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`;

const kakaoLogin = () => {
  window.location.href = kakaoAuthUrl;
}
</script>

<style scoped>
.kakao-img-btn{
  cursor: pointer;
}

</style>