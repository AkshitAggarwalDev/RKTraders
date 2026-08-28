"use client";

import { createContext, useCallback, useContext, useEffect, useState, type ReactNode } from "react";
import { errorMessage } from "@/lib/api/client";
import { authService, customerService } from "@/lib/api/services";
import { AUTH_CHANGED_EVENT, session } from "@/lib/api/session";
import type { Customer } from "@/lib/api/types";

type AuthContextValue = {
  customer: Customer | null;
  isAuthenticated: boolean;
  isCustomer: boolean;
  isRestoring: boolean;
  error: string | null;
  refreshSession: () => Promise<void>;
  logout: () => void;
};

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [customer, setCustomer] = useState<Customer | null>(null);
  const [isRestoring, setIsRestoring] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const refreshSession = useCallback(async () => {
    if (!session.hasToken()) { setCustomer(null); setError(null); setIsRestoring(false); return; }
    setIsRestoring(true);
    try {
      const profile = await customerService.profile();
      if (profile.role !== "CUSTOMER") throw new Error("This area is available to customer accounts only.");
      setCustomer(profile);
      setError(null);
    } catch (reason) {
      session.clear();
      setCustomer(null);
      setError(errorMessage(reason));
    } finally { setIsRestoring(false); }
  }, []);

  useEffect(() => {
    void refreshSession();
    const handleChange = () => { void refreshSession(); };
    window.addEventListener(AUTH_CHANGED_EVENT, handleChange);
    return () => window.removeEventListener(AUTH_CHANGED_EVENT, handleChange);
  }, [refreshSession]);

  const logout = useCallback(() => { authService.logout(); setCustomer(null); setError(null); }, []);
  const value = { customer, isAuthenticated: Boolean(customer), isCustomer: customer?.role === "CUSTOMER", isRestoring, error, refreshSession, logout };
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error("AuthProvider missing");
  return context;
}
