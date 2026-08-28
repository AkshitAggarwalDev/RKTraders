import axios from "axios";
import { session } from "./session";
import type { ApiErrorBody } from "./types";
export const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";
export class ApiError extends Error { constructor(public readonly status?: number, message = "We could not complete that request. Please try again.") { super(message); } }
export const apiClient = axios.create({ baseURL: API_BASE_URL, headers: { "Content-Type": "application/json" } });
apiClient.interceptors.request.use((config) => { const token = session.token(); if (token) config.headers.Authorization = `Bearer ${token}`; return config; });
apiClient.interceptors.response.use((response) => response, (error: unknown) => {
  if (axios.isAxiosError(error)) { const status = error.response?.status; const body = error.response?.data as ApiErrorBody | string | undefined; const responseMessage = typeof body === "string" ? body : body?.message; const message = responseMessage || (status === 401 ? "Your session has expired. Please sign in again." : status === 403 ? "You do not have permission to perform this action." : undefined); if (status === 401) session.clear(); return Promise.reject(new ApiError(status, message)); }
  return Promise.reject(error);
});
export function errorMessage(error: unknown) { if (error instanceof ApiError) return error.message; if (error instanceof Error && error.message) return error.message; return "We could not complete that request. Please try again."; }
