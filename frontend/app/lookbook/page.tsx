"use client";

import Link from "next/link";
import { ArrowRight } from "lucide-react";
import { motion, useScroll, useTransform } from "framer-motion";
import { Navigation } from "@/components/navigation";
import { lookbookScenes } from "@/lib/editorial";

function Scene({ scene, index }: { scene: (typeof lookbookScenes)[number]; index: number }) {
  const { scrollYProgress } = useScroll();
  const y = useTransform(scrollYProgress, [0, 1], [0, index % 2 ? -35 : 35]);
  return <section className="relative min-h-[82svh] overflow-hidden bg-wine sm:min-h-[90svh]"><motion.div style={{ y, backgroundImage: `url(${scene.image})`, backgroundPosition: scene.position }} initial={{ scale: 1.08 }} whileInView={{ scale: 1.02 }} viewport={{ once: true, amount: .25 }} transition={{ duration: 1.4, ease: [0.22, 1, 0.36, 1] }} className="absolute -inset-8 bg-cover" /><div className="absolute inset-0 bg-[linear-gradient(90deg,rgba(19,1,2,.83),rgba(43,2,3,.48)_48%,rgba(19,1,2,.1))]" /><div className={`relative mx-auto flex min-h-[82svh] max-w-[1520px] items-end px-6 py-14 sm:min-h-[90svh] sm:px-12 sm:py-20 lg:px-16 ${index % 2 ? "lg:justify-end" : ""}`}><motion.div initial={{ opacity: 0, y: 34 }} whileInView={{ opacity: 1, y: 0 }} viewport={{ once: true, amount: .35 }} transition={{ duration: .75, ease: [0.22, 1, 0.36, 1] }} className="max-w-xl text-white"><p className="text-[10px] font-bold uppercase tracking-[.23em] text-red-100">{String(index + 1).padStart(2, "0")} · {scene.kicker}</p><h2 className="editorial mt-4 text-[clamp(3.5rem,7vw,7.6rem)] leading-[.78]">{scene.title}</h2><p className="mt-6 max-w-sm text-sm leading-6 text-white/80">{scene.description}</p><Link href={scene.href} className="mt-7 inline-flex items-center gap-4 rounded-full border border-white/60 bg-white/10 px-5 py-3 text-xs font-bold text-white backdrop-blur transition hover:bg-white hover:text-crimson">{scene.label} <ArrowRight size={15} /></Link></motion.div></div></section>;
}

export default function LookbookPage() { return <main className="bg-[#160102]"><Navigation /><div className="pt-0">{lookbookScenes.map((scene, index) => <Scene key={scene.kicker} scene={scene} index={index} />)}<section className="bg-porcelain px-6 py-20 text-center sm:px-12 sm:py-28"><p className="text-[10px] font-bold uppercase tracking-[.22em] text-crimson">The next room is yours</p><h2 className="editorial mx-auto mt-4 max-w-3xl text-6xl leading-[.82] text-wine sm:text-8xl">Bring your point of view home.</h2><Link href="/products" className="mt-8 inline-flex items-center gap-4 rounded-full bg-crimson px-6 py-4 text-sm font-bold text-white shadow-air">Explore the collection <ArrowRight size={17} /></Link></section></div></main>; }
