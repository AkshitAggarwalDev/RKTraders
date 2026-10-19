#!/usr/bin/env bash
# migrate_images.sh — Copy the local uploads/ directory (product images) to the
# machine that will run the backend in production (Railway), OR any host you
# choose. The database stores only filenames; the files themselves live on
# disk in the path set by FILE_UPLOAD_DIR (default: uploads).
#
# Usage examples:
#   ./scripts/migrate_images.sh user@your-vps:/srv/rktraders-uploads
#   ./scripts/migrate_images.sh  -> lists current uploads stats only
set -euo pipefail

UPLOAD_DIR="${FILE_UPLOAD_DIR:-uploads}"

if [[ ! -d "$UPLOAD_DIR" ]]; then
  echo "No $UPLOAD_DIR directory found here; nothing to migrate." >&2
  exit 0
fi

if [[ $# -eq 0 ]]; then
  echo "Uploads dir: $UPLOAD_DIR"
  echo "Files:        $(find "$UPLOAD_DIR" -type f | wc -l)"
  echo ""
  echo "Usage: $0 <destination, e.g. user@host:/srv/rktraders/uploads>"
  exit 0
fi

echo "Rsyncing $UPLOAD_DIR -> $1 ..."
rsync -av --partial "$UPLOAD_DIR"/ "$1"/
echo "Done."
