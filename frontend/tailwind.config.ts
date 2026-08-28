import type { Config } from "tailwindcss";
const config: Config = {
  content: ["./app/**/*.{ts,tsx}", "./components/**/*.{ts,tsx}", "./context/**/*.{ts,tsx}"],
  theme: { extend: {
    colors: { crimson: "#D61F26", wine: "#8B0000", porcelain: "#FAFAFA" },
    boxShadow: { air: "0 18px 55px rgba(74, 6, 8, .13)", luxe: "0 34px 80px rgba(51, 3, 5, .30)" },
    borderRadius: { showroom: "1.75rem" },
    fontFamily: { editorial: ["Iowan Old Style", "Baskerville", "Times New Roman", "serif"] }
  } },
  plugins: []
};
export default config;
