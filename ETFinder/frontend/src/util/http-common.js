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

export default http;