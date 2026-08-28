# RK Traders showroom frontend

Standalone Next.js 15 frontend inspired by the approved red-and-white cinematic showroom preview. The Spring Boot backend has not been edited or connected in this phase.

## Run locally

```bash
cd frontend
npm install
npm run dev
```

Then open `http://localhost:3000`.

## Included

- Premium glass navigation and cinematic showroom hero
- Local, project-owned hero image at `public/images/hero-showroom.png`
- Responsive collection tiles and editor's-picks carousel strip
- Framer Motion and GSAP entrance/parallax motion
- Pointer-reactive 3D product cards
- Context-based wishlist and animated cart drawer, backed by mock data
- `lib/api.ts` as the reserved Axios seam for the later Spring API integration

No backend request occurs in this version. Set `NEXT_PUBLIC_API_BASE_URL` from `.env.example` only when backend integration is approved.
