# RK Traders — MySQL → Supabase PostgreSQL Migration Runbook

Read this top to bottom. Do **not** touch the live Supabase database until step 2.

## 0. What changed in the code

| Change | Why |
|---|---|
| `pom.xml`: added `org.postgresql:postgresql` (runtime) | PostgreSQL JDBC driver; MySQL driver kept |
| `src/main/resources/application-postgres.properties` | New Spring profile `postgres` — identical env-var names (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JPA_DDL_AUTO`, `JWT_SECRET`, `OWNER_*`, `FILE_UPLOAD_DIR`) so nothing else changes |
| `.env.example` | Documents all variables for MySQL dev and Supabase prod |
| `scripts/export_products.py` | Exports `category`, `product`, `product_image` from local MySQL, preserving IDs |
| `scripts/import_products.sh` | Applies `import_supabase.sql` via `psql` over TLS and verifies counts/FKs |
| `scripts/migrate_images.sh` | rsyncs the `uploads/` directory to the production backend host |
| `WebConfig.java` | CORS origins now env-driven via `CORS_ALLOWED_ORIGINS` (comma-separated), default unchanged for local dev — required for Vercel production |
| `frontend/.env.example` | Reverted an accidental local-LAN IP in the template back to `localhost:8080` (template hygiene only — no runtime file changed) |

Nothing in entities, repositories, services, controllers, or frontend needed changes
(verified — no native SQL anywhere; all repositories are derived queries; enums are
`@Enumerated(EnumType.STRING)`; IDs are `GenerationType.IDENTITY`, which Hibernate
maps to PostgreSQL `GENERATED AS IDENTITY` automatically).

## 1. Why code didn't need changes (compatibility review result)

- 15 entities all use `GenerationType.IDENTITY` → Hibernate maps this to PostgreSQL identity columns; no change needed.
- All repository queries are Spring Data derived queries; zero `nativeQuery` usage, zero MySQL functions (`DATE_FORMAT`, `NOW()` etc.) anywhere.
- All enums stored as VARCHAR via `@Enumerated(EnumType.STRING)` (already portable; `PaymentEntity` even pins `@JdbcTypeCode(Types.VARCHAR)`).
- Timestamps use `java.time.LocalDateTime` with `@CreationTimestamp`/`@UpdateTimestamp` — portable.
- Tables are created purely by `ddl-auto=update`; no SQL init scripts exist.
- `orders` and other tables go through Hibernate naming (SnakeCase implicit naming), identical on both databases.

## 2. Initialize Supabase (first time only — needs your password)

1. Copy `.env.example` → `.env`, set the Supabase `DB_URL` (see variants below), `DB_USERNAME=postgres`, `DB_PASSWORD=<your Supabase DB password>`.
2. Start the backend once in postgres mode — it creates the whole schema:
   ```
   ./mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres
   ```
   Watch the logs for successful startup, then stop it (Ctrl+C).
   (Schema creation via `ddl-auto=update` runs inside the app; nothing is executed directly against Supabase by any script.)
3. Optionally set `JPA_DDL_AUTO=validate` afterwards and restart once to prove the schema is complete.

Connection URL variants (paste the right one into `DB_URL`):

```
# Direct (works from Railway; may be blocked by some ISP/cloud egress restrictions)
DB_URL=jdbc:postgresql://db.tzwmraagnoiuwbcdkhim.supabase.co:5432/postgres?ssl=true&sslmode=require

# Session pooler (IPv4-friendly; use if direct host is unreachable from your network)
DB_URL=jdbc:postgresql://aws-0-<REGION>.pooler.supabase.com:5432/postgres?ssl=true&sslmode=require
DB_USERNAME=postgres.tzwmraagnoiuwbcdkhim      # pooler username includes the project ref
```

Copy the exact pooler host/username from **Supabase → Project Settings → Database → Connection string**.

## 3. Migrate the product catalogue

```
# a) Export from local MySQL (reads .env DB_* values; does not modify anything)
pip install mysql-connector-python
python scripts/export_products.py

# b) Import into Supabase (reads SUPABASE_* variables from the environment only)
export SUPABASE_DB_HOST=db.tzwmraagnoiuwbcdkhim.supabase.co
export SUPABASE_DB_USER=postgres
export SUPABASE_DB_NAME=postgres
export SUPABASE_DB_PASSWORD='<paste, never echo>'
./scripts/import_products.sh
```

The script truncates only `product_image`, `product`, `category` (customer/order/cart/review data is untouched) and resets ID sequences afterwards.

## 4. Product images

Images are files on disk (`uploads/`), referenced by filename in `product_image.image_url`. Deploy path:

- **Railway**: Railway's filesystem is ephemeral — files would be wiped on redeploy. Use a Railway Volume mounted at the `FILE_UPLOAD_DIR` path, then run `./scripts/migrate_images.sh <rsync target>` once from a machine that can reach the container, or restore the volume from a tarball uploaded via `railway run`. Until then, owner product-image upload works, but images uploaded before migration are lost unless restored from your local `uploads/`.
- Long-term recommendation (out of scope today): move image storage to Supabase Storage/R2 — flag to the owner.

## 5. Running locally

MySQL (unchanged dev flow):
```
./mvnw.cmd spring-boot:run          # uses .env DB_URL=jdbc:mysql://...
```

PostgreSQL/Supabase:
```
# Set DB_URL/DB_USERNAME/DB_PASSWORD to Supabase values in .env, then:
./mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres
```

## 6. Production deployment checklist

### Railway (backend)
- [ ] Environment variables (Railway → Variables):
  - `SPRING_PROFILES_ACTIVE=postgres`
  - `DB_URL` (direct or pooler URL, `sslmode=require`)
  - `DB_USERNAME`, `DB_PASSWORD`
  - `JWT_SECRET` (long random value; do not reuse the dev value)
  - `JWT_EXPIRATION=3600000`
  - `OWNER_NAME`, `OWNER_EMAIL`, `OWNER_PASSWORD`
  - `JPA_DDL_AUTO=update` (first deploy) → flip to `validate` after first boot
  - `FILE_UPLOAD_DIR=/data/uploads` with a Volume mounted at `/data`
- [ ] Build command: `./mvnw -DskipTests package`; start command: `java -jar target/web-0.0.1-SNAPSHOT.jar`
- [ ] Railway knows Java 21 — set `JAVA_VERSION=21` in variables, or use an nixpacks default that resolves 21.

### Supabase
- [ ] Schema created by backend's first boot (`ddl-auto=update`)
- [ ] Catalogue imported via `import_products.sh`; counts verified
- [ ] `products_with_missing_category = 0` verified by the import script

### Vercel (frontend)
- [ ] `NEXT_PUBLIC_API_BASE_URL=https://<backend>.up.railway.app`
- [ ] Never any DB/JWT/owner secret, only this public API URL, in Vercel env vars.

### Backend CORS
- `WebConfig` must allow the Vercel origin (currently localhost); update before production. ← required code touch before deploy, not done in this task.

### Smoke tests after deploy
- [ ] `GET /product/getAllProducts` returns imported catalogue
- [ ] Customer register → login → authed call works (JWT)
- [ ] Owner login → owner-only endpoints work
- [ ] Add to cart → checkout with saved address → COD order placed → confirmation page shows order + COD
- [ ] Order history lists the order; another customer sees none of it
- [ ] Product image upload (owner) works; image serving returns the file

## 7. Secrets
All secrets are environment variables only. Never paste DB password, JWT secret, or owner password into the repo, frontend `.env`, or chat logs.
