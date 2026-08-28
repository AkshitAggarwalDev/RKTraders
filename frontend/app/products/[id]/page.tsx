"use client";

import Link from "next/link";
import { ArrowLeft, Heart, LoaderCircle, ShoppingBag, ShieldCheck, Truck } from "lucide-react";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { Navigation } from "@/components/navigation";
import { catalogApi } from "@/lib/api";
import { fallbackProducts, money, type Product } from "@/lib/data";
import { useStore } from "@/context/store";

export default function ProductDetailPage() {
  const params = useParams<{ id: string }>();
  const router = useRouter();
  const id = Number(params.id);
  const [product, setProduct] = useState<Product | null>(() => fallbackProducts.find((item) => item.id === id) ?? null);
  const [loading, setLoading] = useState(true);
  const [adding, setAdding] = useState(false);
  const [message, setMessage] = useState("");
  const { add, authenticated, wish, toggleWish } = useStore();

  useEffect(() => {
    let active = true;
    if (!Number.isFinite(id)) { setLoading(false); return; }
    void catalogApi.byId(id).then((item) => { if (active) setProduct(item); }).catch(() => undefined).finally(() => { if (active) setLoading(false); });
    return () => { active = false; };
  }, [id]);

  async function addToCart() {
    if (!product) return;
    if (!authenticated) { router.push(`/login?next=/products/${product.id}`); return; }
    setAdding(true);
    const added = await add(product);
    setAdding(false);
    if (added) setMessage("Added to your cart.");
  }

  if (!product && !loading) return <main className="min-h-screen bg-porcelain"><Navigation /><section className="mx-auto max-w-3xl px-6 pt-40 text-center"><h1 className="editorial text-6xl text-wine">This piece is no longer here.</h1><Link href="/products" className="mt-7 inline-flex items-center gap-2 text-sm font-bold text-crimson"><ArrowLeft size={17} /> Return to catalogue</Link></section></main>;

  return <main className="min-h-screen bg-porcelain pb-16"><Navigation /><section className="mx-auto grid max-w-[1320px] gap-10 px-5 pb-10 pt-32 sm:px-10 lg:grid-cols-[1.1fr_.9fr] lg:gap-16 lg:px-14 lg:pt-40">
    <div className="relative min-h-[410px] overflow-hidden rounded-[2rem] bg-gradient-to-br from-[#f4e8e5] via-[#fffefe] to-[#dfc4be] p-8 sm:min-h-[600px]"><span className="absolute -right-24 -top-20 size-80 rounded-full bg-crimson/15 blur-3xl" />{loading && !product ? <LoaderCircle className="absolute left-1/2 top-1/2 animate-spin text-crimson" /> : <img src={product?.image} alt={product?.name} className="product-image relative z-10 h-full w-full object-contain" />}</div>
    <div className="flex flex-col justify-center"><Link href="/products" className="inline-flex w-fit items-center gap-2 text-xs font-bold uppercase tracking-[.15em] text-crimson"><ArrowLeft size={15} /> All pieces</Link><p className="mt-8 text-[10px] font-bold uppercase tracking-[.2em] text-crimson">{product?.category ?? "RK Collection"}</p><h1 className="editorial mt-3 text-6xl leading-[.88] text-wine sm:text-7xl">{product?.name ?? "Loading piece…"}</h1><p className="mt-5 text-2xl font-semibold text-wine">{product ? money(product.price) : ""}</p><p className="mt-6 max-w-lg text-sm leading-7 text-wine/65">{product?.description || "A thoughtfully selected piece designed to make the everyday feel more considered."}</p>{product?.brand && <p className="mt-4 text-xs font-semibold text-wine/70">From {product.brand}</p>}
      <div className="mt-8 flex flex-wrap gap-3"><button onClick={addToCart} disabled={!product || product.stock === 0 || adding} className="inline-flex min-w-48 items-center justify-center gap-3 rounded-full bg-crimson px-7 py-4 text-sm font-bold text-white shadow-air transition hover:-translate-y-0.5 disabled:cursor-not-allowed disabled:opacity-50">{adding ? <LoaderCircle className="animate-spin" size={18} /> : <ShoppingBag size={18} />}{product?.stock === 0 ? "Currently unavailable" : "Add to cart"}</button><button onClick={() => product && toggleWish(product.id)} aria-label="Save this piece" disabled={!product} className={`grid size-[52px] place-items-center rounded-full border transition ${product && wish.includes(product.id) ? "border-crimson bg-red-50 text-crimson" : "border-wine/15 bg-white text-wine"}`}><Heart size={20} className={product && wish.includes(product.id) ? "fill-crimson" : ""} /></button></div>{message && <p className="mt-3 text-sm font-semibold text-crimson">{message}</p>}
      <div className="mt-10 grid gap-3 border-t border-wine/10 pt-6 sm:grid-cols-2"><div className="flex gap-3 text-sm text-wine/70"><Truck className="shrink-0 text-crimson" size={19} /><span><strong className="block text-wine">Delivery planning</strong>Address is confirmed at checkout.</span></div><div className="flex gap-3 text-sm text-wine/70"><ShieldCheck className="shrink-0 text-crimson" size={19} /><span><strong className="block text-wine">Secure checkout</strong>Your order is created after payment confirmation.</span></div></div>
    </div>
  </section></main>;
}
