import { useAuthStore } from '@/stores/auth'
import EtfCreateView from '@/views/etf/EtfCreateView.vue'
import EtfDetailView from '@/views/etf/EtfDetailView.vue'
import EtfEditView from '@/views/etf/EtfEditView.vue'
import EtfSearchView from '@/views/EtfSearchView.vue'
import KakaoCallback from '@/views/KakaoCallback.vue'
import LikeView from '@/views/LikeView.vue'
import LoginView from '@/views/LoginView.vue'
import MyPageView from '@/views/MyPageView.vue'
import PortfolioView from '@/views/PortfolioView.vue'
import RecommendResultView from '@/views/RecommendResultView.vue'
import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/auth/callback',
      name: 'kakao-callback',
      component: KakaoCallback
    },
    {
      path: '/etfs',
      name: 'etfSearch',
      component: EtfSearchView,
    },
    {
      path: '/etfs/new',
      name: 'etfCreate',
      component: EtfCreateView,
      meta: { requiresAuth: true }
    },
    {
      path: '/etfs/:etfId',
      name: 'etfDetail',
      component: EtfDetailView,
    },
    {
      path: '/etfs/:etfId/edit',
      name: 'etfEdit',
      component: EtfEditView,
      meta: { requiresAuth: true }
    },
    {
      path: '/users/me',
      name: 'myPage',
      component: MyPageView,
      meta: { requiresAuth: true }
    },
    {
      path: '/portfolios',
      name: 'portfolio',
      component: PortfolioView,
      meta: { requiresAuth: true }
    },
    {
      path: '/likes',
      name: 'like',
      component: LikeView,
      meta: { requiresAuth: true }
    },
    {
      path: '/etfs/recommend',
      name: 'recommend',
      component: RecommendResultView,
      meta: { requiresAuth: true }
    },
  ],
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  
  // 1. 가려는 곳이 'requiresAuth' 딱지가 붙어있는지 확인
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    // 2. 로그인 안 했으면 팝업 띄우고 로그인 페이지로 보내기
    alert('로그인이 필요한 서비스입니다.')
    next({ name: 'login' })
  } else {
    // 3. 문제없으면 통과
    next()
  }
})

export default router
