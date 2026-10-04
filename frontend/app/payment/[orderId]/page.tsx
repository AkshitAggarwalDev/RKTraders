"use client";

import Link from "next/link";
import { ArrowRight, CreditCard, LoaderCircle, QrCode, ShoppingBag } from "lucide-react";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { CustomerRoute } from "@/components/customer-route";
import { Navigation } from "@/components/navigation";
import { errorMessage } from "@/lib/api/client";
import { orderService } from "@/lib/api/services";
import type { Order } from "@/lib/api/types";
import { money } from "@/lib/data";

function PaymentContent() {
  const params = useParams<{ orderId: string }>();
  const orderId = Number(params.orderId);
  const [order, setOrder] = useState<Order | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!Number.isInteger(orderId) || orderId < 1) { setError("This payment link is invalid."); return; }
    let active = true;
    void orderService.mineById(orderId).then((item) => { if (active) setOrder(item); })
      .catch((reason) => { if (active) setError(errorMessage(reason)); });
    return () => { active = false; };
  }, [orderId]);

  return <main className="min-h-screen bg-porcelain pb-16"><Navigation /><section className="mx-auto max-w-[980px] px-5 pt-36 sm:px-10 lg:px-14"><p className="text-[10px] font-bold uppercase tracking-[.2em] text-crimson">RK Traders · Payment</p><h1 className="editorial mt-3 text-5xl text-wine sm:text-7xl">Your order is reserved.</h1><p className="mt-4 max-w-xl text-sm leading-6 text-wine/65">Payment instructions will appear here when the verified RK Traders QR asset is available. No payment has been taken or recorded.</p>
    {error ? <div className="mt-10 rounded-showroom bg-white p-8 text-center shadow-air"><ShoppingBag className="mx-auto text-crimson" size={28} /><h2 className="editorial mt-5 text-3xl text-wine">We couldn&apos;t open this order.</h2><p className="mt-3 text-sm text-wine/60">{error}</p><Link href="/orders" className="mt-6 inline-flex items-center gap-2 rounded-full bg-crimson px-5 py-3 text-xs font-bold text-white">View orders <ArrowRight size={14} /></Link></div> : !order ? <div className="mt-12 grid place-items-center"><LoaderCircle className="animate-spin text-crimson" size={28} /></div> : <div className="mt-10 grid gap-7 lg:grid-cols-[1fr_360px]"><section className="rounded-showroom bg-white p-6 shadow-air sm:p-8"><p className="text-[10px] font-bold uppercase tracking-[.18em] text-crimson">Order #{order.id}</p><div className="mt-5 divide-y divide-wine/10 border-y border-wine/10">{order.orderItems.map((item) => <div key={item.id} className="flex items-center justify-between gap-4 py-4"><div><p className="font-semibold text-wine">{item.productName}</p><p className="mt-1 text-xs text-wine/55">Quantity {item.quantity} · {money(item.productPrice)} each</p></div><p className="text-sm font-bold text-wine">{money(item.totalPrice)}</p></div>)}</div><div className="mt-6 flex justify-between text-lg font-bold text-wine"><span>Amount payable</span><span>{money(order.totalAmount)}</span></div></section><aside className="rounded-showroom bg-wine p-6 text-white sm:p-7"><div className="flex items-center gap-3"><CreditCard className="text-red-200" /><div><p className="font-bold">Payment pending</p><p className="text-xs text-white/65">Order status: {order.orderStatus}</p></div></div><div className="mt-6 grid min-h-52 place-items-center rounded-2xl border border-dashed border-white/35 bg-white/5 p-6 text-center"><QrCode size={34} className="text-red-200" /><p className="mt-3 text-xs font-bold uppercase tracking-[.14em] text-white">QR code will be provided</p><p className="mt-2 text-xs leading-5 text-white/60">The verified payment QR image will be placed in this panel. This placeholder cannot be scanned and does not represent a completed payment.</p></div><p className="mt-5 text-xs leading-5 text-white/70">Keep your order number handy. Once payment handling is introduced, this page will guide you through the next step.</p><Link href="/orders" className="mt-6 inline-flex w-full items-center justify-center gap-2 rounded-full bg-white px-5 py-3 text-xs font-bold text-crimson">View my orders <ArrowRight size={14} /></Link></aside></div>}
  </section></main>;
}

export default function PaymentPage() { return <CustomerRoute><PaymentContent /></CustomerRoute>; }
