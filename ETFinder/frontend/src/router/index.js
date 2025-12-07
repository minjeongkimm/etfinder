import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '@/views/LoginView.vue'
import KakaoCallback from '@/views/KakaoCallback.vue'
import EtfSearchView from '@/views/EtfSearchView.vue'
import MyPageView from '@/views/MyPageView.vue'
import PortfolioView from '@/views/PortfolioView.vue'
import RecommendResultView from '@/views/RecommendResultView.vue'

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
      path: '/users/me',
      name: 'myPage',
      component: MyPageView,
    },
    {
      path: '/bookmarks',
      name: 'portfolio',
      component: PortfolioView,
    },
    {
      path: '/etfs/recommend',
      name: 'recommend',
      component: RecommendResultView,
    },
  ],
})

export default router
