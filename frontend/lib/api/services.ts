import { apiClient } from "./client";
import { isJwt, session } from "./session";
import type { Address, CartItem, Category, Customer, LoginRequest, Order, Owner, PasswordUpdateRequest, Payment, Product, ProductImage, RegisterRequest, Review, ReviewRequest, ReviewSummary } from "./types";

function isRecord(value: unknown): value is Record<string, unknown> {
  return typeof value === "object" && value !== null;
}

function isOrderItem(value: unknown): boolean {
  return isRecord(value)
    && typeof value.id === "number"
    && typeof value.productName === "string"
    && typeof value.productPrice === "number"
    && typeof value.quantity === "number"
    && typeof value.totalPrice === "number";
}

function isOrder(value: unknown): value is Order {
  return isRecord(value)
    && typeof value.id === "number"
    && typeof value.totalAmount === "number"
    && typeof value.orderStatus === "string"
    && typeof value.orderDate === "string"
    && Array.isArray(value.orderItems)
    && value.orderItems.every(isOrderItem);
}

function parseOrderHistory(value: unknown): Order[] {
  // The backend serializes the customer's own orders as a top-level JSON
  // array. Accept it as-is; anything else is a genuine contract break.
  if (Array.isArray(value) && value.every(isOrder)) { return value; }
  // A disabled/legacy config may nest the array inside an object; pull the
  // first array-valued field out so history still renders rather than throwing.
  if (isRecord(value)) {
    for (const nested of Object.values(value)) {
      if (Array.isArray(nested) && nested.every(isOrder)) { return nested; }
    }
  }
  throw new Error("We received an unexpected order-history response. Please try again.");
}
function parseOrder(value: unknown): Order {
  if (!isOrder(value)) throw new Error("The order was created, but its confirmation could not be read. Please open Order History.");
  return value;
}
function orderIdFrom(value: unknown): number | null {
  if (!isRecord(value)) return null;
  for (const key of ["id", "orderId", "OrderId"]) {
    const candidate = value[key];
    if (typeof candidate === "number" && Number.isInteger(candidate) && candidate > 0) return candidate;
    if (typeof candidate === "string" && /^\d+$/.test(candidate) && Number(candidate) > 0) return Number(candidate);
  }
  return null;
}
function parseCreatedOrder(value: unknown): { id: number } {
  const id = orderIdFrom(value);
  if (!id) throw new Error("The order response did not include a valid order number.");
  return { id };
}
function parseOrderIds(value: unknown): number[] {
  if (!Array.isArray(value)) throw new Error("Order History returned an unexpected response.");
  const ids = value.map(orderIdFrom);
  if (ids.some((id) => !id)) throw new Error("Order History returned an order without a valid number.");
  return ids as number[];
}
export const authService = { async login(request: LoginRequest) { const { data } = await apiClient.post<unknown>("/customer/login", request); if (!isJwt(data)) throw new Error(typeof data === "string" && data.trim() ? data : "Unable to sign in"); session.saveToken(data); }, async register(request: RegisterRequest) { await apiClient.post<Customer>("/customer/register", request); }, logout: () => session.clear() };
export const customerService = { profile: async () => (await apiClient.get<Customer>("/customer/profile")).data, updateProfile: async (customer: Customer) => (await apiClient.put<Customer>("/customer/updateProfile", customer)).data, updatePassword: async (request: PasswordUpdateRequest) => (await apiClient.patch<string>("/customer/updatePassword", request)).data, deleteProfile: async () => (await apiClient.delete<string>("/customer/deleteProfile")).data };
export const categoryService = { byId: async (id: number) => (await apiClient.get<Category>(`/category/getCategory/${id}`)).data, search: async (name: string) => (await apiClient.get<Category>("/category/search", { params: { name } })).data, add: async (category: Omit<Category, "id">) => (await apiClient.post<Category>("/category/addCategory", category)).data, update: async (id: number, category: Category) => (await apiClient.put<Category>(`/category/updateCategory/${id}`, category)).data, remove: async (id: number) => (await apiClient.delete<string>(`/category/deleteCategory/${id}`)).data };
export const productService = { all: async () => (await apiClient.get<Product[]>("/products/allProducts")).data, byId: async (id: number) => (await apiClient.get<Product>(`/products/getProduct/${id}`)).data, search: async (name: string) => (await apiClient.get<Product[]>("/products/search", { params: { name } })).data, byCategory: async (categoryId: number) => (await apiClient.get<Product[]>(`/products/category/${categoryId}`)).data, addMany: async (products: Product[]) => (await apiClient.post<Product[]>("/products/addProducts", products)).data, update: async (id: number, product: Product) => (await apiClient.put<Product>(`/Products/updateProduct/${id}`, product)).data, remove: async (id: number) => (await apiClient.delete<string>(`/Products/deleteProduct/${id}`)).data };
export const productImageService = { byProduct: async (productId: number) => (await apiClient.get<ProductImage[]>(`/product-images/${productId}`)).data, upload: async (productId: number, image: File) => { const form = new FormData(); form.append("image", image); return (await apiClient.post<string>(`/product-images/upload/${productId}`, form, { headers: { "Content-Type": "multipart/form-data" } })).data; }, remove: async (imageId: number) => (await apiClient.delete<string>(`/product-images/${imageId}`)).data, setPrimary: async (imageId: number) => (await apiClient.put<string>(`/product-images/primary/${imageId}`)).data };
export const cartService = { lines: async () => (await apiClient.get<CartItem[]>("/cart/viewItems")).data, add: async (productId: number, quantity: number) => (await apiClient.post<string>(`/cart/add/${productId}`, null, { params: { quantity } })).data, update: async (cartItemId: number, quantity: number) => (await apiClient.put<string>(`/cart/update/${cartItemId}`, null, { params: { quantity } })).data, remove: async (cartItemId: number) => (await apiClient.delete<string>(`/cart/remove/${cartItemId}`)).data, clear: async () => (await apiClient.delete<string>("/cart/clear")).data, total: async () => (await apiClient.get<number>("/cart/total")).data };
export const addressService = { all: async () => (await apiClient.get<Address[]>("/Address/getMyAddress")).data, add: async (address: Address) => (await apiClient.post<Address>("/Address/addAddress", address)).data, update: async (id: number, address: Address) => (await apiClient.put<Address>(`/Address/updateAddress/${id}`, address)).data, remove: async (id: number) => (await apiClient.delete<string>(`/Address/delete/${id}`)).data, setDefault: async (id: number) => (await apiClient.patch<Address>(`/Address/default/${id}`)).data };
export const paymentService = { mine: async () => (await apiClient.get<Payment[]>("/payment/my")).data, byOrder: async (orderId: number) => (await apiClient.get<Payment>(`/payment/order/${orderId}`)).data };
export const orderService = { place: async (addressId: number) => parseCreatedOrder((await apiClient.post<unknown>("/orders/place", null, { params: { addressId } })).data), ids: async () => parseOrderIds((await apiClient.get<unknown>("/orders/myOrders")).data), mine: async () => parseOrderHistory((await apiClient.get<unknown>("/orders/myOrders")).data), mineById: async (orderId: number) => parseOrder((await apiClient.get<unknown>(`/orders/my/${orderId}`)).data), cancel: async (orderId: number) => (await apiClient.delete<string>(`/orders/cancel/${orderId}`)).data, all: async () => (await apiClient.get<Order[]>("/orders/all")).data };
export const reviewService = { byProduct: async (productId: number) => (await apiClient.get<Review[]>(`/review/getReview/${productId}`)).data, summary: async (productId: number) => (await apiClient.get<ReviewSummary>(`/review/summary/${productId}`)).data, add: async (productId: number, review: ReviewRequest) => (await apiClient.post<string>(`/review/addReview/${productId}`, review)).data, remove: async (reviewId: number) => (await apiClient.delete<string>(`/review/deleteReview/${reviewId}`)).data };
export const ownerService = { login: async (ownerEmail: string, ownerPassword: string) => (await apiClient.post<string>("/owner/settings/login", { ownerEmail, ownerPassword })).data, profile: async () => (await apiClient.get<Owner>("/owner/settings/profile")).data };
