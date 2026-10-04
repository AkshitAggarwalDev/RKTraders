"use client";

import { Check, ChevronRight, LoaderCircle, MapPin, Pencil, Plus } from "lucide-react";
import { FormEvent, useCallback, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { CustomerRoute } from "@/components/customer-route";
import { Navigation } from "@/components/navigation";
import { useStore } from "@/context/store";
import { errorMessage } from "@/lib/api/client";
import { addressService, orderService } from "@/lib/api/services";
import type { Address } from "@/lib/api/types";
import { money } from "@/lib/data";

const emptyAddress = (): Address => ({ fullName: "", phoneNumber: "", houseNumber: "", street: "", NearByLoc: "", city: "", state: "", pincode: "", country: "India", addressType: "HOME", defaultAddress: false });
const addressLine = (address: Address) => [address.houseNumber, address.street, address.NearByLoc].filter(Boolean).join(", ");

function CheckoutContent() {
  const router = useRouter();
  const { cart, total, loadingCart, refreshCart } = useStore();
  const [addresses, setAddresses] = useState<Address[]>([]);
  const [selectedAddressId, setSelectedAddressId] = useState<number | null>(null);
  const [form, setForm] = useState<Address>(emptyAddress);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [loadingAddresses, setLoadingAddresses] = useState(true);
  const [savingAddress, setSavingAddress] = useState(false);
  const [placingOrder, setPlacingOrder] = useState(false);
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
      setSelectedAddressId(saved.id ?? null); resetForm(); await loadAddresses();
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

  async function placeOrder() {
    if (!selectedAddressId) { setError("Select a saved address before placing your order."); return; }
    if (!cart.length) { setError("Your cart is empty."); return; }
    setPlacingOrder(true); setError(null); setMessage(null);
    try {
      const order = await orderService.place(selectedAddressId);
      await refreshCart();
      router.push(`/payment/${order.id}`);
    } catch (reason) { setError(errorMessage(reason)); }
    finally { setPlacingOrder(false); }
  }

  const selectedAddress = addresses.find((address) => address.id === selectedAddressId) ?? null;
  return <main className="min-h-screen bg-porcelain pb-16"><Navigation /><section className="mx-auto max-w-[1280px] px-5 pt-36 sm:px-10 lg:px-14"><p className="text-[10px] font-bold uppercase tracking-[.2em] text-crimson">Secure checkout</p><h1 className="editorial mt-3 text-6xl text-wine sm:text-8xl">Complete your room.</h1><p className="mt-4 max-w-xl text-sm leading-6 text-wine/65">Choose a saved delivery address to place your order. Payment is completed on the next step.</p>
    {(error || message) && <p className={`mt-7 rounded-2xl px-5 py-4 text-sm ${error ? "bg-red-50 text-crimson" : "bg-emerald-50 text-emerald-800"}`}>{error ?? message}</p>}
    <div className="mt-10 grid gap-8 lg:grid-cols-[minmax(0,1fr)_360px]"><div className="space-y-6">
      <section className="rounded-showroom bg-white p-5 shadow-air sm:p-7"><div className="flex items-center justify-between"><div><p className="text-[10px] font-bold uppercase tracking-[.18em] text-crimson">01 · Delivery address</p><h2 className="editorial mt-2 text-4xl text-wine">Where it&apos;s going.</h2></div><MapPin className="text-crimson" /></div>
        {loadingAddresses ? <div className="grid min-h-32 place-items-center"><LoaderCircle className="animate-spin text-crimson" /></div> : addresses.length ? <div className="mt-6 grid gap-3">{addresses.map((address) => <div key={address.id} className={`rounded-2xl border p-4 transition ${selectedAddressId === address.id ? "border-crimson bg-red-50/60" : "border-wine/10 hover:border-crimson/35"}`}><button type="button" onClick={() => setSelectedAddressId(address.id ?? null)} className="w-full text-left"><div className="flex justify-between gap-4"><div><p className="font-bold text-wine">{address.fullName} {address.defaultAddress && <span className="ml-2 text-[9px] uppercase tracking-[.14em] text-crimson">Default</span>}</p><p className="mt-1 text-sm text-wine/65">{address.phoneNumber}</p><p className="mt-2 text-sm leading-5 text-wine/70">{addressLine(address)}<br />{address.city}, {address.state} · {address.pincode}</p></div>{selectedAddressId === address.id && <span className="grid size-6 shrink-0 place-items-center rounded-full bg-crimson text-white"><Check size={14} /></span>}</div></button><div className="mt-4 flex flex-wrap gap-4 text-[10px] font-bold uppercase tracking-[.12em] text-crimson"><button type="button" onClick={() => beginEdit(address)}>Edit</button><button type="button" onClick={() => void deleteAddress(address)}>Delete</button>{!address.defaultAddress && <button type="button" onClick={() => void makeDefault(address.id!)}>Set default</button>}</div></div>)}</div> : <p className="mt-6 rounded-2xl border border-dashed border-wine/15 px-5 py-7 text-sm text-wine/60">You have no saved addresses yet. Add one below to continue.</p>}
      </section>
      <AddressForm form={form} setForm={setForm} editingId={editingId} saving={savingAddress} onSubmit={saveAddress} onCancel={resetForm} />
    </div><aside className="h-fit rounded-showroom bg-wine p-6 text-white sm:p-7"><p className="text-[10px] font-bold uppercase tracking-[.2em] text-red-200">Order summary</p><div className="mt-6 space-y-4 border-b border-white/15 pb-5">{loadingCart ? <LoaderCircle className="animate-spin text-red-200" size={18} /> : cart.length ? cart.map((line) => <div key={line.id} className="flex justify-between gap-4 text-sm"><span className="min-w-0 truncate text-white/75">{line.product.name} × {line.quantity}</span><span>{money(line.product.price * line.quantity)}</span></div>) : <p className="text-sm text-white/65">Your cart is empty.</p>}</div><div className="mt-5 flex justify-between text-lg font-bold"><span>Total</span><span>{money(total)}</span></div><button onClick={placeOrder} disabled={!selectedAddress || !cart.length || placingOrder} className="mt-7 inline-flex w-full items-center justify-center gap-2 rounded-full bg-white px-5 py-3.5 text-sm font-bold text-crimson disabled:cursor-not-allowed disabled:opacity-50">{placingOrder ? <LoaderCircle className="animate-spin" size={17} /> : <>Place order <ChevronRight size={17} /></>}</button><p className="mt-3 text-center text-[11px] leading-4 text-white/60">You&apos;ll review payment instructions next. No payment is recorded at this step.</p></aside></div>
  </section></main>;
}

function AddressForm({ form, setForm, editingId, saving, onSubmit, onCancel }: { form: Address; setForm: (address: Address) => void; editingId: number | null; saving: boolean; onSubmit: (event: FormEvent<HTMLFormElement>) => void; onCancel: () => void }) { return <section className="rounded-showroom bg-white p-5 shadow-air sm:p-7"><div className="flex items-center gap-3"><span className="grid size-8 place-items-center rounded-full bg-red-50 text-crimson">{editingId ? <Pencil size={15} /> : <Plus size={16} />}</span><div><p className="text-[10px] font-bold uppercase tracking-[.18em] text-crimson">{editingId ? "Edit address" : "New address"}</p><h2 className="editorial mt-1 text-3xl text-wine">{editingId ? "Refine the details." : "A new destination."}</h2></div></div><form onSubmit={onSubmit} className="mt-6 grid gap-4 sm:grid-cols-2"><Field label="Full name" value={form.fullName} onChange={(value) => setForm({ ...form, fullName: value })} /><Field label="Phone" type="tel" value={form.phoneNumber} onChange={(value) => setForm({ ...form, phoneNumber: value })} /><Field label="House / flat number" value={form.houseNumber} onChange={(value) => setForm({ ...form, houseNumber: value })} /><Field label="Street" value={form.street} onChange={(value) => setForm({ ...form, street: value })} /><Field label="Nearby landmark" value={form.NearByLoc ?? ""} onChange={(value) => setForm({ ...form, NearByLoc: value })} required={false} /><Field label="City" value={form.city} onChange={(value) => setForm({ ...form, city: value })} /><Field label="State" value={form.state} onChange={(value) => setForm({ ...form, state: value })} /><Field label="Pincode" value={form.pincode} onChange={(value) => setForm({ ...form, pincode: value })} /><Field label="Country" value={form.country} onChange={(value) => setForm({ ...form, country: value })} /><label className="text-xs font-semibold text-wine">Address type<select value={form.addressType} onChange={(event) => setForm({ ...form, addressType: event.target.value as Address["addressType"] })} className="mt-2 block w-full rounded-2xl border border-wine/15 bg-white px-4 py-3.5 text-sm outline-none focus:border-crimson"><option value="HOME">Home</option><option value="OFFICE">Office</option><option value="WORK">Work</option></select></label><label className="sm:col-span-2 flex items-center gap-3 text-sm text-wine/70"><input type="checkbox" checked={Boolean(form.defaultAddress)} onChange={(event) => setForm({ ...form, defaultAddress: event.target.checked })} className="size-4 accent-[#d61f26]" /> Make this my default address</label><div className="sm:col-span-2 flex flex-wrap gap-3"><button disabled={saving} className="inline-flex items-center gap-2 rounded-full bg-crimson px-5 py-3 text-xs font-bold text-white disabled:opacity-60">{saving && <LoaderCircle className="animate-spin" size={14} />}{editingId ? "Save address" : "Add address"}</button>{editingId && <button type="button" onClick={onCancel} className="rounded-full border border-wine/15 px-5 py-3 text-xs font-bold text-wine">Cancel</button>}</div></form></section>; }
function Field({ label, value, onChange, type = "text", required = true }: { label: string; value: string; onChange: (value: string) => void; type?: string; required?: boolean }) { return <label className="text-xs font-semibold text-wine">{label}<input required={required} type={type} value={value} onChange={(event) => onChange(event.target.value)} className="mt-2 block w-full rounded-2xl border border-wine/15 bg-white px-4 py-3.5 text-sm outline-none focus:border-crimson" /></label>; }
export default function CheckoutPage() { return <CustomerRoute><CheckoutContent /></CustomerRoute>; }
