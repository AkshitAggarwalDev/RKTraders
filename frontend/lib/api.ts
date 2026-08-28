// Transitional façade for existing product/cart components. New code imports domain services from @/lib/api/services.
import type { CartLine, Product as UiProduct } from "@/lib/data";
import { API_BASE_URL, apiClient, errorMessage } from "./api/client";
import { authService, cartService, customerService, productImageService, productService } from "./api/services";
import { session } from "./api/session";
import type { Product } from "./api/types";

export { apiClient as api, errorMessage };
export const auth = { token: session.token, loggedIn: session.hasToken, saveToken: session.saveToken, clear: session.clear };

const tones = ["#8B0000", "#D61F26", "#9d3428", "#7b2021"];
const assetUrl = (imageUrl?: string) => imageUrl ? (imageUrl.startsWith("http") ? imageUrl : `${API_BASE_URL}/uploads/${imageUrl}`) : "/images/studio-chair.png";
async function toUiProduct(product: Product): Promise<UiProduct> {
  let imageUrl: string | undefined;
  try { const images = await productImageService.byProduct(product.id); imageUrl = (images.find((image) => image.primaryImage) ?? images[0])?.imageUrl; } catch { /* handled during the product module */ }
  return { id: product.id, name: product.name, price: product.price, image: assetUrl(imageUrl), tone: tones[product.id % tones.length], badge: product.status === "ACTIVE" ? "New" : product.status, category: product.category?.name ?? "Uncategorised", description: product.description, stock: product.stock, brand: product.brand, status: product.status };
}

export const catalogApi = { all: async () => Promise.all((await productService.all()).map(toUiProduct)), byId: async (id: number) => toUiProduct(await productService.byId(id)), search: async (name: string) => Promise.all((await productService.search(name)).map(toUiProduct)) };
export const authApi = { login: (email: string, password: string) => authService.login({ email, password }), register: async (name: string, email: string, password: string) => { await authService.register({ name, email, password }); await authService.login({ email, password }); }, profile: customerService.profile };
export const cartApi = { lines: async (): Promise<CartLine[]> => Promise.all((await cartService.lines()).map(async (line) => ({ id: line.id, quantity: line.quantity, product: await toUiProduct(line.product) }))), add: (productId: number, quantity = 1) => cartService.add(productId, quantity), update: (cartItemId: number, quantity: number) => cartService.update(cartItemId, quantity), remove: (cartItemId: number) => cartService.remove(cartItemId) };
