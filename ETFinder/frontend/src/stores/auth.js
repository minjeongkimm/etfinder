import router from '@/router'
import http from '@/util/http-common'
import { getRoleFromToken } from '@/util/jwt-helper'
import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  // 상태(State)
  const token = ref(localStorage.getItem('accessToken') || null)
  const user = ref(null) // 사용자 정보 (닉네임 등)
  const role = ref(null) // 사용자 권한 (ROLE_USER, ROLE_ADMIN)

  // 초기화: 토큰이 있으면 role 추출
  if (token.value) {
    role.value = getRoleFromToken(token.value)
  }

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
      
      // 4. JWT에서 role 추출
      role.value = getRoleFromToken(accessToken)
      console.log('로그인 성공:', message, '/ Role:', role.value)
      
      // 5. 메인 페이지로 이동
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
    role.value = null
    localStorage.removeItem('accessToken')
    console.log('로그아웃 완료')
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

  // 관리자 권한 체크: JWT 토큰의 role이 "ROLE_ADMIN"인지 확인
  const isAdmin = computed(() => {
    return role.value === 'ROLE_ADMIN'
  })

  // Getters (토큰이 있는지 확인)
  const isAuthenticated = computed(() => !!token.value)

  return { token, user, role, kakaoLogin, logout, isAuthenticated, getMyInfo, isAdmin}
})