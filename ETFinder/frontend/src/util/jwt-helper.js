/**
 * JWT 토큰 디코딩 유틸리티
 * jwt-decode 라이브러리 대신 직접 구현
 */

/**
 * JWT 토큰에서 payload를 디코딩합니다.
 * @param {string} token - JWT 토큰
 * @returns {object|null} 디코딩된 payload 객체 또는 null
 */
export function decodeJwt(token) {
  if (!token) return null

  try {
    // JWT 구조: header.payload.signature
    const parts = token.split('.')
    
    if (parts.length !== 3) {
      console.error('Invalid JWT token format')
      return null
    }

    // payload 부분 (두 번째 부분) 추출
    const payload = parts[1]
    
    // Base64URL 디코딩
    // Base64URL은 Base64와 달리 +를 -, /를 _로 치환하고 = 패딩을 제거
    const base64 = payload.replace(/-/g, '+').replace(/_/g, '/')
    
    // Base64 디코딩 후 JSON 파싱
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    )

    return JSON.parse(jsonPayload)
  } catch (error) {
    console.error('JWT decoding failed:', error)
    return null
  }
}

/**
 * JWT 토큰에서 role을 추출합니다.
 * @param {string} token - JWT 토큰
 * @returns {string|null} role 값 또는 null
 */
export function getRoleFromToken(token) {
  const payload = decodeJwt(token)
  return payload?.role || null
}

/**
 * JWT 토큰에서 providerId를 추출합니다.
 * @param {string} token - JWT 토큰
 * @returns {string|null} providerId 값 또는 null
 */
export function getProviderIdFromToken(token) {
  const payload = decodeJwt(token)
  return payload?.sub || null // JWT 표준에서 sub는 subject (사용자 식별자)
}

/**
 * JWT 토큰이 만료되었는지 확인합니다.
 * @param {string} token - JWT 토큰
 * @returns {boolean} 만료 여부
 */
export function isTokenExpired(token) {
  const payload = decodeJwt(token)
  if (!payload || !payload.exp) return true

  // exp는 Unix timestamp (초 단위)
  const expirationTime = payload.exp * 1000 // 밀리초로 변환
  return Date.now() >= expirationTime
}
