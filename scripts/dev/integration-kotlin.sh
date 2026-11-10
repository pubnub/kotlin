#!/usr/bin/env bash
# Kotlin SDK integration tests (real PubNub; needs credentials). Opt-in: run explicitly.
# Usage: scripts/dev/integration-kotlin.sh [--tests "com.pubnub.foo.BarTest"]
set -uo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/_lib.sh"

require_credentials integration-kotlin
run_gradle integration-kotlin pubnub-kotlin/pubnub-kotlin-impl :pubnub-kotlin:pubnub-kotlin-impl:integrationTest "$@"
