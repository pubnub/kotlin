#!/usr/bin/env bash
# Java (GSON) SDK integration tests (real PubNub; needs credentials). Opt-in: run explicitly.
# Usage: scripts/dev/integration-gson.sh [--tests "com.pubnub.foo.BarTest"]
set -uo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/_lib.sh"

require_credentials integration-gson
run_gradle integration-gson pubnub-gson/pubnub-gson-impl :pubnub-gson:pubnub-gson-impl:integrationTest "$@"
