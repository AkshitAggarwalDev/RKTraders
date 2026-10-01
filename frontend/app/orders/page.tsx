"use client";

import Link from "next/link";
import { LoaderCircle, PackageOpen } from "lucide-react";
import { useEffect, useState } from "react";
import { CustomerRoute } from "@/components/customer-route";
import { Navigation } from "@/components/navigation";
import { errorMessage } from "@/lib/api";
import { orderService } from "@/lib/api/services";
import { money } from "@/lib/data";
import type { Order } from "@/lib/api/types";

const statusTone: Record<string, string> = {
  PLACED: "bg-amber-50 text-amber-700",
  SHIPPED: "bg-blue-50 text-blue-700",
  DELIVERED: "bg-green-50 text-green-700",
  CANCELLED: "bg-red-50 text-crimson",
};

function formatDate(value: string) {
  const parsed = new Date(value);
  return Number.isNaN(parsed.getTime()) ? value : parsed.toLocaleDateString("en-IN", { day: "numeric", month: "short", year: "numeric" });
}

function OrdersContent() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let active = true;
    void orderService.mine().then((items) => { if (active) setOrders(items); })
      .catch((err) => { if (active) setError(errorMessage(err)); })
      .finally(() => { if (active) setLoading(false); });
    return () => { active = false; };
  }, []);

  return <main className="min-h-screen bg-porcelain pb-16"><Navigation /><section className="mx-auto max-w-[1100px] px-5 pt-36 sm:px-10 lg:px-14"><p className="text-[10px] font-bold uppercase tracking-[.2em] text-crimson">Your RK orders</p><h1 className="editorial mt-3 text-6xl text-wine sm:text-8xl">Every order, kept.</h1><p className="mt-4 max-w-md text-sm leading-6 text-wine/65">A quiet record of everything you've chosen, and where each piece is headed.</p>{loading ? <LoaderCircle className="mt-12 animate-spin text-crimson" /> : error ? <div className="mt-10 rounded-showroom border border-dashed border-wine/20 bg-white p-12 text-center"><PackageOpen className="mx-auto text-crimson" size={28} /><h2 className="editorial mt-5 text-4xl text-wine">We couldn't reach your orders.</h2><p className="mt-3 text-sm text-wine/60">{error}</p><Link href="/products" className="mt-7 inline-block rounded-full bg-crimson px-5 py-3 text-xs font-bold text-white">Explore collection</Link></div> : orders.length === 0 ? <div className="mt-10 rounded-showroom border border-dashed border-wine/20 bg-white p-12 text-center"><PackageOpen className="mx-auto text-crimson" size={28} /><h2 className="editorial mt-5 text-4xl text-wine">No orders yet.</h2><p className="mt-3 text-sm text-wine/60">Your first order will appear here the moment you place it.</p><Link href="/products" className="mt-7 inline-block rounded-full bg-crimson px-5 py-3 text-xs font-bold text-white">Explore collection</Link></div> : <div className="mt-10 space-y-4">{orders.map((order) => <article key={order.id} className="rounded-showroom bg-white p-6 shadow-air sm:p-8"><div className="flex flex-wrap items-start justify-between gap-4"><div><p className="text-[9px] font-bold uppercase tracking-[.18em] text-crimson">Order #{order.id}</p><p className="mt-1 text-sm font-semibold text-wine">{formatDate(order.orderDate)}</p></div><span className={`rounded-full px-3 py-1.5 text-[9px] font-bold uppercase tracking-[.12em] ${statusTone[order.orderStatus] ?? "bg-wine/10 text-wine"}`}>{order.orderStatus}</span></div><div className="mt-6 divide-y divide-wine/10 border-t border-wine/10">{order.orderItems?.map((item) => <div key={item.id} className="flex items-center justify-between gap-4 py-3"><div><p className="text-sm font-semibold text-wine">{item.productName}</p><p className="mt-1 text-xs text-wine/55">Qty {item.quantity} × {money(item.productPrice)}</p></div><p className="text-sm font-bold text-wine">{money(item.totalPrice)}</p></div>)}</div><div className="mt-5 flex flex-wrap items-center justify-between gap-3 border-t border-wine/10 pt-5"><p className="text-sm text-wine/60">Total</p><p className="font-bold text-wine">{money(order.totalAmount)}</p></div></article>)}</div>}</section></main>;
}

export default function OrdersPage() { return <CustomerRoute><OrdersContent /></CustomerRoute>; }
