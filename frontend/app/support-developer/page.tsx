"use client";

import Link from "next/link";
import { useState, useEffect } from "react";
import { ArrowLeft, Check, Heart, ShieldAlert, Sparkles } from "lucide-react";
import { Navigation } from "@/components/navigation";

export default function SupportDeveloperPage() {
  const [email, setEmail] = useState("");
  const [amount, setAmount] = useState<2 | 5>(2);
  const [showIntegrationNotice, setShowIntegrationNotice] = useState(false);

  useEffect(() => {
    try {
      const stored = sessionStorage.getItem("developer_support_email");
      if (stored) {
        setEmail(stored);
      }
    } catch {
      // Storage access exception handling
    }
  }, []);

  return (
    <main className="min-h-screen bg-porcelain pb-20">
      <Navigation />

      <section className="mx-auto max-w-[880px] px-5 pt-36 sm:px-10 lg:px-14">
        <Link
          href="/"
          className="inline-flex items-center gap-2 text-xs font-bold uppercase tracking-[.15em] text-wine/60 transition hover:text-crimson"
        >
          <ArrowLeft size={14} /> Back to RK Traders
        </Link>

        <div className="mt-8">
          <div className="inline-flex items-center gap-2 rounded-full bg-red-100/70 px-3.5 py-1 text-[10px] font-bold uppercase tracking-[.2em] text-crimson">
            <Heart size={12} className="fill-crimson text-crimson" /> Optional Contribution
          </div>
          <h1 className="editorial mt-4 text-5xl leading-tight text-wine sm:text-7xl">
            Support the Developer
          </h1>
          <p className="mt-4 max-w-xl text-sm leading-7 text-wine/70 sm:text-base">
            RK Traders is an independently crafted showroom experience built with passion for considered living and timeless design. Your optional contribution directly supports ongoing engineering, new features, and continuous performance improvements.
          </p>
        </div>

        <div className="mt-10 max-w-xl rounded-showroom border border-wine/10 bg-white p-6 shadow-air sm:p-9">
          {email ? (
            <div className="mb-6 rounded-2xl border border-wine/10 bg-wine/[0.03] px-4 py-3 text-xs text-wine/70">
              <span className="font-semibold text-wine">Contributing as:</span>{" "}
              <span className="font-mono text-wine/90">{email}</span>
            </div>
          ) : (
            <div className="mb-6">
              <label className="block text-xs font-semibold text-wine">
                Your email address
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="Enter your email"
                  className="mt-2 block w-full rounded-2xl border border-wine/15 bg-white px-4 py-3 text-sm text-wine outline-none transition focus:border-crimson"
                />
              </label>
            </div>
          )}

          <div className="space-y-3">
            <p className="text-xs font-bold uppercase tracking-[.15em] text-wine/80">
              Select Contribution Amount
            </p>

            <div className="grid grid-cols-2 gap-4">
              {/* Option ₹2 */}
              <button
                type="button"
                onClick={() => { setAmount(2); setShowIntegrationNotice(false); }}
                className={`group relative flex flex-col items-start justify-between rounded-2xl border p-5 text-left transition ${
                  amount === 2
                    ? "border-crimson bg-red-50/70 shadow-sm ring-1 ring-crimson"
                    : "border-wine/15 bg-white hover:border-crimson/40 hover:bg-wine/[0.02]"
                }`}
              >
                <div className="flex w-full items-center justify-between">
                  <span className="editorial text-3xl font-bold text-wine">₹2</span>
                  <span
                    className={`grid size-6 place-items-center rounded-full transition ${
                      amount === 2
                        ? "bg-crimson text-white"
                        : "border border-wine/25 text-transparent"
                    }`}
                  >
                    <Check size={14} />
                  </span>
                </div>
                <p className="mt-3 text-[11px] font-medium text-wine/60">
                  A small token of appreciation
                </p>
              </button>

              {/* Option ₹5 */}
              <button
                type="button"
                onClick={() => { setAmount(5); setShowIntegrationNotice(false); }}
                className={`group relative flex flex-col items-start justify-between rounded-2xl border p-5 text-left transition ${
                  amount === 5
                    ? "border-crimson bg-red-50/70 shadow-sm ring-1 ring-crimson"
                    : "border-wine/15 bg-white hover:border-crimson/40 hover:bg-wine/[0.02]"
                }`}
              >
                <div className="flex w-full items-center justify-between">
                  <span className="editorial text-3xl font-bold text-wine">₹5</span>
                  <span
                    className={`grid size-6 place-items-center rounded-full transition ${
                      amount === 5
                        ? "bg-crimson text-white"
                        : "border border-wine/25 text-transparent"
                    }`}
                  >
                    <Check size={14} />
                  </span>
                </div>
                <p className="mt-3 text-[11px] font-medium text-wine/60">
                  A warm cup of chai to fuel coding
                </p>
              </button>
            </div>
          </div>

          {showIntegrationNotice ? (
            <div className="mt-8 rounded-2xl border border-wine/15 bg-wine/[0.04] p-5">
              <div className="flex items-start gap-3">
                <ShieldAlert className="mt-0.5 shrink-0 text-crimson" size={20} />
                <div>
                  <h3 className="text-sm font-bold text-wine">
                    Payment Gateway Integration Ready
                  </h3>
                  <p className="mt-1 text-xs leading-relaxed text-wine/75">
                    Thank you for choosing to support with <strong className="text-crimson">₹{amount}</strong>
                    {email ? ` (${email})` : ""}. Direct developer contribution processing is currently in setup mode. No charges have been deducted from your account.
                  </p>
                  <div className="mt-4 flex flex-wrap gap-3">
                    <button
                      type="button"
                      onClick={() => setShowIntegrationNotice(false)}
                      className="rounded-full border border-wine/20 px-4 py-2 text-xs font-bold text-wine transition hover:border-wine"
                    >
                      Change Amount
                    </button>
                    <Link
                      href="/"
                      className="rounded-full bg-wine px-4 py-2 text-xs font-bold text-white transition hover:bg-crimson"
                    >
                      Return to Showroom
                    </Link>
                  </div>
                </div>
              </div>
            </div>
          ) : (
            <div className="mt-8">
              <button
                type="button"
                onClick={() => setShowIntegrationNotice(true)}
                className="inline-flex w-full items-center justify-center gap-3 rounded-full bg-crimson px-6 py-4 text-sm font-bold text-white shadow-air transition hover:bg-[#a90006]"
              >
                <Sparkles size={16} /> Contribute ₹{amount}
              </button>
              <p className="mt-3 text-center text-[11px] text-wine/50">
                Secure checkout integration · Contributions are completely voluntary
              </p>
            </div>
          )}
        </div>
      </section>
    </main>
  );
}
