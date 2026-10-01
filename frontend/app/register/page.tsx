"use client";

import Link from "next/link";
import { ArrowRight, Eye, EyeOff, LoaderCircle } from "lucide-react";
import { FormEvent, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { authApi, errorMessage } from "@/lib/api";
import { ApiError } from "@/lib/api/client";
import { registrationValidationMessage } from "@/lib/auth-validation";

export default function RegisterPage() {
  const router = useRouter(); const searchParams = useSearchParams(); const next = searchParams.get("next") || "/account";
  const [form, setForm] = useState({ name: "", email: "", password: "" });
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false); const [error, setError] = useState("");
  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const validationError = registrationValidationMessage(form);
    if (validationError) { setError(validationError); return; }

    setError(""); setLoading(true);
    try { await authApi.register(form.name, form.email, form.password); router.replace(next); }
    catch (reason) {
      setError(reason instanceof ApiError && [401, 403].includes(reason.status ?? 0)
        ? "We could not create your account right now. Please try again shortly."
        : errorMessage(reason));
      setLoading(false);
    }
  }
  return <main className="grid min-h-screen bg-porcelain lg:grid-cols-[.92fr_1.08fr]"><section className="bg-[linear-gradient(135deg,#430002,#a50009)] px-7 py-10 text-white sm:px-14 lg:flex lg:flex-col lg:justify-between"><Link href="/" className="editorial text-3xl">RK TRADERS</Link><div className="my-20 max-w-md"><p className="text-[10px] font-bold uppercase tracking-[.2em] text-red-200">A more considered home</p><h1 className="editorial mt-4 text-7xl leading-[.83]">Begin your<br />collection.</h1><p className="mt-7 max-w-sm text-sm leading-7 text-white/75">Create an account for a more personal way to browse, save, and order the pieces you love.</p></div><p className="text-xs text-white/55">Designed for homes with a point of view.</p></section><section className="mx-auto flex w-full max-w-xl flex-col justify-center px-7 py-14 sm:px-14"><p className="text-[10px] font-bold uppercase tracking-[.2em] text-crimson">Create your account</p><h2 className="editorial mt-3 text-6xl leading-none text-wine">Let&apos;s make it yours.</h2><form noValidate onSubmit={submit} className="mt-9 grid gap-5"><label className="text-xs font-semibold text-wine">Full name<input required minLength={2} autoComplete="name" value={form.name} onChange={(event) => setForm({ ...form, name: event.target.value })} aria-invalid={Boolean(error)} aria-describedby="name-help" className="mt-2 block w-full rounded-2xl border border-wine/15 bg-white px-4 py-3.5 text-sm outline-none focus:border-crimson" /><span id="name-help" className="mt-2 block font-normal text-wine/55">Use letters only; start each name with a capital letter.</span></label><label className="text-xs font-semibold text-wine">Email address<input required type="email" autoComplete="email" value={form.email} onChange={(event) => setForm({ ...form, email: event.target.value })} aria-describedby="email-help" className="mt-2 block w-full rounded-2xl border border-wine/15 bg-white px-4 py-3.5 text-sm outline-none focus:border-crimson" /><span id="email-help" className="mt-2 block font-normal text-wine/55">Use a valid email address that you can access.</span></label><label className="text-xs font-semibold text-wine">Password<div className="relative mt-2"><input required minLength={6} inputMode="numeric" pattern="[0-9]*" type={showPassword ? "text" : "password"} autoComplete="new-password" value={form.password} onChange={(event) => setForm({ ...form, password: event.target.value })} aria-describedby="password-help" className="block w-full rounded-2xl border border-wine/15 bg-white px-4 py-3.5 pr-12 text-sm outline-none focus:border-crimson" /><button type="button" onMouseEnter={() => setShowPassword(true)} onMouseLeave={() => setShowPassword(false)} onFocus={() => setShowPassword(true)} onBlur={() => setShowPassword(false)} className="absolute right-3 top-1/2 -translate-y-1/2 text-wine/60 hover:text-crimson" aria-label="Hover to reveal password">{showPassword ? <EyeOff size={18} /> : <Eye size={18} />}</button></div><span id="password-help" className="mt-2 block font-normal text-wine/55">At least 6 digits; numbers only. Hover over the eye icon to reveal it.</span></label>{error && <p role="alert" className="rounded-xl bg-red-50 px-4 py-3 text-sm text-crimson">{error}</p>}<button disabled={loading} className="inline-flex items-center justify-center gap-3 rounded-full bg-crimson px-5 py-4 text-sm font-bold text-white shadow-air disabled:opacity-60">{loading ? <LoaderCircle className="animate-spin" size={18} /> : <>Create account <ArrowRight size={18} /></>}</button></form><p className="mt-7 text-center text-sm text-wine/65">Already a member? <Link href={`/login?next=${encodeURIComponent(next)}`} className="font-bold text-crimson underline underline-offset-4">Sign in</Link></p></section></main>;
}
