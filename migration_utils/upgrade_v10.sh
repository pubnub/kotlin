#!/bin/bash
set -e
# Function to check if a file contains a search string

# Main script
if [ $# -lt 2 ]; then
  echo "Usage: $0 <replacements_file> <directory> [-d]"
  exit 1
fi

dry_run=false
replacements_file="$1"
directory="$2"

# Check for dry run flag
shift 2
while [[ $# -gt 0 ]]; do
  case "$1" in
    -d|--dry-run)
      dry_run=true
      ;;
    *)
      echo "Unknown option: $1"
      exit 1
      ;;
  esac
  shift
done

# Read replacements from file
while IFS= read -r line; do
  if [[ $line =~ ^([^:]+):(.+)$ ]]; then
    search="${BASH_REMATCH[1]}"
    replace="${BASH_REMATCH[2]}"
    echo "Replacing '$search' with '$replace'"

    # Find all files and apply the replacement or print
    find "$directory" -type f -print0 | xargs -0 -n 1 ./replace_in_file.sh "$search" "$replace" "$dry_run" | sort -u
  fi
done < "$replacements_file"