const TOKEN_KEY = "rk-traders.access-token";
export const AUTH_CHANGED_EVENT = "rk-auth-change";
function emit() { if (typeof window !== "undefined") window.dispatchEvent(new Event(AUTH_CHANGED_EVENT)); }
export const session = {
  token: () => typeof window === "undefined" ? null : window.localStorage.getItem(TOKEN_KEY),
  saveToken: (token: string) => { window.localStorage.setItem(TOKEN_KEY, token); emit(); },
  clear: () => { if (typeof window !== "undefined") { window.localStorage.removeItem(TOKEN_KEY); emit(); } },
  hasToken: () => Boolean(session.token()),
};
export function isJwt(value: unknown): value is string { return typeof value === "string" && /^[A-Za-z0-9_-]+\.[A-Za-z0-9_-]+\.[A-Za-z0-9_-]+$/.test(value); }
