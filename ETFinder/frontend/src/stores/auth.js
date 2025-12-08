import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import http from '@/util/http-common'
import router from '@/router'

export const useAuthStore = defineStore('auth', () => {
  // 상태(State)
  const token = ref(localStorage.getItem('accessToken') || null)
  const user = ref(null) // 사용자 정보 (닉네임 등)

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

  const getMyInfo = function(){
    http.get('/users/me')
    .then((res)=>{
        user.value = res.data
    })
  }

  // Getters (토큰이 있는지 확인)
  const isAuthenticated = computed(() => !!token.value)

  return { token, user, kakaoLogin, logout, isAuthenticated, getMyInfo}
})