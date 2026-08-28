export type Product = {
  id: number;
  name: string;
  price: number;
  image: string;
  tone: string;
  badge?: string;
  category: string;
  description?: string;
  stock?: number;
  brand?: string;
  status?: string;
};

export type CartLine = { id: number; quantity: number; product: Product };

export const fallbackProducts: Product[] = [
  { id: 1, name: "Luna Lounge Chair", price: 78900, image: "/images/scarlet-chair.png", tone: "#7c090e", badge: "New", category: "Living", description: "Sculptural comfort for expressive interiors.", stock: 12, status: "ACTIVE" },
  { id: 2, name: "Arco Dining Table", price: 124900, image: "/images/noir-table.jpg", tone: "#b13a28", badge: "New", category: "Dining", description: "A crisp centrepiece designed for gatherings.", stock: 7, status: "ACTIVE" },
  { id: 3, name: "Rovere Sideboard", price: 89900, image: "/images/studio-chair.png", tone: "#8B0000", badge: "New", category: "Decor", description: "A considered storage piece with a quiet presence.", stock: 5, status: "ACTIVE" },
  { id: 4, name: "Orbit Table Lamp", price: 18900, image: "/images/noir-table.jpg", tone: "#d61f26", category: "Lighting", description: "Warm, ambient light for slow evenings.", stock: 14, status: "ACTIVE" },
];

export const collections = [
  { title: "Modern Living", image: "/images/hero-showroom.png", position: "58% center" },
  { title: "Timeless Dining", image: "/images/hero-showroom.png", position: "70% center" },
  { title: "Refined Bedroom", image: "/images/hero-showroom.png", position: "89% center" },
];

export const money = (value: number) => new Intl.NumberFormat("en-IN", { style: "currency", currency: "INR", maximumFractionDigits: 0 }).format(value);
