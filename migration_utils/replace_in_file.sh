#!/bin/bash
file="$4"
search="$1"
replace="$2"
dry_run="$3"

if [[ "$dry_run" == "true" ]]; then
  if grep -q "$search" "$file"; then
    echo "$file"
  fi
else
  sed -i '' "s#$search#$replace#g" "$file"
fi
