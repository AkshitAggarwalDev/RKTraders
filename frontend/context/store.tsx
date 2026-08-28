"use client";

import { createContext, useCallback, useContext, useEffect, useMemo, useState, type ReactNode } from "react";
import type { CartLine, Product } from "@/lib/data";
import { cartApi, errorMessage } from "@/lib/api";
import { useAuth } from "@/context/auth";

type Store = {
  cart: CartLine[];
  wish: number[];
  cartOpen: boolean;
  authenticated: boolean;
  loadingCart: boolean;
  notice: string | null;
  add: (item: Product) => Promise<boolean>;
  toggleWish: (id: number) => void;
  setCartOpen: (open: boolean) => void;
  remove: (id: number) => Promise<void>;
  updateQuantity: (id: number, quantity: number) => Promise<void>;
  refreshCart: () => Promise<void>;
  dismissNotice: () => void;
  total: number;
};

const StoreContext = createContext<Store | null>(null);
const WISHLIST_KEY = "rk-traders.wishlist";

export function StoreProvider({ children }: { children: ReactNode }) {
  const { isAuthenticated, isRestoring } = useAuth();
  const [cart, setCart] = useState<CartLine[]>([]);
  const [wish, setWish] = useState<number[]>([]);
  const [cartOpen, setCartOpen] = useState(false);
  const [loadingCart, setLoadingCart] = useState(false);
  const [notice, setNotice] = useState<string | null>(null);

  const refreshCart = useCallback(async () => {
    if (!isAuthenticated) { setCart([]); return; }
    setLoadingCart(true);
    try { setCart(await cartApi.lines()); }
    catch (error) {
      const message = errorMessage(error);
      if (!/cart is empty/i.test(message)) setNotice(message);
      setCart([]);
    } finally { setLoadingCart(false); }
  }, [isAuthenticated]);

  useEffect(() => {
    try { setWish(JSON.parse(window.localStorage.getItem(WISHLIST_KEY) ?? "[]") as number[]); } catch { setWish([]); }
    if (!isRestoring) void refreshCart();
  }, [isRestoring, refreshCart]);

  useEffect(() => {
    if (!isAuthenticated) setCart([]);
  }, [isAuthenticated]);

  const add = async (item: Product) => {
    if (!isAuthenticated) { setNotice("Sign in to add this piece to your cart."); return false; }
    try { await cartApi.add(item.id); await refreshCart(); setCartOpen(true); return true; }
    catch (error) { setNotice(errorMessage(error)); return false; }
  };

  const remove = async (id: number) => {
    try { await cartApi.remove(id); await refreshCart(); }
    catch (error) { setNotice(errorMessage(error)); }
  };

  const updateQuantity = async (id: number, quantity: number) => {
    try {
      if (quantity <= 0) await remove(id);
      else { await cartApi.update(id, quantity); await refreshCart(); }
    } catch (error) { setNotice(errorMessage(error)); }
  };

  const toggleWish = (id: number) => setWish((current) => {
    const next = current.includes(id) ? current.filter((item) => item !== id) : [...current, id];
    window.localStorage.setItem(WISHLIST_KEY, JSON.stringify(next));
    return next;
  });

  const total = useMemo(() => cart.reduce((sum, line) => sum + line.product.price * line.quantity, 0), [cart]);
  return <StoreContext.Provider value={{ cart, wish, cartOpen, authenticated: isAuthenticated, loadingCart, notice, add, toggleWish, setCartOpen, remove, updateQuantity, refreshCart, dismissNotice: () => setNotice(null), total }}>{children}</StoreContext.Provider>;
}

export function useStore() { const store = useContext(StoreContext); if (!store) throw new Error("StoreProvider missing"); return store; }
