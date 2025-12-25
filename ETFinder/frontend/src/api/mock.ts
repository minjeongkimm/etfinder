import type { DashboardResponse, TradeRequest } from "@/types/mock";
import http from "@/util/http-common"; // Assuming this resolves correctly in Vite even if it's .js

// 분 단위 자산 포인트 타입
export interface MinuteAssetPoint {
    baseDatetime: string;
    totalAsset: number;
}

// 분 단위 추이 응답 타입
export interface MinuteTrendResponse {
    points: MinuteAssetPoint[];
}

// GET /api/wallet/dashboard
export const getDashboard = async (): Promise<DashboardResponse> => {
    const response = await http.get("/wallet/dashboard");
    return response.data;
};

// POST /api/trades
export const executeTrade = async (request: TradeRequest): Promise<string> => {
    const response = await http.post("/trades", request);
    return response.data; // "Total trade amount: ..." or message
};

// DELETE /api/wallet (Reset)
export const resetWallet = async (): Promise<string> => {
    const response = await http.delete("/wallet");
    return response.data;
};

// GET /api/wallet/asset-trend/minute?minutes=60 (분 단위 자산 추이)
export const getMinuteTrend = async (minutes: number = 60): Promise<MinuteTrendResponse> => {
    const response = await http.get(`/wallet/asset-trend/minute?minutes=${minutes}`);
    return response.data;
};
