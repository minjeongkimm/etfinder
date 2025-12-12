import { ref } from 'vue'
import { defineStore } from 'pinia'
import * as likeApi from '@/api/like'

export const useLikeStore = defineStore('like', () => {
  // 상태(State)
  const likedEtfs = ref([]) // EtfProduct 객체 배열
  const loading = ref(false)
  const error = ref(null)

  // 좋아요 목록 조회
  const fetchLikes = async () => {
    loading.value = true
    error.value = null
    try {
      const response = await likeApi.getMyLikes()
      likedEtfs.value = response.data || []
    } catch (err) {
      error.value = err.response?.data || '좋아요 목록을 불러오는데 실패했습니다.'
      likedEtfs.value = []
    } finally {
      loading.value = false
    }
  }

  // 좋아요 추가
  const addLike = async (etfId) => {
    try {
      await likeApi.addLike(etfId)
      // 목록 새로고침
      await fetchLikes()
      return { success: true, message: '찜 목록에 추가되었습니다.' }
    } catch (err) {
      const message = err.response?.data || '좋아요 추가에 실패했습니다.'
      return { success: false, message }
    }
  }

  // 좋아요 해제
  const removeLike = async (etfId) => {
    try {
      await likeApi.removeLike(etfId)
      // 목록 새로고침
      await fetchLikes()
      return { success: true, message: '찜 목록에서 제거되었습니다.' }
    } catch (err) {
      const message = err.response?.data || '좋아요 해제에 실패했습니다.'
      return { success: false, message }
    }
  }

  // 좋아요 여부 확인
  const isLiked = (etfId) => {
    return likedEtfs.value.some(etf => etf.etfId === etfId)
  }

  // 좋아요 토글
  const toggleLike = async (etfId) => {
    if (isLiked(etfId)) {
      return await removeLike(etfId)
    } else {
      return await addLike(etfId)
    }
  }

  return {
    likedEtfs,
    loading,
    error,
    fetchLikes,
    addLike,
    removeLike,
    isLiked,
    toggleLike
  }
})
