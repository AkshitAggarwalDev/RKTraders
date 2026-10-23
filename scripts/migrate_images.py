
import os
import sys
from pathlib import Path
from urllib.parse import urlparse

import psycopg
import cloudinary
import cloudinary.uploader
from dotenv import load_dotenv

ROOT = Path(__file__).resolve().parent.parent
UPLOAD_DIR = ROOT / "uploads"

load_dotenv(ROOT / ".env")

CLOUDINARY_URL = os.getenv("CLOUDINARY_URL")
DB_URL = os.getenv("SUPABASE_DATABASE_URL")

if not CLOUDINARY_URL:
    sys.exit("ERROR: CLOUDINARY_URL environment variable missing.")

if not DB_URL:
    sys.exit("ERROR: Set SUPABASE_DATABASE_URL to your Supabase PostgreSQL URI.")

if not UPLOAD_DIR.is_dir():
    sys.exit(f"ERROR: uploads folder not found: {UPLOAD_DIR}")

cloudinary.config(cloudinary_url=CLOUDINARY_URL)


def main():
    files = sorted(p for p in UPLOAD_DIR.iterdir() if p.is_file())

    if not files:
        sys.exit("No files found in uploads/.")

    print(f"Found {len(files)} local files.")
    print("Connecting to Supabase PostgreSQL...")

    with psycopg.connect(DB_URL, sslmode="require") as conn:
        with conn.cursor() as cur:
            cur.execute(
                """
                SELECT id, image_url
                FROM product_image
                ORDER BY id
                """
            )
            rows = cur.fetchall()

            by_filename = {
                Path(image_url).name: (image_id, image_url)
                for image_id, image_url in rows
                if image_url and not image_url.startswith(
                    "https://res.cloudinary.com/"
                )
            }

            migrated = 0
            skipped = 0

            for file_path in files:
                match = by_filename.get(file_path.name)

                if not match:
                    print(f"SKIP (no exact DB filename match): {file_path.name}")
                    skipped += 1
                    continue

                image_id, old_url = match

                try:
                    result = cloudinary.uploader.upload(
                        str(file_path),
                        folder="rk-traders/products",
                        resource_type="image",
                    )

                    secure_url = result.get("secure_url")
                    if not secure_url:
                        raise RuntimeError("Cloudinary returned no secure_url")

                    cur.execute(
                        """
                        UPDATE product_image
                        SET image_url = %s
                        WHERE id = %s AND image_url = %s
                        """,
                        (secure_url, image_id, old_url),
                    )

                    if cur.rowcount != 1:
                        raise RuntimeError(
                            "Database row changed; URL was not updated."
                        )

                    conn.commit()
                    print(f"OK id={image_id}: {file_path.name}")
                    migrated += 1

                except Exception as exc:
                    conn.rollback()
                    print(f"FAILED {file_path.name}: {exc}")
                    skipped += 1

    print("\nMigration finished.")
    print(f"Updated: {migrated}")
    print(f"Skipped/failed: {skipped}")


if __name__ == "__main__":
    main()
