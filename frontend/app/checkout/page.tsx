"use client";

import Link from "next/link";
import { Check, ChevronRight, CreditCard, LoaderCircle, MapPin, Pencil, Plus, Trash2 } from "lucide-react";
import { FormEvent, useCallback, useEffect, useState } from "react";
import { CustomerRoute } from "@/components/customer-route";
import { Navigation } from "@/components/navigation";
import { useStore } from "@/context/store";
import { errorMessage } from "@/lib/api/client";
import { addressService, paymentService } from "@/lib/api/services";
import type { Address, Payment } from "@/lib/api/types";
import { money } from "@/lib/data";

const emptyAddress = (): Address => ({ fullName: "", phoneNumber: "", houseNumber: "", street: "", NearByLoc: "", city: "", state: "", pincode: "", country: "India", addressType: "HOME", defaultAddress: false });

function addressLine(address: Address) { return [address.houseNumber, address.street, address.NearByLoc].filter(Boolean).join(", "); }

function CheckoutContent() {
  const { cart, total, loadingCart, refreshCart } = useStore();
  const [addresses, setAddresses] = useState<Address[]>([]);
  const [selectedAddressId, setSelectedAddressId] = useState<number | null>(null);
  const [form, setForm] = useState<Address>(emptyAddress);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [loadingAddresses, setLoadingAddresses] = useState(true);
  const [savingAddress, setSavingAddress] = useState(false);
  const [initiatingPayment, setInitiatingPayment] = useState(false);
  const [verifyingPayment, setVerifyingPayment] = useState(false);
  const [payment, setPayment] = useState<Payment | null>(null);
  const [transactionId, setTransactionId] = useState("");
  const [message, setMessage] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const loadAddresses = useCallback(async () => {
    setLoadingAddresses(true); setError(null);
    try {
      const saved = await addressService.all();
      setAddresses(saved);
      setSelectedAddressId((current) => current && saved.some((address) => address.id === current) ? current : saved.find((address) => address.defaultAddress)?.id ?? saved[0]?.id ?? null);
    } catch (reason) { setError(errorMessage(reason)); }
    finally { setLoadingAddresses(false); }
  }, []);

  useEffect(() => { void loadAddresses(); }, [loadAddresses]);

  const resetForm = () => { setForm(emptyAddress()); setEditingId(null); };
  const beginEdit = (address: Address) => { setForm({ ...address, defaultAddress: Boolean(address.defaultAddress) }); setEditingId(address.id ?? null); setError(null); setMessage(null); };

  async function saveAddress(event: FormEvent<HTMLFormElement>) {
    event.preventDefault(); setSavingAddress(true); setError(null); setMessage(null);
    try {
      const saved = editingId ? await addressService.update(editingId, form) : await addressService.add(form);
      setSelectedAddressId(saved.id ?? null);
      resetForm(); await loadAddresses();
      setMessage(editingId ? "Address updated." : "Address saved.");
    } catch (reason) { setError(errorMessage(reason)); }
    finally { setSavingAddress(false); }
  }

  async function deleteAddress(address: Address) {
    if (!address.id || !window.confirm("Delete this saved address?")) return;
    setError(null); setMessage(null);
    try { await addressService.remove(address.id); await loadAddresses(); setMessage("Address deleted."); }
    catch (reason) { setError(errorMessage(reason)); }
  }

  async function makeDefault(addressId: number) {
    setError(null); setMessage(null);
    try { await addressService.setDefault(addressId); setSelectedAddressId(addressId); await loadAddresses(); setMessage("Default address updated."); }
    catch (reason) { setError(errorMessage(reason)); }
  }

  async function initiatePayment() {
    if (!selectedAddressId) { setError("Select a saved address before continuing to payment."); return; }
    setInitiatingPayment(true); setError(null); setMessage(null);
    try { setPayment(await paymentService.initiate(selectedAddressId)); }
    catch (reason) { setError(errorMessage(reason)); }
    finally { setInitiatingPayment(false); }
  }

  async function verifyPayment(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!payment) return;
    setVerifyingPayment(true); setError(null); setMessage(null);
    try {
      const verified = await paymentService.verify(payment.paymentId, transactionId.trim());
      setPayment(verified);
      await refreshCart();
      setMessage("The backend recorded this payment as successful and created your order.");
    } catch (reason) { setError(errorMessage(reason)); }
    finally { setVerifyingPayment(false); }
  }

  const selectedAddress = addresses.find((address) => address.id === selectedAddressId) ?? null;
  const paymentSucceeded = payment?.paymentStatus === "SUCCESS";

  return <main className="min-h-screen bg-porcelain pb-16"><Navigation /><section className="mx-auto max-w-[1280px] px-5 pt-36 sm:px-10 lg:px-14"><p className="text-[10px] font-bold uppercase tracking-[.2em] text-crimson">Secure checkout</p><h1 className="editorial mt-3 text-6xl text-wine sm:text-8xl">Complete your room.</h1><p className="mt-4 max-w-xl text-sm leading-6 text-wine/65">Choose a saved delivery address, then use the payment flow currently provided by RK Traders.</p>
    {(error || message) && <p className={`mt-7 rounded-2xl px-5 py-4 text-sm ${error ? "bg-red-50 text-crimson" : "bg-emerald-50 text-emerald-800"}`}>{error ?? message}</p>}
    <div className="mt-10 grid gap-8 lg:grid-cols-[minmax(0,1fr)_360px]">
      <div className="space-y-6">
        <section className="rounded-showroom bg-white p-5 shadow-air sm:p-7"><div className="flex items-center justify-between"><div><p className="text-[10px] font-bold uppercase tracking-[.18em] text-crimson">01 · Delivery address</p><h2 className="editorial mt-2 text-4xl text-wine">Where it&apos;s going.</h2></div><MapPin className="text-crimson" /></div>
          {loadingAddresses ? <div className="grid min-h-32 place-items-center"><LoaderCircle className="animate-spin text-crimson" /></div> : addresses.length ? <div className="mt-6 grid gap-3">{addresses.map((address) => <button disabled={Boolean(payment)} type="button" key={address.id} onClick={() => setSelectedAddressId(address.id ?? null)} className={`w-full rounded-2xl border p-4 text-left transition disabled:cursor-not-allowed disabled:opacity-70 ${selectedAddressId === address.id ? "border-crimson bg-red-50/60" : "border-wine/10 hover:border-crimson/35"}`}><div className="flex justify-between gap-4"><div><p className="font-bold text-wine">{address.fullName} {address.defaultAddress && <span className="ml-2 text-[9px] uppercase tracking-[.14em] text-crimson">Default</span>}</p><p className="mt-1 text-sm text-wine/65">{address.phoneNumber}</p><p className="mt-2 text-sm leading-5 text-wine/70">{addressLine(address)}<br />{address.city}, {address.state} · {address.pincode}</p></div>{selectedAddressId === address.id && <span className="grid size-6 shrink-0 place-items-center rounded-full bg-crimson text-white"><Check size={14} /></span>}</div><span className="mt-4 flex flex-wrap gap-4 text-[10px] font-bold uppercase tracking-[.12em] text-crimson"><span onClick={(event) => { event.stopPropagation(); beginEdit(address); }}>Edit</span><span onClick={(event) => { event.stopPropagation(); void deleteAddress(address); }}>Delete</span>{!address.defaultAddress && <span onClick={(event) => { event.stopPropagation(); void makeDefault(address.id!); }}>Set default</span>}</span></button>)}</div> : <p className="mt-6 rounded-2xl border border-dashed border-wine/15 px-5 py-7 text-sm text-wine/60">You have no saved addresses yet. Add one below to continue.</p>}
        </section>
        <section className="rounded-showroom bg-white p-5 shadow-air sm:p-7"><div className="flex items-center gap-3"><span className="grid size-8 place-items-center rounded-full bg-red-50 text-crimson">{editingId ? <Pencil size={15} /> : <Plus size={16} />}</span><div><p className="text-[10px] font-bold uppercase tracking-[.18em] text-crimson">{editingId ? "Edit address" : "New address"}</p><h2 className="editorial mt-1 text-3xl text-wine">{editingId ? "Refine the details." : "A new destination."}</h2></div></div><form onSubmit={saveAddress} className="mt-6 grid gap-4 sm:grid-cols-2"><Field label="Full name" value={form.fullName} onChange={(value) => setForm({ ...form, fullName: value })} /><Field label="Phone" type="tel" value={form.phoneNumber} onChange={(value) => setForm({ ...form, phoneNumber: value })} /><Field label="House / flat number" value={form.houseNumber} onChange={(value) => setForm({ ...form, houseNumber: value })} /><Field label="Street" value={form.street} onChange={(value) => setForm({ ...form, street: value })} /><Field label="Nearby landmark" value={form.NearByLoc ?? ""} onChange={(value) => setForm({ ...form, NearByLoc: value })} required={false} /><Field label="City" value={form.city} onChange={(value) => setForm({ ...form, city: value })} /><Field label="State" value={form.state} onChange={(value) => setForm({ ...form, state: value })} /><Field label="Pincode" value={form.pincode} onChange={(value) => setForm({ ...form, pincode: value })} /><Field label="Country" value={form.country} onChange={(value) => setForm({ ...form, country: value })} /><label className="text-xs font-semibold text-wine">Address type<select value={form.addressType} onChange={(event) => setForm({ ...form, addressType: event.target.value as Address["addressType"] })} className="mt-2 block w-full rounded-2xl border border-wine/15 bg-white px-4 py-3.5 text-sm outline-none focus:border-crimson"><option value="HOME">Home</option><option value="OFFICE">Office</option><option value="WORK">Work</option></select></label><label className="sm:col-span-2 flex items-center gap-3 text-sm text-wine/70"><input type="checkbox" checked={Boolean(form.defaultAddress)} onChange={(event) => setForm({ ...form, defaultAddress: event.target.checked })} className="size-4 accent-[#d61f26]" /> Make this my default address</label><div className="sm:col-span-2 flex flex-wrap gap-3"><button disabled={savingAddress} className="inline-flex items-center gap-2 rounded-full bg-crimson px-5 py-3 text-xs font-bold text-white disabled:opacity-60">{savingAddress && <LoaderCircle className="animate-spin" size={14} />}{editingId ? "Save address" : "Add address"}</button>{editingId && <button type="button" onClick={resetForm} className="rounded-full border border-wine/15 px-5 py-3 text-xs font-bold text-wine">Cancel</button>}</div></form></section>
      </div>
      <aside className="h-fit rounded-showroom bg-wine p-6 text-white sm:p-7"><p className="text-[10px] font-bold uppercase tracking-[.2em] text-red-200">Order summary</p><div className="mt-6 space-y-4 border-b border-white/15 pb-5">{loadingCart ? <LoaderCircle className="animate-spin text-red-200" size={18} /> : cart.length ? cart.map((line) => <div key={line.id} className="flex justify-between gap-4 text-sm"><span className="min-w-0 truncate text-white/75">{line.product.name} × {line.quantity}</span><span>{money(line.product.price * line.quantity)}</span></div>) : <p className="text-sm text-white/65">Your cart is empty.</p>}</div><div className="mt-5 flex justify-between text-lg font-bold"><span>Total</span><span>{money(total)}</span></div>
        {!payment ? <button onClick={initiatePayment} disabled={!selectedAddress || !cart.length || initiatingPayment} className="mt-7 inline-flex w-full items-center justify-center gap-2 rounded-full bg-white px-5 py-3.5 text-sm font-bold text-crimson disabled:cursor-not-allowed disabled:opacity-50">{initiatingPayment ? <LoaderCircle className="animate-spin" size={17} /> : <>Continue to payment <ChevronRight size={17} /></>}</button> : <div className="mt-7"><div className="rounded-2xl bg-white/10 p-4"><div className="flex items-center gap-3"><CreditCard size={18} className="text-red-200" /><div><p className="text-sm font-bold">{payment.paymentMethod}</p><p className="text-xs text-white/65">Backend status: {payment.paymentStatus}</p></div></div><p className="mt-4 text-sm text-white/80">Pay {money(payment.amount)} to <strong>{payment.upiId}</strong> using your UPI app.</p></div>{paymentSucceeded ? <div className="mt-5 rounded-2xl bg-emerald-400/15 p-4 text-sm text-emerald-100"><p className="font-bold">Payment recorded as {payment.paymentStatus}.</p><p className="mt-2 text-emerald-50/80">The backend created the order after verification. It does not return an order ID in this payment response.</p><Link href="/account" className="mt-4 inline-block font-bold underline underline-offset-4">Return to your account</Link></div> : <form onSubmit={verifyPayment} className="mt-5"><label className="block text-xs font-semibold text-white/85">UPI transaction ID<input required value={transactionId} onChange={(event) => setTransactionId(event.target.value)} placeholder="Transaction ID from your UPI app" className="mt-2 block w-full rounded-xl border border-white/20 bg-white/10 px-4 py-3 text-sm text-white outline-none placeholder:text-white/45 focus:border-red-200" /></label><p className="mt-3 text-[11px] leading-4 text-red-100/80">RK Traders currently records the entered transaction ID itself; no payment gateway verification is integrated.</p><button disabled={verifyingPayment || !transactionId.trim()} className="mt-4 inline-flex w-full items-center justify-center gap-2 rounded-full bg-white px-5 py-3.5 text-sm font-bold text-crimson disabled:opacity-50">{verifyingPayment ? <LoaderCircle className="animate-spin" size={17} /> : "Record payment verification"}</button></form>}</div>}</aside>
    </div></section></main>;
}

function Field({ label, value, onChange, type = "text", required = true }: { label: string; value: string; onChange: (value: string) => void; type?: string; required?: boolean }) { return <label className="text-xs font-semibold text-wine">{label}<input required={required} type={type} value={value} onChange={(event) => onChange(event.target.value)} className="mt-2 block w-full rounded-2xl border border-wine/15 bg-white px-4 py-3.5 text-sm outline-none focus:border-crimson" /></label>; }

export default function CheckoutPage() { return <CustomerRoute><CheckoutContent /></CustomerRoute>; }
