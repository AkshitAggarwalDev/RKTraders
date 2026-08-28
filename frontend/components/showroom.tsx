"use client";

import Link from "next/link";
import { ArrowRight, ChevronLeft, ChevronRight, Instagram, Send, Youtube } from "lucide-react";
import { motion } from "framer-motion";
import { useEffect, useRef, useState } from "react";
import { collections, fallbackProducts } from "@/lib/data";
import { catalogApi } from "@/lib/api";
import { Navigation } from "@/components/navigation";
import { ProductCard } from "@/components/product-card";
import { HeroCarousel } from "@/components/hero-carousel";

const entrance = { hidden: { opacity: 0, y: 24 }, visible: { opacity: 1, y: 0 } };

export function Showroom() {
  const picks = useRef<HTMLDivElement>(null);
  const [products, setProducts] = useState(fallbackProducts);
  const [email, setEmail] = useState("");
  const [subscribed, setSubscribed] = useState(false);
  useEffect(() => {
    let active = true;
    void catalogApi.all().then((catalogue) => {
      const visible = catalogue.filter((product) => product.status !== "INACTIVE");
      if (active && visible.length) setProducts(visible);
    }).catch(() => undefined);
    return () => { active = false; };
  }, []);

  return <main id="top" className="bg-porcelain">
    <Navigation />
    <HeroCarousel />

    <section id="collections" className="bg-[#fcfbfa] px-5 py-4 sm:px-10 sm:py-5">
      <div className="mx-auto grid max-w-[1520px] gap-3 lg:grid-cols-[1fr_1fr_1fr_2.85fr]">
        <div className="contents lg:col-span-3">{collections.map((collection, index) => <motion.div initial="hidden" whileInView="visible" viewport={{ once: true }} variants={entrance} transition={{ duration: .5, delay: index * .07 }} key={collection.title}><Link href={`/products?category=${encodeURIComponent(collection.title.replace(/^(Modern |Timeless |Refined )/, ""))}`} className="group relative block h-[176px] overflow-hidden rounded-[.6rem] bg-wine shadow-air sm:h-[210px] lg:h-[190px]">
          <div className="absolute inset-0 bg-cover transition duration-700 group-hover:scale-110" style={{ backgroundImage: `url(${collection.image})`, backgroundPosition: collection.position }} />
          <div className="absolute inset-0 bg-gradient-to-t from-black/70 via-wine/10 to-transparent" />
          <div className="absolute inset-x-4 bottom-3 flex items-center justify-between text-white"><p className="editorial text-xl">{collection.title}</p><span className="grid size-7 place-items-center rounded-full bg-white text-crimson transition group-hover:translate-x-1"><ArrowRight size={14} /></span></div>
        </Link></motion.div>)}</div>

        <section id="picks" className="min-w-0 pt-3 lg:pl-4 lg:pt-0">
          <div className="flex items-center gap-3"><p className="editorial shrink-0 text-xl text-crimson">Editor&apos;s Picks</p><span className="h-px flex-1 bg-crimson/20" /><button onClick={() => picks.current?.scrollBy({ left: -260, behavior: "smooth" })} aria-label="Previous picks" className="grid size-6 place-items-center rounded-full border border-crimson/15 text-crimson"><ChevronLeft size={13} /></button><button onClick={() => picks.current?.scrollBy({ left: 260, behavior: "smooth" })} aria-label="Next picks" className="grid size-6 place-items-center rounded-full border border-crimson/15 text-crimson"><ChevronRight size={13} /></button></div>
          <div ref={picks} className="hide-scrollbar mt-3 flex gap-2.5 overflow-x-auto pb-2">{products.slice(0, 4).map((product, index) => <ProductCard key={product.id} product={product} index={index} />)}</div>
        </section>
      </div>
    </section>

    <section className="relative overflow-hidden bg-wine px-5 py-16 text-white sm:px-10 sm:py-24 lg:px-14">
      <div className="absolute inset-y-0 right-0 hidden w-[48%] bg-[url('/images/hero-bedroom.png')] bg-cover bg-center opacity-45 lg:block" />
      <div className="absolute inset-0 bg-[linear-gradient(90deg,#580003_0%,rgba(88,0,3,.94)_45%,rgba(88,0,3,.22)_100%)]" />
      <motion.div initial={{ opacity: 0, y: 25 }} whileInView={{ opacity: 1, y: 0 }} viewport={{ once: true, amount: .35 }} transition={{ duration: .7 }} className="relative mx-auto max-w-[1520px]"><p className="text-[10px] font-bold uppercase tracking-[.23em] text-red-200">About RK Traders</p><h2 className="editorial mt-4 max-w-2xl text-6xl leading-[.8] sm:text-8xl">Furniture that belongs in your space.</h2><p className="mt-7 max-w-md text-sm leading-7 text-white/78">RK Traders brings together considered furniture and home pieces chosen for comfort, character and the rituals of everyday life. The aim is simple: make it easier to find things that feel right at home.</p><Link href="/lookbook" className="mt-8 inline-flex items-center gap-4 rounded-full border border-white/55 bg-white/10 px-5 py-3 text-xs font-bold text-white backdrop-blur transition hover:bg-white hover:text-crimson">Enter the lookbook <ArrowRight size={15} /></Link></motion.div>
    </section>

    <section className="overflow-hidden bg-[linear-gradient(100deg,#870004,#D61F26,#a90006)] px-5 py-4 text-white sm:px-10 sm:py-5">
      <div className="mx-auto flex max-w-[1440px] flex-col items-center gap-4 md:flex-row md:justify-between">
        <div className="flex items-center gap-7"><p className="editorial text-2xl">Stay Inspired.</p><p className="hidden max-w-[230px] text-[10px] leading-4 text-white/85 sm:block">New collections, timeless design and stories—straight to your inbox.</p></div>
        <form onSubmit={(event) => { event.preventDefault(); if (email.trim()) setSubscribed(true); }} className="flex w-full max-w-sm rounded-full border border-white/55 bg-white/10 p-1 backdrop-blur"><input value={email} onChange={(event) => setEmail(event.target.value)} type="email" aria-label="Email address" placeholder="Enter your email" className="min-w-0 flex-1 bg-transparent px-4 text-[10px] text-white placeholder:text-white/75 outline-none" /><button className="inline-flex items-center gap-3 rounded-full bg-white px-5 py-2 text-[10px] font-bold text-crimson">{subscribed ? "Thank you" : "Subscribe"} <ArrowRight size={13} /></button></form>
        <div className="flex gap-2.5">{[Instagram, Send, Youtube].map((Icon, index) => <a href="#top" onClick={(event) => { event.preventDefault(); document.getElementById("top")?.scrollIntoView({ behavior: "smooth" }); }} aria-label="Back to top" key={index} className="grid size-7 place-items-center rounded-full border border-white/55"><Icon size={14} /></a>)}</div>
      </div>
    </section>
  </main>;
}
