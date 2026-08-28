"use client";

import Link from "next/link";
import { ArrowRight, Play } from "lucide-react";
import { AnimatePresence, motion, useMotionValue, useSpring, useTransform } from "framer-motion";
import { useCallback, useEffect, useState, type PointerEvent } from "react";
import { heroSlides } from "@/lib/editorial";

const AUTOPLAY_MS = 6500;

export function HeroCarousel() {
  const [active, setActive] = useState(0);
  const [paused, setPaused] = useState(false);
  const pointerX = useMotionValue(0); const pointerY = useMotionValue(0);
  const imageX = useSpring(useTransform(pointerX, [-.5, .5], [-8, 8]), { stiffness: 65, damping: 23 });
  const imageY = useSpring(useTransform(pointerY, [-.5, .5], [-5, 5]), { stiffness: 65, damping: 23 });
  const slide = heroSlides[active];
  const select = useCallback((index: number) => setActive(index), []);

  useEffect(() => {
    if (paused) return;
    const timer = window.setInterval(() => setActive((current) => (current + 1) % heroSlides.length), AUTOPLAY_MS);
    return () => window.clearInterval(timer);
  }, [paused]);

  function parallax(event: PointerEvent<HTMLElement>) { const box = event.currentTarget.getBoundingClientRect(); pointerX.set((event.clientX - box.left) / box.width - .5); pointerY.set((event.clientY - box.top) / box.height - .5); }

  return <section aria-roledescription="carousel" aria-label="RK Traders editorial highlights" onPointerMove={parallax} onPointerLeave={() => { pointerX.set(0); pointerY.set(0); setPaused(false); }} onPointerDown={() => setPaused(true)} onPointerUp={() => setPaused(false)} className="grain relative h-[clamp(460px,48vw,720px)] min-h-[460px] overflow-hidden bg-[#220001] pt-24 sm:pt-28">
    <AnimatePresence initial={false} mode="sync"><motion.div key={slide.image} initial={{ opacity: 0, scale: 1.07 }} animate={{ opacity: 1, scale: 1.025 }} exit={{ opacity: 0, scale: 1.035 }} transition={{ duration: 1.15, ease: [0.22, 1, 0.36, 1] }} style={{ x: imageX, y: imageY, backgroundImage: `url(${slide.image})`, backgroundPosition: slide.position }} className="absolute inset-0 bg-cover" /></AnimatePresence>
    <div className="hero-shade absolute inset-0" /><div className="hero-grid absolute inset-0 opacity-45" /><span className="ember absolute left-[44%] top-[40%] size-1.5 rounded-full bg-red-200" /><span className="ember absolute left-[53%] top-[63%] size-1 rounded-full bg-red-100" style={{ animationDelay: "-2.3s" }} />
    <div className="relative z-10 mx-auto flex h-full max-w-[1520px] items-center px-6 sm:px-12 lg:px-16"><AnimatePresence mode="wait"><motion.div key={active} initial={{ opacity: 0, y: 24 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -16 }} transition={{ duration: .58, ease: [0.22, 1, 0.36, 1] }} className="max-w-[585px] pt-3"><p className="text-[9px] font-bold uppercase tracking-[.23em] text-red-100">{slide.eyebrow}</p><h1 className="editorial mt-3 whitespace-pre-line text-[clamp(4.4rem,7.5vw,8.7rem)] leading-[.72] text-white">{slide.title}</h1><p className="mt-6 max-w-sm text-xs leading-5 text-white/85 sm:text-sm">{slide.description}</p><div className="mt-5 flex flex-wrap gap-2.5"><Link href={slide.primaryHref} className="group inline-flex items-center gap-5 rounded-full bg-crimson px-5 py-3 text-xs font-semibold text-white shadow-air transition hover:-translate-y-0.5 hover:bg-[#b50e15]">{slide.primaryLabel} <ArrowRight size={15} className="transition group-hover:translate-x-1" /></Link><Link href={slide.secondaryHref} className="inline-flex items-center gap-4 rounded-full border border-white/60 bg-white/10 px-5 py-3 text-xs font-semibold text-white backdrop-blur-sm transition hover:bg-white/20"><Play size={12} fill="currentColor" /> {slide.secondaryLabel} <ArrowRight size={15} /></Link></div></motion.div></AnimatePresence><AnimatePresence mode="wait"><motion.aside key={`note-${active}`} initial={{ opacity: 0, x: 20 }} animate={{ opacity: 1, x: 0 }} exit={{ opacity: 0, x: 12 }} transition={{ duration: .6, delay: .18 }} className="glass float absolute right-[10%] top-[30%] hidden w-[160px] rounded-[1.2rem] p-4 text-[#361514] lg:block"><span className="grid size-7 place-items-center rounded-full bg-crimson text-xs text-white">R</span><p className="editorial mt-4 whitespace-pre-line text-xl leading-[.95]">{slide.note}</p><Link href="/lookbook" className="mt-4 flex items-center justify-between text-[9px] font-semibold text-crimson">Discover stories <ArrowRight size={13} /></Link></motion.aside></AnimatePresence><div className="absolute bottom-5 left-1/2 flex -translate-x-1/2 gap-2" role="tablist" aria-label="Hero slides">{heroSlides.map((item, index) => <button key={item.title} role="tab" aria-selected={active === index} aria-label={`Show hero slide ${index + 1}: ${item.title.replace("\n", " ")}`} onClick={() => select(index)} onFocus={() => setPaused(true)} onBlur={() => setPaused(false)} className={`h-1.5 rounded-full transition-all duration-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-white ${active === index ? "w-8 bg-crimson" : "w-4 bg-white/75 hover:bg-white"}`} />)}</div></div>
  </section>;
}
