import type { Metadata } from "next";
import "./globals.css";
import { AuthProvider } from "@/context/auth";
import { StoreProvider } from "@/context/store";

export const metadata: Metadata = { title: "RK Traders | Live in Remarkable", description: "Cinematic furniture and home decor experience" };
export default function Layout({ children }: Readonly<{ children: React.ReactNode }>) { return <html lang="en"><body><AuthProvider><StoreProvider>{children}</StoreProvider></AuthProvider></body></html>; }
