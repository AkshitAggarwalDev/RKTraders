"use client";

import { LogOut, Mail, UserRound } from "lucide-react";
import { useRouter } from "next/navigation";
import { CustomerRoute } from "@/components/customer-route";
import { Navigation } from "@/components/navigation";
import { useAuth } from "@/context/auth";

function AccountContent() {
  const { customer, logout } = useAuth();
  const router = useRouter();
  const signOut = () => { logout(); router.replace("/"); };
  return <main className="min-h-screen bg-porcelain pb-16"><Navigation /><section className="mx-auto max-w-[980px] px-5 pt-36 sm:px-10 lg:px-14"><p className="text-[10px] font-bold uppercase tracking-[.2em] text-crimson">Your RK account</p><h1 className="editorial mt-3 text-6xl text-wine sm:text-8xl">Welcome back.</h1><div className="mt-10 max-w-2xl rounded-showroom bg-white p-6 shadow-air sm:p-9"><div className="flex items-center gap-4"><span className="grid size-12 place-items-center rounded-full bg-red-50 text-crimson"><UserRound size={22} /></span><div><h2 className="text-lg font-bold text-wine">{customer?.name}</h2><p className="mt-1 text-sm text-wine/55">Customer account</p></div></div><div className="mt-8 border-t border-wine/10 pt-6"><div className="flex items-center gap-3 text-sm text-wine/70"><Mail size={16} className="text-crimson" /><span>{customer?.email}</span></div></div><button onClick={signOut} className="mt-9 inline-flex items-center gap-3 rounded-full bg-wine px-5 py-3 text-xs font-bold uppercase tracking-[.12em] text-white transition hover:bg-crimson"><LogOut size={15} /> Sign out</button></div></section></main>;
}

export default function AccountPage() { return <CustomerRoute><AccountContent /></CustomerRoute>; }
