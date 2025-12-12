import axios from "axios";

// 1. baseURL 수정! 
// http://localhost:8080/api 라고 쓰지 말고 그냥 "/api" 라고 써야 
// vite.config.js의 proxy 설정이 작동해서 8080으로 납치해줌.
const http = axios.create({
  baseURL: "/api", 
  headers: {
    "Content-Type": "application/json",
  },
});

// 2. 요청 가기 전에 토큰 낚아채서 헤더에 집어넣기 (Interceptor)
http.interceptors.request.use(
  (config) => {
    // 로컬 스토리지에서 토큰 꺼내기
    const token = localStorage.getItem("accessToken");
    
    // 디버깅: 토큰 확인
    console.log('[HTTP Request]', config.method.toUpperCase(), config.url);
    console.log('[Auth Token]', token ? `있음 (${token.substring(0, 20)}...)` : '없음 ❌');
    
    // 토큰이 있으면 헤더에 'Bearer 토큰' 형태로 추가
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 3. 응답 인터셉터 - 401/403 처리
http.interceptors.response.use(
  (response) => {
    // 정상 응답은 그대로 반환
    return response;
  },
  (error) => {
    const status = error.response?.status;
    const url = error.config?.url;
    
    console.error(`[HTTP Error ${status}]`, url, error.response?.data);
    
    // 401 Unauthorized 에러 처리
    if (status === 401) {
      console.warn('❌ 인증 실패: 토큰이 없거나 만료되었습니다.');
      // 토큰 제거
      localStorage.removeItem("accessToken");
      
      // 로그인 페이지로 리다이렉트
      alert('로그인이 만료되었습니다. 다시 로그인해주세요.');
      window.location.href = "/login";
    }
    
    // 403 Forbidden 에러 처리
    if (status === 403) {
      console.error('❌ 권한 없음: 이 작업을 수행할 권한이 없습니다.');
      const token = localStorage.getItem("accessToken");
      if (!token) {
        alert('로그인이 필요합니다.');
        window.location.href = "/login";
      } else {
        alert('이 작업을 수행할 권한이 없습니다.');
      }
    }
    
    return Promise.reject(error);
  }
);

export default http;