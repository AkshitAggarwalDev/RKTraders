"use client";

import { Search, SlidersHorizontal } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { useSearchParams } from "next/navigation";
import { Navigation } from "@/components/navigation";
import { ProductCard } from "@/components/product-card";
import { catalogApi } from "@/lib/api";
import { fallbackProducts } from "@/lib/data";

export default function ProductsPage() {
  const searchParams = useSearchParams();
  const requestedCategory = searchParams.get("category") ?? "All";
  const sort = searchParams.get("sort");
  const [products, setProducts] = useState(fallbackProducts);
  const [query, setQuery] = useState("");
  const [category, setCategory] = useState(requestedCategory);
  const [loading, setLoading] = useState(true);

  useEffect(() => { setCategory(requestedCategory); }, [requestedCategory]);
  useEffect(() => {
    let active = true;
    void catalogApi.all().then((items) => {
      if (active && items.length) setProducts(items.filter((product) => product.status !== "INACTIVE"));
    }).catch(() => undefined).finally(() => { if (active) setLoading(false); });
    return () => { active = false; };
  }, []);

  const categories = useMemo(() => ["All", ...new Set(products.map((product) => product.category).filter(Boolean))], [products]);
  const displayed = useMemo(() => products
    .filter((product) => category === "All" || product.category.toLowerCase().includes(category.toLowerCase()))
    .filter((product) => `${product.name} ${product.category} ${product.brand ?? ""}`.toLowerCase().includes(query.toLowerCase()))
    .sort((a, b) => sort === "price" ? a.price - b.price : sort === "latest" ? b.id - a.id : 0), [category, products, query, sort]);

  return <main className="min-h-screen bg-porcelain pb-16">
    <Navigation />
    <section className="mx-auto max-w-[1500px] px-5 pb-8 pt-36 sm:px-10 lg:px-14">
      <p className="text-[10px] font-bold uppercase tracking-[.21em] text-crimson">RK Traders Catalogue</p>
      <div className="mt-3 flex flex-col justify-between gap-6 sm:flex-row sm:items-end"><div><h1 className="editorial text-6xl leading-none text-wine sm:text-8xl">The complete edit.</h1><p className="mt-4 max-w-md text-sm leading-6 text-wine/65">Furniture and objects selected for spaces with warmth, character, and staying power.</p></div><p className="text-sm text-wine/55">{loading ? "Refreshing collection…" : `${displayed.length} pieces`}</p></div>
      <div className="mt-9 flex flex-col gap-4 border-y border-wine/10 py-4 lg:flex-row lg:items-center lg:justify-between">
        <div className="hide-scrollbar flex max-w-full gap-2 overflow-x-auto"><SlidersHorizontal className="mt-2 shrink-0 text-crimson" size={17} />{categories.map((item) => <button key={item} onClick={() => setCategory(item)} className={`whitespace-nowrap rounded-full px-4 py-2 text-xs font-semibold transition ${category === item ? "bg-wine text-white" : "bg-white text-wine/70 hover:bg-red-50"}`}>{item}</button>)}</div>
        <label className="flex w-full items-center gap-2 rounded-full border border-wine/15 bg-white px-4 py-2.5 lg:max-w-xs"><Search size={16} className="text-crimson" /><input value={query} onChange={(event) => setQuery(event.target.value)} placeholder="Search the collection" className="w-full bg-transparent text-sm outline-none placeholder:text-wine/35" /></label>
      </div>
      {displayed.length ? <div className="mt-8 grid gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">{displayed.map((product, index) => <ProductCard key={product.id} product={product} index={index % 4} />)}</div> : <div className="mt-12 rounded-showroom border border-dashed border-wine/20 bg-white p-12 text-center"><h2 className="editorial text-4xl text-wine">Nothing quite like that yet.</h2><p className="mt-3 text-sm text-wine/60">Try another category or a more open search.</p><button onClick={() => { setCategory("All"); setQuery(""); }} className="mt-6 rounded-full bg-crimson px-5 py-3 text-xs font-bold text-white">Reset filters</button></div>}
    </section>
  </main>;
}
