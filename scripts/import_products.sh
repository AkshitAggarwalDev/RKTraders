#!/usr/bin/env bash
# import_products.sh — Import the exported catalogue into Supabase PostgreSQL.
#
# Prerequisites (run export_products.py first):
#   scripts/out/import_supabase.sql exists
#
# Usage:
#   SUPABASE_DB_PASSWORD=... ./scripts/import_products.sh
#
# The password is read from the environment only; never from a file in the repo.
set -euo pipefail

require_env_var() {  # name
  if [[ -z "${!1:-}" ]]; then
    echo "ERROR: $1 must be set in the environment (not stored in any file)." >&2
    exit 1
  fi
}

require_env_var SUPABASE_DB_PASSWORD
require_env_var SUPABASE_DB_HOST      # e.g. db.tzwmraagnoiuwbcdkhim.supabase.co
require_env_var SUPABASE_DB_USER      # e.g. postgres
require_env_var SUPABASE_DB_NAME      # e.g. postgres

SQL_FILE="scripts/out/import_supabase.sql"
[[ -f "$SQL_FILE" ]] || { echo "ERROR: $SQL_FILE missing — run scripts/export_products.py first." >&2; exit 1; }

echo "Applying $SQL_FILE to ${SUPABASE_DB_HOST}/${SUPABASE_DB_NAME} ..."
PGPASSWORD="$SUPABASE_DB_PASSWORD" PGSSLMODE=require psql \
  -h "$SUPABASE_DB_HOST" -p 5432 \
  -U "$SUPABASE_DB_USER" -d "$SUPABASE_DB_NAME" \
  -v ON_ERROR_STOP=1 \
  -f "$SQL_FILE"

echo ""
echo "Verifying imported record counts..."
PGPASSWORD="$SUPABASE_DB_PASSWORD" PGSSLMODE=require psql \
  -h "$SUPABASE_DB_HOST" -p 5432 \
  -U "$SUPABASE_DB_USER" -d "$SUPABASE_DB_NAME" \
  -v ON_ERROR_STOP=1 \
  -c "SELECT 'category' AS t, COUNT(*) FROM category
      UNION ALL SELECT 'product', COUNT(*) FROM product
      UNION ALL SELECT 'product_image', COUNT(*) FROM product_image;"

echo ""
echo "Verifying category relationships (products should have valid categories)..."
PGPASSWORD="$SUPABASE_DB_PASSWORD" PGSSLMODE=require psql \
  -h "$SUPABASE_DB_HOST" -p 5432 \
  -U "$SUPABASE_DB_USER" -d "$SUPABASE_DB_NAME" \
  -v ON_ERROR_STOP=1 \
  -c "SELECT COUNT(*) AS products_with_missing_category
      FROM product p LEFT JOIN category c ON p.category_id = c.id
      WHERE c.id IS NULL;"

echo ""
echo "Done. Expected: products_with_missing_category = 0."
