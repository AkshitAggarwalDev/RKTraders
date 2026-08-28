"use client";

import { LoaderCircle } from "lucide-react";
import { usePathname, useRouter } from "next/navigation";
import { useEffect, type ReactNode } from "react";
import { useAuth } from "@/context/auth";

export function CustomerRoute({ children }: { children: ReactNode }) {
  const { isAuthenticated, isRestoring } = useAuth();
  const pathname = usePathname();
  const router = useRouter();
  useEffect(() => { if (!isRestoring && !isAuthenticated) router.replace(`/login?next=${encodeURIComponent(pathname)}`); }, [isAuthenticated, isRestoring, pathname, router]);
  if (isRestoring || !isAuthenticated) return <main className="grid min-h-screen place-items-center bg-porcelain"><LoaderCircle className="animate-spin text-crimson" size={28} /></main>;
  return <>{children}</>;
}
