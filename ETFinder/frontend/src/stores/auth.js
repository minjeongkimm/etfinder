import router from '@/router'
import http from '@/util/http-common'
import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  // 상태(State)
  const token = ref(localStorage.getItem('accessToken') || null)
  const user = ref(null) // 사용자 정보 (닉네임 등)

  const ADMIN_PROVIDER_ID = "4604028154";

  // 동작(Actions)
  const kakaoLogin = async (code) => {
    try {
      // 1. 백엔드로 인가 코드 전달 (GET 요청)
      // 백엔드 컨트롤러 주소: /api/auth/kakao/callback?code=...
      const response = await http.get(`/auth/kakao/callback?code=${code}`)
      
      // 2. 백엔드가 준 데이터 받기
      const { accessToken, message } = response.data
      
      // 3. 토큰 저장 (Pinia 상태 + 로컬 스토리지)
      token.value = accessToken
      localStorage.setItem('accessToken', accessToken)
      
      console.log('로그인 성공:', message)
      
      // 4. 메인 페이지로 이동
      router.replace('/')
      
    } catch (error) {
      console.error('로그인 실패:', error)
      alert('로그인에 실패했습니다.')
      router.replace('/login')
    }
  }

  const logout = () => {
    token.value = null
    user.value = null
    localStorage.removeItem('accessToken')
    // 로그아웃 후 홈으로 보내기
    router.push('/')
  }

  const getMyInfo = async () => {
    try {
      const response = await http.get('/users/me')
      user.value = response.data
      return response.data
    } catch (error) {
      console.error('사용자 정보 조회 실패:', error)
      throw error
    }
  }

  const isAdmin = computed(() => {
    // 1. 유저 정보가 없으면 false
    if (!user.value) return false;

    // 2. 내 providerId가 관리자 ID랑 똑같은지 확인 (문자열 비교)
    return String(user.value.providerId) === ADMIN_PROVIDER_ID;
  });

  // Getters (토큰이 있는지 확인)
  const isAuthenticated = computed(() => !!token.value)

  return { token, user, kakaoLogin, logout, isAuthenticated, getMyInfo, isAdmin}
})