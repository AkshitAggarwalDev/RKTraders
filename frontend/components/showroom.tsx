"use client";

import Link from "next/link";
import { ArrowRight, ChevronLeft, ChevronRight, Github, Instagram, Linkedin, LoaderCircle } from "lucide-react";
import { motion, useInView } from "framer-motion";
import { useEffect, useRef, useState } from "react";
import { useRouter } from "next/navigation";
import { collections, type Product } from "@/lib/data";
import { catalogApi } from "@/lib/api";
import { Navigation } from "@/components/navigation";
import { ProductCard } from "@/components/product-card";
import { HeroCarousel } from "@/components/hero-carousel";

const entrance = { hidden: { opacity: 0, y: 24 }, visible: { opacity: 1, y: 0 } };

// Configure these when customer and delivered-order totals become available to this public frontend.
const CUSTOMER_TOTAL = 0;
const ORDERS_DELIVERED_TOTAL = 0;

function CountUp({ target, suffix = "" }: { target: number | null; suffix?: string }) {
  const numberRef = useRef<HTMLSpanElement>(null);
  const isInView = useInView(numberRef, { once: true, amount: .65 });
  const hasCounted = useRef(false);
  const [value, setValue] = useState(0);

  useEffect(() => {
    if (!isInView || target === null || hasCounted.current) return;
    hasCounted.current = true;

    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    if (reduceMotion || target === 0) { setValue(target); return; }

    const duration = 1900;
    const startedAt = performance.now();
    let frame = 0;
    const animate = (now: number) => {
      const progress = Math.min((now - startedAt) / duration, 1);
      const eased = 1 - Math.pow(1 - progress, 3);
      setValue(Math.round(target * eased));
      if (progress < 1) frame = requestAnimationFrame(animate);
    };
    frame = requestAnimationFrame(animate);
    return () => cancelAnimationFrame(frame);
  }, [isInView, target]);

  return <span ref={numberRef}>{value.toLocaleString("en-IN")}{suffix}</span>;
}

export function Showroom() {
  const picks = useRef<HTMLDivElement>(null);
  const router = useRouter();
  const [products, setProducts] = useState<Product[]>([]);
  const [email, setEmail] = useState("");
  const [emailError, setEmailError] = useState("");
  const [isNavigating, setIsNavigating] = useState(false);

  const handleSubscribe = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const trimmed = email.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!trimmed) {
      setEmailError("Please enter your email address.");
      return;
    }
    if (!emailRegex.test(trimmed)) {
      setEmailError("Please enter a valid email address.");
      return;
    }

    setEmailError("");
    setIsNavigating(true);
    try {
      sessionStorage.setItem("developer_support_email", trimmed);
    } catch {
      // Gracefully handle disabled sessionStorage in strict browser environments
    }
    router.push("/support-developer");
  };

  useEffect(() => {
    let active = true;
    void catalogApi.all().then((catalogue) => {
      const visible = catalogue.filter((product) => product.status !== "INACTIVE");
      if (active) setProducts(visible);
    }).catch(() => undefined);
    return () => { active = false; };
  }, []);
  const closingStats = [
    { target: CUSTOMER_TOTAL, label: "Customers", suffix: "" },
    { target: ORDERS_DELIVERED_TOTAL, label: "Orders Delivered", suffix: "" },
    { target: products.length || null, label: "Products", suffix: "" },
  ];

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

    <section className="grain relative isolate overflow-hidden bg-[#320002] px-5 py-24 text-white sm:px-10 sm:py-32 lg:px-14 lg:py-40">
      <div aria-hidden="true" className="absolute inset-0 bg-[radial-gradient(circle_at_50%_48%,rgba(218,56,53,.35),transparent_31%),linear-gradient(135deg,#270001_0%,#590003_49%,#240001_100%)]" />
      <motion.div aria-hidden="true" animate={{ y: [0, -16, 0], rotate: [10, 13, 10] }} transition={{ duration: 8, repeat: Infinity, ease: "easeInOut" }} className="absolute -left-28 top-16 size-72 rounded-full border border-white/10 bg-gradient-to-br from-white/10 to-transparent blur-[1px] sm:size-[28rem]" />
      <motion.div aria-hidden="true" animate={{ y: [0, 18, 0], rotate: [-14, -10, -14] }} transition={{ duration: 10, repeat: Infinity, ease: "easeInOut" }} className="absolute -right-24 bottom-[-9rem] size-80 rounded-[4rem] border border-red-200/10 bg-[linear-gradient(145deg,rgba(255,255,255,.12),transparent_58%)] shadow-[-30px_-30px_80px_rgba(255,77,70,.1)] sm:size-[31rem]" />
      <div aria-hidden="true" className="absolute inset-y-0 left-1/2 hidden w-px bg-white/10 lg:block" />
      <div aria-hidden="true" className="absolute inset-x-[8%] top-1/2 h-px bg-white/10" />

      <div className="relative mx-auto max-w-[1320px] text-center">
        <motion.p initial={{ opacity: 0, y: 16 }} whileInView={{ opacity: 1, y: 0 }} viewport={{ once: true, amount: .4 }} transition={{ duration: .65 }} className="text-[10px] font-bold uppercase tracking-[.3em] text-red-200">RK Traders · considered living</motion.p>
        <motion.h2 initial={{ opacity: 0, y: 28 }} whileInView={{ opacity: 1, y: 0 }} viewport={{ once: true, amount: .4 }} transition={{ duration: .8, delay: .08, ease: [0.22, 1, 0.36, 1] }} className="editorial mx-auto mt-5 max-w-4xl text-[clamp(3rem,5.4vw,5.75rem)] leading-[.88] tracking-[-.045em] text-white">Made for the way you live.</motion.h2>
        <motion.p initial={{ opacity: 0, y: 18 }} whileInView={{ opacity: 1, y: 0 }} viewport={{ once: true, amount: .4 }} transition={{ duration: .7, delay: .18 }} className="mx-auto mt-8 max-w-md text-sm leading-7 text-white/75 sm:text-base">Thoughtfully chosen furniture and home pieces for spaces that feel unmistakably like yours.</motion.p>

        <div className="mx-auto mt-16 grid max-w-5xl gap-3 sm:grid-cols-3 sm:gap-4 lg:mt-20">
          {closingStats.map((stat, index) => <motion.div key={stat.label} initial={{ opacity: 0, y: 30, scale: .96 }} whileInView={{ opacity: 1, y: 0, scale: 1 }} viewport={{ once: true, amount: .35 }} transition={{ duration: .65, delay: .28 + index * .12, ease: [0.22, 1, 0.36, 1] }} whileHover={{ y: -6, scale: 1.015 }} className="group rounded-[1.7rem] border border-white/15 bg-white/[.07] px-7 py-9 shadow-[inset_0_1px_0_rgba(255,255,255,.14),0_24px_50px_rgba(20,0,1,.2)] backdrop-blur-md transition-colors hover:border-red-100/45 hover:bg-white/[.11] sm:px-5">
            <p className="editorial text-6xl leading-none text-white sm:text-7xl"><CountUp target={stat.target} suffix={stat.suffix} /></p>
            <div className="mx-auto mt-5 h-px w-8 bg-red-200/65 transition-all duration-500 group-hover:w-14" />
            <p className="mt-4 text-[10px] font-bold uppercase tracking-[.2em] text-white/70">{stat.label}</p>
          </motion.div>)}
        </div>
      </div>
    </section>

    <section className="overflow-hidden bg-[linear-gradient(100deg,#870004,#D61F26,#a90006)] px-5 py-5 text-white sm:px-10 sm:py-6">
      <div className="mx-auto max-w-[1440px]">
        <div className="flex flex-col items-center gap-4 xl:flex-row xl:justify-between">
          <div className="flex items-center gap-7"><p className="editorial text-2xl">Stay Inspired.</p><p className="hidden max-w-[270px] text-[10px] leading-4 text-white/85 sm:block">New collections, timeless design and stories—straight to your inbox.</p></div>
          <div className="flex w-full max-w-sm flex-col items-stretch sm:items-end">
            <form noValidate onSubmit={handleSubscribe} className="flex w-full max-w-sm rounded-full border border-white/55 bg-white/10 p-1 backdrop-blur"><input value={email} onChange={(event) => { setEmail(event.target.value); if (emailError) setEmailError(""); }} type="email" aria-label="Email address" placeholder="Enter your email" className="subscription-email-input min-w-0 flex-1 bg-transparent px-4 text-[10px] text-white placeholder:text-white/75 outline-none" /><button disabled={isNavigating} className="inline-flex items-center gap-3 rounded-full bg-white px-5 py-2 text-[10px] font-bold text-crimson transition hover:bg-red-50 disabled:opacity-75">{isNavigating ? <><LoaderCircle className="animate-spin" size={13} /> Continuing...</> : <>Subscribe <ArrowRight size={13} /></>}</button></form>
            {emailError && <p role="alert" className="mt-1 px-3 text-[11px] font-medium text-red-200">{emailError}</p>}
          </div>
        </div>
        <div className="mt-5 flex flex-col items-center gap-4 border-t border-white/20 pt-4 sm:flex-row sm:justify-between">
          <span aria-hidden="true" />
          <div className="flex gap-2.5"><a href="https://www.instagram.com/only_akshit007/" target="_blank" rel="noopener noreferrer" aria-label="Instagram profile" className="grid size-7 place-items-center rounded-full border border-white/55 transition hover:-translate-y-0.5 hover:bg-white hover:text-crimson"><Instagram size={14} /></a><a href="https://www.linkedin.com/in/akshit-aggarwal-69093a364/" target="_blank" rel="noopener noreferrer" aria-label="LinkedIn profile" className="grid size-7 place-items-center rounded-full border border-white/55 transition hover:-translate-y-0.5 hover:bg-white hover:text-crimson"><Linkedin size={14} /></a><a href="https://github.com/AkshitAggarwalDev" target="_blank" rel="noopener noreferrer" aria-label="GitHub profile" className="grid size-7 place-items-center rounded-full border border-white/55 transition hover:-translate-y-0.5 hover:bg-white hover:text-crimson"><Github size={14} /></a></div>
        </div>
      </div>
    </section>
  </main>;
}
