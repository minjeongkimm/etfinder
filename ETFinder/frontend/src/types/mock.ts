export interface WalletResponse {
  balance: number;
  totalAsset: number;
  realizedProfit: number;
  cashRatio?: number;
  stockRatio?: number;
}

export interface DailyAssetPoint {
  baseDate: string; // 'YYYY-MM-DD'
  totalAsset: number;
  realizedProfit: number;
}

export interface MockHoldingResponse {
  etfId: number;
  etfCode: string;
  etfName: string;
  quantity: number;
  averagePrice: number;
  currentPrice: number;
  evalAmount: number;
  profitAmount: number;
  profitRate: number;
}

export interface TradeHistoryResponse {
  tradeId: number;
  etfId: number;
  etfName: string;
  tradeType: 'BUY' | 'SELL';
  price: number;
  quantity: number;
  amount: number;
  createdAt: string; // ISO string
}

export interface MockRanking {
  userId: number;
  nickname: string;
  totalAsset: number;
  returnRate: number;
  rank: number;
}

export interface MockRankingResponse {
  myRank: number;
  totalUsers: number;
  topPercent: number;
  topUsers: MockRanking[];
}

export interface DashboardResponse {
  walletResponse: WalletResponse;
  assetTrend: DailyAssetPoint[];
  holdings: MockHoldingResponse[];
  recentTrades: TradeHistoryResponse[];
  ranking: MockRankingResponse;
}

export interface TradeRequest {
  etfId: number;
  tradeType: 'BUY' | 'SELL';
  quantity: number;
}
