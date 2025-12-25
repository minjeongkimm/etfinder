import http from '@/util/http-common'


/**
 * 시뮬레이션 실행
 * @param {Object} simulationRequest - 시뮬레이션 요청 데이터
 * @param {number} simulationRequest.investmentAmount - 총 투자금액
 * @param {Array} simulationRequest.portfolio - 포트폴리오 배열 (선택사항)
 * @param {number} simulationRequest.portfolio[].etfId - ETF ID
 * @param {number} simulationRequest.portfolio[].ratio - 비중 (%)
 * @returns {Promise} 시뮬레이션 결과
 */
export const runSimulation = (payload) => http.post('/simulation', payload)

