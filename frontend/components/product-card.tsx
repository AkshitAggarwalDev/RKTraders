"use client";

import Link from "next/link";
import { Heart, LoaderCircle, Plus } from "lucide-react";
import { motion, useMotionValue, useSpring, useTransform } from "framer-motion";
import { useState, type PointerEvent } from "react";
import { useRouter } from "next/navigation";
import type { Product } from "@/lib/data";
import { money } from "@/lib/data";
import { useStore } from "@/context/store";

export function ProductCard({ product, index = 0 }: { product: Product; index?: number }) {
  const { add, wish, toggleWish, authenticated } = useStore();
  const router = useRouter();
  const [adding, setAdding] = useState(false);
  const x = useMotionValue(0); const y = useMotionValue(0);
  const rotateX = useSpring(useTransform(y, [-.5, .5], [8, -8]), { stiffness: 260, damping: 20 });
  const rotateY = useSpring(useTransform(x, [-.5, .5], [-8, 8]), { stiffness: 260, damping: 20 });

  function move(event: PointerEvent<HTMLElement>) {
    const box = event.currentTarget.getBoundingClientRect();
    x.set((event.clientX - box.left) / box.width - .5); y.set((event.clientY - box.top) / box.height - .5);
  }

  async function handleAdd() {
    if (!authenticated) { router.push(`/login?next=/products/${product.id}`); return; }
    setAdding(true); await add(product); setAdding(false);
  }

  return <motion.article initial={{ opacity: 0, y: 22 }} whileInView={{ opacity: 1, y: 0 }} viewport={{ once: true, amount: .3 }} transition={{ delay: index * .08, duration: .55 }} onPointerMove={move} onPointerLeave={() => { x.set(0); y.set(0); }} style={{ rotateX, rotateY, transformPerspective: 1000 }} className="showcase-card group min-w-[164px] rounded-[1.1rem] bg-[#fbfbfb] p-3 shadow-air transition-shadow duration-500 hover:shadow-luxe sm:min-w-[178px]">
    <div className="relative grid h-36 place-items-center overflow-hidden rounded-xl bg-gradient-to-br from-[#f6eeec] via-white to-[#ead5d0] p-3 sm:h-40"><span className="absolute -right-10 -top-10 size-24 rounded-full opacity-30 blur-2xl" style={{ background: product.tone }} />{product.badge && <span className="absolute left-2 top-2 rounded-md bg-wine px-2 py-1 text-[8px] font-bold uppercase tracking-wider text-white">{product.badge}</span>}<button onClick={() => toggleWish(product.id)} aria-label={`Save ${product.name}`} className="absolute right-2 top-2 z-10 rounded-full bg-white/80 p-2 text-wine transition hover:scale-110"><Heart size={14} className={wish.includes(product.id) ? "fill-crimson text-crimson" : ""} /></button><Link href={`/products/${product.id}`} aria-label={`View ${product.name}`} className="absolute inset-0 z-[2]" /><motion.img whileHover={{ scale: 1.08, rotate: -1.5 }} transition={{ type: "spring", stiffness: 280, damping: 19 }} src={product.image} alt={product.name} className="product-image z-[1] h-[90%] w-full object-contain mix-blend-multiply" /></div>
    <div className="pt-3"><p className="truncate text-[11px] font-semibold text-[#39201d]">{product.name}</p><div className="mt-2 flex items-center justify-between"><p className="text-[11px] font-bold text-wine">{money(product.price)}</p><button onClick={handleAdd} disabled={adding} aria-label={`Add ${product.name} to cart`} className="z-[3] grid size-7 place-items-center rounded-full border border-crimson/25 text-crimson transition hover:bg-crimson hover:text-white disabled:opacity-50">{adding ? <LoaderCircle size={14} className="animate-spin" /> : <Plus size={14} />}</button></div></div>
  </motion.article>;
}
