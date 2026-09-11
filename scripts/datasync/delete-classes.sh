#!/usr/bin/env bash
#
# delete-classes.sh — remove the custom Test* DataSync classes that create-classes.sh
# registers. 404s are expected on a keyset the classes were never registered on.
#
# Deleting a class does NOT delete the entities/relationships registered under it —
# they are simply left orphaned (no instance-driven 409). Bounded to the known Test*
# names only; there is no bulk delete and this must never touch User/Channel/Membership.
#
# Usage:
#   SDK_DS_API_KEY=... SDK_DS_SUB_KEY=... ./delete-classes.sh

set -euo pipefail

. "$(dirname "${BASH_SOURCE[0]}")/shared.sh"

require_environment

CLASS_PATHS=(
  "relationship-classes/TestOwnership/versions/1"
  "relationship-classes/TestFriendship/versions/1"
  "entity-classes/TestUser/versions/1"
  "entity-classes/TestNode/versions/1"
)

say "deleting ${#CLASS_PATHS[@]} class versions"

for path in "${CLASS_PATHS[@]}"; do
  meta_delete "$META/$path"
done

finish
