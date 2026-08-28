"use client";

import Link from "next/link";
import { Heart } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { Navigation } from "@/components/navigation";
import { ProductCard } from "@/components/product-card";
import { catalogApi } from "@/lib/api";
import { fallbackProducts } from "@/lib/data";
import { useStore } from "@/context/store";

export default function WishlistPage() {
  const { wish } = useStore(); const [products, setProducts] = useState(fallbackProducts);
  useEffect(() => { let active = true; void catalogApi.all().then((items) => { if (active && items.length) setProducts(items); }).catch(() => undefined); return () => { active = false; }; }, []);
  const saved = useMemo(() => products.filter((product) => wish.includes(product.id)), [products, wish]);
  return <main className="min-h-screen bg-porcelain pb-16"><Navigation /><section className="mx-auto max-w-[1500px] px-5 pt-36 sm:px-10 lg:px-14"><p className="text-[10px] font-bold uppercase tracking-[.2em] text-crimson">Saved pieces</p><h1 className="editorial mt-3 text-6xl text-wine sm:text-8xl">Your enduring edit.</h1><p className="mt-4 max-w-md text-sm leading-6 text-wine/65">Saved on this device, ready for whenever the timing feels right.</p>{saved.length ? <div className="mt-10 grid gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">{saved.map((product, index) => <ProductCard key={product.id} product={product} index={index} />)}</div> : <div className="mt-10 rounded-showroom border border-dashed border-wine/20 bg-white p-12 text-center"><Heart className="mx-auto text-crimson" size={28} /><h2 className="editorial mt-5 text-4xl text-wine">Save the pieces that stay with you.</h2><Link href="/products" className="mt-7 inline-block rounded-full bg-crimson px-5 py-3 text-xs font-bold text-white">Explore collection</Link></div>}</section></main>;
}
