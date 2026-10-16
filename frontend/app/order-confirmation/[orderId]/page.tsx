"use client";

import Link from "next/link";
import { ArrowRight, Banknote, CheckCircle2, LoaderCircle, MapPin, Phone, ShoppingBag, Truck } from "lucide-react";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { CustomerRoute } from "@/components/customer-route";
import { Navigation } from "@/components/navigation";
import { errorMessage } from "@/lib/api/client";
import { orderService } from "@/lib/api/services";
import type { Address, Order } from "@/lib/api/types";
import { money } from "@/lib/data";

const phonePattern = /^[1-9]\d*$/;

function addressText(address?: Address | null) {
  if (!address) return "Delivery address details will appear here once they are attached to the order.";
  return [address.fullName, address.phoneNumber, address.houseNumber, address.street, address.NearByLoc, `${address.city}, ${address.state} · ${address.pincode}`].filter(Boolean).join(", ");
}

function OrderConfirmationContent() {
  const params = useParams<{ orderId: string }>();
  const rawOrderId = Array.isArray(params.orderId) ? params.orderId[0] : params.orderId;
  const validId = typeof rawOrderId === "string" && phonePattern.test(rawOrderId);
  const orderId = validId ? Number(rawOrderId) : 0;
  const [order, setOrder] = useState<Order | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [detailsError, setDetailsError] = useState<string | null>(null);
  const [creationConfirmed, setCreationConfirmed] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!validId) { setError("This confirmation link is invalid."); setLoading(false); return; }
    let active = true;
    const createdInCheckout = sessionStorage.getItem(`rk-order-created:${orderId}`) === "true";
    setCreationConfirmed(createdInCheckout); setLoading(true); setError(null); setDetailsError(null);
    void orderService.mineById(orderId)
      .then((loaded) => { if (active) { setOrder(loaded); setCreationConfirmed(true); setError(null); } })
      .catch((reason) => { if (active) { if (createdInCheckout) setDetailsError(errorMessage(reason)); else setError(errorMessage(reason)); } })
      .finally(() => { if (active) setLoading(false); });
    return () => { active = false; };
  }, [orderId, validId]);

  const confirmed = !loading && !error ? order : null;
  const cod = confirmed?.paymentMethod === "CASH_ON_DELIVERY" || !confirmed?.paymentMethod;
  const paymentPending = confirmed?.paymentStatus === "PENDING" || !confirmed?.paymentStatus;
  return <main className="min-h-screen bg-porcelain pb-16"><Navigation /><section className="mx-auto flex min-h-[calc(100vh-7rem)] max-w-[1080px] items-center px-5 pt-32 sm:px-10 lg:px-14">
    {loading ? <div className="grid place-items-center py-24"><LoaderCircle className="animate-spin text-crimson" size={30} /></div>
      : error ? <div className="mt-10 rounded-showroom bg-white p-10 text-center shadow-air"><ShoppingBag className="mx-auto text-crimson" size={28} /><h1 className="editorial mt-5 text-4xl text-wine">We couldn&apos;t open this order.</h1><p className="mt-3 text-sm text-wine/60">{error}</p><div className="mt-7 flex flex-wrap justify-center gap-3"><Link href="/orders" className="rounded-full bg-crimson px-5 py-3 text-xs font-bold text-white">View order history</Link><Link href="/" className="rounded-full border border-wine/15 px-5 py-3 text-xs font-bold text-wine">Continue shopping</Link></div></div>
      : !confirmed && creationConfirmed ? <div className="w-full py-8"><div className="rounded-showroom bg-white p-8 text-center shadow-air sm:p-12"><span className="mx-auto grid size-20 place-items-center rounded-full bg-emerald-600 text-white shadow-lg shadow-emerald-700/20"><CheckCircle2 size={44} strokeWidth={2.5} /></span><h1 className="mt-5 text-[22px] font-bold uppercase tracking-[.12em] text-emerald-700 sm:text-2xl">Order Placed Successfully!</h1><p className="mt-2 text-sm text-wine/65">Thank you for shopping with RK Traders. Your order has been confirmed.</p><p className="mt-6 text-xs font-bold uppercase tracking-[.16em] text-crimson">Order number</p><p className="editorial mt-1 text-5xl text-wine">#{orderId}</p><p className="mt-6 text-sm font-bold text-wine">Expected Delivery: 3–5 business days</p><p className="mt-3 text-sm leading-6 text-wine/70">Payment Method: Cash on Delivery (COD)<br />Payment will be collected in cash when your order is delivered to your delivery address.</p><p className="mt-5 text-sm text-wine"><span className="font-bold">For more details about delivery, contact:</span> <a href="tel:+918295451720" className="font-bold text-crimson">+91 8295451720</a></p>{detailsError && <p className="mt-6 rounded-2xl bg-red-50 p-4 text-sm text-wine/70">Order details are temporarily unavailable. You can view them in Order History shortly.</p>}<div className="mt-8 flex flex-wrap justify-center gap-3"><Link href="/orders" className="rounded-full bg-crimson px-6 py-3.5 text-xs font-bold text-white">View My Orders</Link><Link href="/products" className="rounded-full border border-wine/15 px-6 py-3.5 text-xs font-bold text-wine">Continue Shopping</Link></div></div></div>
      : <div className="w-full py-8">
        <div className="rounded-showroom bg-white p-7 text-center shadow-air sm:p-10">
          <div className="flex flex-col items-center">
            <span className="grid size-20 place-items-center rounded-full bg-emerald-600 text-white shadow-lg shadow-emerald-700/20"><CheckCircle2 size={44} strokeWidth={2.5} /></span>
            <div className="mt-5">
              <h1 className="text-[22px] font-bold uppercase tracking-[.12em] text-emerald-700 sm:text-2xl">Order Placed Successfully!</h1>
              <p className="mt-2 text-sm text-wine/65">Thank you for shopping with RK Traders. Your order has been confirmed.</p>
            </div>
          </div>
          <p className="mt-6 text-xs font-bold uppercase tracking-[.16em] text-crimson">Order number</p>
          <p className="editorial mt-1 text-5xl text-wine">#{confirmed?.id}</p>
          <div className="mt-8 grid gap-5 text-left sm:grid-cols-3">
            <div className="rounded-2xl border border-wine/10 p-5">
              <div className="flex items-center gap-2 text-crimson"><Banknote size={16} /><p className="text-[10px] font-bold uppercase tracking-[.15em] text-crimson">Payment method</p></div>
              <p className="mt-2 text-sm font-bold text-wine">{cod ? "Cash on Delivery (COD)" : confirmed?.paymentMethod}</p>
            </div>
            <div className="rounded-2xl border border-wine/10 p-5">
              <div className="flex items-center gap-2 text-crimson"><Banknote size={16} /><p className="text-[10px] font-bold uppercase tracking-[.15em] text-crimson">Payment status</p></div>
              <p className="mt-2 text-sm font-bold text-wine">Pay when your order is delivered</p>
              {!paymentPending && <p className="mt-1 text-xs text-wine/55">Recorded as: {confirmed?.paymentStatus}</p>}
            </div>
            <div className="rounded-2xl border border-wine/10 p-5">
              <div className="flex items-center gap-2 text-crimson"><Truck size={16} /><p className="text-[10px] font-bold uppercase tracking-[.15em] text-crimson">Expected delivery</p></div>
              <p className="mt-2 text-sm font-bold text-wine">3–5 business days</p>
            </div>
          </div>
          <div className="mt-8 rounded-2xl bg-red-50/60 p-5 text-left">
            <div className="flex items-start gap-3">
              <Phone size={18} className="mt-0.5 shrink-0 text-crimson" />
              <p className="text-sm leading-6 text-wine"><span className="font-bold">For more details about delivery, contact:</span> <a href="tel:+918295451720" className="whitespace-nowrap font-bold text-crimson">+91 8295451720</a></p>
            </div>
          </div>
          <div className="mt-8 divide-y divide-wine/10 border-y border-wine/10 text-left">
            {confirmed?.orderItems.map((item) => <div key={item.id} className="flex items-center justify-between gap-4 py-4">
              <div><p className="text-sm font-semibold text-wine">{item.productName}</p><p className="mt-1 text-xs text-wine/55">Quantity {item.quantity} · {money(item.productPrice)} each</p></div>
              <p className="text-sm font-bold text-wine">{money(item.totalPrice)}</p>
            </div>)}
          </div>
          <div className="mt-6 flex justify-between text-lg font-bold text-wine"><span>Total</span><span>{money(confirmed?.totalAmount ?? 0)}</span></div>
          <div className="mt-8 rounded-2xl border border-wine/10 p-5 text-left">
            <div className="flex items-center gap-2 text-crimson"><MapPin size={16} /><p className="text-[10px] font-bold uppercase tracking-[.15em] text-crimson">Delivery address</p></div>
            <p className="mt-3 text-sm leading-6 text-wine/80">{addressText(confirmed?.address)}</p>
            <p className="mt-3 text-[11px] text-wine/50">Delivery estimate is an approximation, not a guaranteed date.</p>
          </div>
        </div>
        <div className="mt-7 flex flex-wrap justify-center gap-3 pb-4">
          <Link href="/orders" className="inline-flex items-center gap-2 rounded-full bg-crimson px-6 py-3.5 text-xs font-bold text-white">View My Orders <ArrowRight size={15} /></Link>
          <Link href="/" className="inline-flex items-center gap-2 rounded-full border border-wine/15 px-6 py-3.5 text-xs font-bold text-wine">Continue shopping</Link>
        </div>
      </div>}
  </section></main>;
}

export default function OrderConfirmationPage() { return <CustomerRoute><OrderConfirmationContent /></CustomerRoute>; }
