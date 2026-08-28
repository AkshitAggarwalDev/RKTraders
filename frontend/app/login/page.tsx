"use client";

import Link from "next/link";
import { ArrowRight, Eye, EyeOff, LoaderCircle } from "lucide-react";
import { FormEvent, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { authApi, errorMessage } from "@/lib/api";

export default function LoginPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const next = searchParams.get("next") || "/account";

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault(); setError(""); setLoading(true);
    try { await authApi.login(email, password); router.replace(next); }
    catch (reason) { setError(errorMessage(reason)); setLoading(false); }
  }

  return <main className="grid min-h-screen bg-[#300001] p-4 sm:p-7 lg:grid-cols-2"><section className="relative hidden overflow-hidden rounded-showroom bg-[url('/images/hero-showroom.png')] bg-cover bg-[60%_center] lg:block"><div className="absolute inset-0 bg-gradient-to-t from-wine/80 to-transparent" /><Link href="/" className="editorial absolute left-9 top-8 text-3xl text-white">RK TRADERS</Link><div className="absolute bottom-10 left-10 max-w-md text-white"><p className="text-[10px] font-bold uppercase tracking-[.2em] text-red-200">Welcome back</p><h1 className="editorial mt-3 text-7xl leading-[.85]">Return to<br />remarkable.</h1></div></section><section className="mx-auto flex w-full max-w-md flex-col justify-center px-5 py-14 text-white sm:px-8"><Link href="/" className="editorial text-3xl lg:hidden">RK TRADERS</Link><p className="mt-14 text-[10px] font-bold uppercase tracking-[.2em] text-red-200">Your RK account</p><h1 className="editorial mt-3 text-6xl leading-none">Welcome in.</h1><p className="mt-4 text-sm leading-6 text-white/70">Sign in to save favourites, manage your cart, and keep track of every order.</p><form onSubmit={submit} className="mt-9 space-y-4"><label className="block text-xs font-semibold text-white/85">Email address<input required type="email" autoComplete="email" value={email} onChange={(event) => setEmail(event.target.value)} className="mt-2 block w-full rounded-2xl border border-white/20 bg-white/10 px-4 py-3.5 text-white outline-none transition focus:border-red-300" /></label><label className="block text-xs font-semibold text-white/85">Password<div className="relative mt-2"><input required minLength={6} type={showPassword ? "text" : "password"} autoComplete="current-password" value={password} onChange={(event) => setPassword(event.target.value)} className="block w-full rounded-2xl border border-white/20 bg-white/10 px-4 py-3.5 pr-12 text-white outline-none transition focus:border-red-300" /><button type="button" onClick={() => setShowPassword((current) => !current)} className="absolute right-3 top-1/2 -translate-y-1/2 text-white/70" aria-label="Show or hide password">{showPassword ? <EyeOff size={18} /> : <Eye size={18} />}</button></div></label>{error && <p className="rounded-xl border border-red-300/30 bg-red-300/10 px-4 py-3 text-sm text-red-100">{error}</p>}<button disabled={loading} className="mt-2 inline-flex w-full items-center justify-center gap-3 rounded-full bg-white px-5 py-4 text-sm font-bold text-crimson transition hover:bg-red-50 disabled:opacity-60">{loading ? <LoaderCircle className="animate-spin" size={18} /> : <>Sign in <ArrowRight size={18} /></>}</button></form><p className="mt-7 text-center text-sm text-white/65">New to RK Traders? <Link href={`/register?next=${encodeURIComponent(next)}`} className="font-bold text-white underline underline-offset-4">Create an account</Link></p></section></main>;
}
