import { ref } from 'vue'
import { defineStore } from 'pinia'
import * as bookmarkApi from '@/api/bookmark'
import * as etfApi from '@/api/etf'

export const useBookmarkStore = defineStore('bookmark', () => {
  // 상태(State)
  const bookmarks = ref([]) // Bookmark 객체 배열
  const bookmarkedEtfs = ref([]) // ETF 상세 정보 배열
  const loading = ref(false)
  const error = ref(null)

  // 북마크 목록 조회
  const fetchBookmarks = async () => {
    loading.value = true
    error.value = null
    try {
      const response = await bookmarkApi.getMyBookmarks()
      bookmarks.value = response.data || []
      
      // 각 북마크의 ETF 상세 정보 조회
      if (bookmarks.value.length > 0) {
        const etfPromises = bookmarks.value.map(bookmark => 
          etfApi.getEtfDetail(bookmark.etfId)
        )
        const etfResponses = await Promise.all(etfPromises)
        bookmarkedEtfs.value = etfResponses.map(res => res.data)
      } else {
        bookmarkedEtfs.value = []
      }
    } catch (err) {
      error.value = err.response?.data || '북마크 목록을 불러오는데 실패했습니다.'
      bookmarks.value = []
      bookmarkedEtfs.value = []
    } finally {
      loading.value = false
    }
  }

  // 북마크 추가
  const addBookmark = async (etfId) => {
    try {
      await bookmarkApi.addBookmark(etfId)
      // 목록 새로고침
      await fetchBookmarks()
      return { success: true, message: '포트폴리오에 추가되었습니다.' }
    } catch (err) {
      const message = err.response?.data || '북마크 추가에 실패했습니다.'
      return { success: false, message }
    }
  }

  // 북마크 해제
  const removeBookmark = async (etfId) => {
    try {
      await bookmarkApi.removeBookmark(etfId)
      // 목록 새로고침
      await fetchBookmarks()
      return { success: true, message: '포트폴리오에서 제거되었습니다.' }
    } catch (err) {
      const message = err.response?.data || '북마크 해제에 실패했습니다.'
      return { success: false, message }
    }
  }

  // 북마크 여부 확인
  const isBookmarked = (etfId) => {
    return bookmarks.value.some(bookmark => bookmark.etfId === etfId)
  }

  return {
    bookmarks,
    bookmarkedEtfs,
    loading,
    error,
    fetchBookmarks,
    addBookmark,
    removeBookmark,
    isBookmarked
  }
})
