"use client";

import { motion, useReducedMotion } from "framer-motion";
import { usePathname, useRouter } from "next/navigation";
import { useCallback, useEffect, useRef, useState } from "react";

/**
 * Global RK Traders page transition.
 *
 * A single full-screen burgundy curtain (existing wine brand color) sweeps in
 * during internal navigations, covering the white flash of the incoming page,
 * then sweeps out to reveal the destination. One instance lives in the root
 * layout; no page or Link needs to change.
 *
 * Architecture notes:
 * - The curtain is ONE persistent element driven by a phase machine
 *   (idle → cover → exit → idle). It never unmounts mid-flight, so a rapid
 *   re-click simply re-animates the same curtain back in — duplicates are
 *   structurally impossible.
 * - Click interception uses a capture-phase listener on `document`, so it is
 *   immune to re-renders/remounts and always sees the click first, before any
 *   other handler. Navigation itself is never altered.
 * - Reveal waits for BOTH the curtain covering AND the destination route
 *   committing (pathname effect, covering Link clicks, router.push/replace/
 *   back/forward, and browser back/forward via popstate). Same-route changes
 *   (e.g. /products?category=A → ?category=B) render instantly, so the
 *   curtain reveals right after covering.
 * - Failsafe timers always reveal the page — the curtain can never stick.
 * - External links (other origins), mail/tel, target="_blank", downloads,
 *   modified/aux-clicks behave exactly as before.
 * - prefers-reduced-motion: a gentle crossfade instead of the sweep.
 * - Overlay is aria-hidden and pointer-events: none — it can never block the
 *   UI, even in the worst case.
 */

const COVER_MS = 380;
const EXIT_MS = 440;
const FAILSAFE_MS = 1800; // absolute upper bound for any single transition

type Phase = "idle" | "cover" | "exit";

const variants = {
  in: { x: "0%", transition: { duration: COVER_MS / 1000, ease: [0.65, 0, 0.35, 1] as const } },
  out: { x: "-100%", transition: { duration: EXIT_MS / 1000, ease: [0.65, 0, 0.35, 1] as const } },
};

const fadeVariants = {
  in: { opacity: 1, transition: { duration: 0.18, ease: "easeOut" as const } },
  out: { opacity: 0, transition: { duration: 0.3, ease: "easeOut" as const } },
};

export function PageTransition({ children }: { children: React.ReactNode }) {
  const router = useRouter();
  const pathname = usePathname();
  const reducedMotion = useReducedMotion();

  const [phase, setPhase] = useState<Phase>("idle");
  const activeRef = useRef(false);
  const arrivedRef = useRef(false);
  const holdRef = useRef(false);
  const pathnameRef = useRef(pathname);
  const failsafeRef = useRef<number | null>(null);
  const coverBackupRef = useRef<number | null>(null);

  useEffect(() => { pathnameRef.current = pathname; }, [pathname]);

  const clearTimers = useCallback(() => {
    if (failsafeRef.current !== null) { window.clearTimeout(failsafeRef.current); failsafeRef.current = null; }
    if (coverBackupRef.current !== null) { window.clearTimeout(coverBackupRef.current); coverBackupRef.current = null; }
  }, []);

  /** Reveal (sweep out) once the curtain covers AND the destination committed. */
  const tryReveal = useCallback(() => {
    if (!arrivedRef.current || !holdRef.current) return;
    clearTimers();
    setPhase("exit");
  }, [clearTimers]);

  const startTransition = useCallback((instantArrival: boolean) => {
    arrivedRef.current = instantArrival; // same-route changes render instantly
    holdRef.current = false;
    activeRef.current = true;
    clearTimers();
    coverBackupRef.current = window.setTimeout(() => { coverBackupRef.current = null; holdRef.current = true; tryReveal(); }, COVER_MS + 140);
    failsafeRef.current = window.setTimeout(() => { arrivedRef.current = true; holdRef.current = true; tryReveal(); }, FAILSAFE_MS);
    setPhase("cover"); // same element re-animates in place if already visible
  }, [tryReveal]);

  const onAnimationComplete = useCallback((definition: unknown) => {
    if (definition === "out") {
      activeRef.current = false;
      arrivedRef.current = false;
      holdRef.current = false;
      setPhase("idle");
    } else if (definition === "in") {
      holdRef.current = true;
      tryReveal();
    }
  }, [tryReveal]);

  // Destination route committed (Link click, router method, or popstate):
  // mark arrival and reveal once the curtain has finished covering.
  useEffect(() => {
    if (!activeRef.current) return;
    arrivedRef.current = true;
    tryReveal();
  }, [pathname, tryReveal]);

  // Programmatic navigation gets the same curtain without touching callers.
  useEffect(() => {
    const original = { push: router.push, replace: router.replace, back: router.back, forward: router.forward };
    const instant = (href: unknown) => {
      const path = typeof href === "string"
        ? href.split(/[?#]/)[0]
        : (href && typeof href === "object" && "pathname" in href && typeof href.pathname === "string" && href.pathname) || "";
      return path === window.location.pathname;
    };
    router.push = (...args: Parameters<typeof original.push>) => { startTransition(instant(args[0])); return original.push(...args); };
    router.replace = (...args: Parameters<typeof original.replace>) => { startTransition(instant(args[0])); return original.replace(...args); };
    router.back = (...args: Parameters<typeof original.back>) => { startTransition(false); return original.back(...args); };
    router.forward = (...args: Parameters<typeof original.forward>) => { startTransition(false); return original.forward(...args); };
    return () => { router.push = original.push; router.replace = original.replace; router.back = original.back; router.forward = original.forward; };
  }, [router, startTransition]);

  // Browser back/forward fire popstate after the URL has already changed: the
  // destination is the same page exactly when its pathname matches the one
  // currently rendered (search-param-only back/forward reveals immediately).
  useEffect(() => {
    const onPopstate = () => startTransition(window.location.pathname === pathnameRef.current);
    window.addEventListener("popstate", onPopstate);
    return () => window.removeEventListener("popstate", onPopstate);
  }, [startTransition]);

  // Capture-phase click interception: sees every click first, immune to
  // re-renders, and never interferes with how navigation actually happens.
  useEffect(() => {
    const onClick = (event: MouseEvent) => {
      if (event.button !== 0 || event.metaKey || event.ctrlKey || event.shiftKey || event.altKey) return;
      const anchor = (event.target as Element | null)?.closest?.("a") as HTMLAnchorElement | null;
      if (!anchor) return;
      const href = anchor.getAttribute("href");
      if (!href || href.startsWith("#") || anchor.target === "_blank" || anchor.hasAttribute("download")) return;
      if (anchor.rel.split(/\s+/).includes("external")) return;
      const url = new URL(anchor.href, window.location.href);
      if (url.origin !== window.location.origin) return; // external: untouched
      if (url.pathname + url.search === window.location.pathname + window.location.search) return; // same destination
      startTransition(url.pathname === window.location.pathname); // same-route (search-only) renders instantly
      // Next <Link> and plain anchors then navigate exactly as before.
    };
    document.addEventListener("click", onClick, true);
    return () => document.removeEventListener("click", onClick, true);
  }, [startTransition]);

  useEffect(() => clearTimers, [clearTimers]);

  return (
    <>
      {children}
      {phase !== "idle" && (
        <motion.div
          aria-hidden
          initial={reducedMotion ? { opacity: 0 } : { x: "100%" }}
          animate={phase === "exit" ? "out" : "in"}
          variants={reducedMotion ? fadeVariants : variants}
          onAnimationComplete={onAnimationComplete}
          className="fixed inset-0 z-[200] grid place-items-center bg-wine"
          style={{ pointerEvents: "none" }}
        >
          <div className="flex flex-col items-center">
            <span className="editorial text-3xl tracking-[.08em] text-white">RK TRADERS</span>
            <span className="mt-4 h-[2px] w-24 overflow-hidden rounded-full bg-white/25">
              <motion.span
                className="block h-full w-full rounded-full bg-white"
                initial={{ x: reducedMotion ? "0%" : "-100%" }}
                animate={{ x: "0%" }}
                transition={{ duration: reducedMotion ? 0.18 : 0.42, ease: "easeOut" }}
              />
            </span>
          </div>
        </motion.div>
      )}
    </>
  );
}
