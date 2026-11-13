#!/usr/bin/env bash
# Kotlin AND Java (GSON) integration suites in ONE Gradle invocation, run in parallel by Gradle itself.
# Prefer this over starting integration-kotlin.sh and integration-gson.sh at the same time: two separate
# Gradle processes in one checkout contend for the same build locks and the shared pubnub-kotlin-impl outputs.
# Usage: scripts/dev/integration-both.sh
set -uo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/_lib.sh"

require_credentials integration-both
run_gradle integration-both . --parallel \
  :pubnub-kotlin:pubnub-kotlin-impl:integrationTest \
  :pubnub-gson:pubnub-gson-impl:integrationTest
