import type { DashboardResponse, TradeRequest } from "@/types/mock";
import http from "@/util/http-common"; // Assuming this resolves correctly in Vite even if it's .js

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
