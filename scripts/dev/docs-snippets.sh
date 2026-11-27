#!/usr/bin/env bash
# Live docs snippet tests (Kotlin and Java, package com.pubnub.docs). Compact result, full Gradle output in build/agent-logs/.
# The live runs hit real PubNub and need the docs keysets, see the README in
# pubnub-kotlin/pubnub-kotlin-impl/src/integrationTest/kotlin/com/pubnub/docs/. The script checks env NAMES only (or the
# env file named by LIVE_ENV_FILE) and never prints a key value.
#
# Usage:
#   scripts/dev/docs-snippets.sh lint [--tests X]           # compile both integrationTest source sets, run the offline tools (no keys, no network). X narrows to one class
#   scripts/dev/docs-snippets.sh run [group] [--sdk S] [--tests X]  # live tests of one group (LIVE_GROUP) or all. S: kotlin, java or both (default, in parallel)
#   scripts/dev/docs-snippets.sh janitor [--sdk S]          # clean the listed names, revoke the application level grant, wait until it is closed
#   scripts/dev/docs-snippets.sh preflight [--sdk S]        # read the keyset features through the Admin API and probe the open keyset
#
# Kotlin tests use the Kotlin keysets (PUBLISH_KEY and so on), Java tests the Java keysets (JAVA_PUBLISH_KEY and so on).
#   scripts/dev/docs-snippets.sh docs-check [docs checkout] # classify every docs EmbeddedCode (report only), checkout from the argument or DOCS_REPO
#
# LIVE_MANIFEST_WRITE=1 scripts/dev/docs-snippets.sh lint regenerates coverage.json.
set -uo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/_lib.sh"

TIER_DIR="$REPO_ROOT/pubnub-kotlin/pubnub-kotlin-impl/src/integrationTest/kotlin/com/pubnub/docs"
KOTLIN=":pubnub-kotlin:pubnub-kotlin-impl"
GSON=":pubnub-gson:pubnub-gson-impl"

# Fails fast, without printing any secret, when no docs keys can be found. Looks at names and at the file path only.
require_docs_credentials() {
  local file="${LIVE_ENV_FILE:-$TIER_DIR/.env.local}"
  if [ -f "$file" ]; then return 0; fi
  if [ -n "${PUBLISH_KEY:-}" ] && [ -n "${SUBSCRIBE_KEY:-}" ]; then return 0; fi
  echo "FAIL $1: no docs keys. Copy $TIER_DIR/.env.example to .env.local there and fill it in, or set LIVE_ENV_FILE, or export PUBLISH_KEY, SUBSCRIBE_KEY and the other names of the .env.example."
  exit 2
}

# One live run per SDK keyset set per machine at a time (run, janitor, preflight), across checkouts and worktrees: the
# tier uses fixed names on shared keysets. Kotlin tests use the plain env names, Java tests the JAVA_ ones (their own
# keysets), so a Kotlin and a Java run may overlap. The Gradle build service locks cover one build only. A lock whose
# owner process is gone is taken over. LIVE_LOCK_WAIT_MIN (default 120) is how long to wait for another run.
# A fixed path, not $TMPDIR: every session and worktree on the machine must see the same lock.
HELD_LOCKS=()
release_live_locks() {
  local lock
  # bash 3.2 with set -u fails on an empty array, so expand only when it has items
  for lock in ${HELD_LOCKS[@]+"${HELD_LOCKS[@]}"}; do rm -rf "$lock"; done
}
trap release_live_locks EXIT
take_live_lock() { # <command name> <sdk>
  # The kotlin lock keeps the path of the earlier single lock, which also guarded the Kotlin keysets.
  local lock="/tmp/pubnub-kotlin-docs-snippets-live.lock" waited=0 limit=$(( ${LIVE_LOCK_WAIT_MIN:-120} * 60 )) owner
  if [ "$2" != kotlin ]; then lock="/tmp/pubnub-kotlin-docs-snippets-live-$2.lock"; fi
  until mkdir "$lock" 2>/dev/null; do
    owner="$(cat "$lock/pid" 2>/dev/null || true)"
    if [ -n "$owner" ] && ! kill -0 "$owner" 2>/dev/null; then
      rm -rf "$lock"
      continue
    fi
    if [ "$waited" -eq 0 ]; then echo "waiting for another live $2 docs snippet run (pid ${owner:-unknown}, $(cat "$lock/what" 2>/dev/null))"; fi
    if [ "$waited" -ge "$limit" ]; then echo "FAIL $1: another live run still holds $lock after ${LIVE_LOCK_WAIT_MIN:-120} min"; exit 2; fi
    sleep 10
    waited=$((waited + 10))
  done
  echo $$ > "$lock/pid"
  echo "$1 in $REPO_ROOT" > "$lock/what"
  HELD_LOCKS+=("$lock")
}
# Takes the locks of the selected SDKs, always kotlin before java, so two runs can never wait on each other.
take_live_locks() { # <command name>
  case "$SDK" in
    kotlin|java) take_live_lock "$1" "$SDK" ;;
    both) take_live_lock "$1" kotlin; take_live_lock "$1" java ;;
  esac
}

# --sdk kotlin|java|both (default both) may come first after the command.
SDK=both
parse_sdk() {
  if [ "${1:-}" = "--sdk" ]; then
    case "${2:-}" in
      kotlin|java|both) SDK="$2" ;;
      *) echo "FAIL docs-snippets: --sdk must be kotlin, java or both"; exit 64 ;;
    esac
    return 2
  fi
  return 0
}
# Fills TASKS with the Gradle tasks of the selected SDKs, each followed by the given args (bash 3.2: no mapfile).
TASKS=()
sdk_tasks() { # <extra args for each task...>
  TASKS=()
  if [ "$SDK" != java ]; then TASKS+=("$KOTLIN:docsSnippetsTest" "$@"); fi
  if [ "$SDK" != kotlin ]; then TASKS+=("$GSON:docsSnippetsTest" "$@"); fi
}
# Only the janitor and preflight commands below set it, never the caller's shell.
unset DOCS_SNIPPETS_TOOL_SDK

# Prints the lines the tools wrote (never a key value) from the Gradle log.
show_tool_lines() {
  grep -h -E '(janitor|preflight|docs-check|live roles|equivalence|extractor|groups|manifest|marker lint|lint self test|lint):' "$LOG_DIR/$1.log" 2>/dev/null | sed -E 's/^[[:space:]]+//' | awk '!seen[$0]++'
}

sub="${1:-}"
[ $# -gt 0 ] && shift

case "$sub" in
  lint)
    # lint [--tests X]: all offline checks, or only the tests that match X (one class, for a granular run).
    unset LIVE_GROUP
    lint_tests='com.pubnub.docs.offline.*'
    if [ "${1:-}" = "--tests" ] && [ -n "${2:-}" ]; then lint_tests="$2"; fi
    run_gradle docs-snippets-lint . \
      "$KOTLIN:compileIntegrationTestKotlin" "$GSON:compileIntegrationTestJava" \
      "$KOTLIN:docsSnippetsTest" --tests "$lint_tests" \
      "$GSON:docsSnippetsTest" --tests "$lint_tests"
    rc=$?
    show_tool_lines docs-snippets-lint
    exit $rc
    ;;
  run)
    # run [group] [--sdk kotlin|java|both] [--tests X]. Both SDKs run in parallel in one Gradle build (own keysets).
    require_docs_credentials docs-snippets-run
    unset DOCS_SNIPPETS_TOOL_SDK
    group="" tests=""
    while [ $# -gt 0 ]; do
      case "$1" in
        --sdk) parse_sdk "$1" "${2:-}"; shift 2 ;;
        --tests) tests="${2:-}"; shift 2 ;;
        --*) break ;;
        *) [ -z "$group" ] && group="$1"; shift ;;
      esac
    done
    take_live_locks docs-snippets-run
    if [ -n "$group" ]; then export LIVE_GROUP="$group"; else unset LIVE_GROUP; fi
    name="docs-snippets-run${group:+-$group}-$SDK"
    if [ -n "$tests" ]; then sdk_tasks --tests "$tests"; else sdk_tasks; fi
    run_gradle "$name" . "${TASKS[@]}" "$@"
    rc=$?
    show_tool_lines "$name"
    exit $rc
    ;;
  janitor|preflight)
    # janitor|preflight [--sdk kotlin|java|both]: the tool test runs in the Kotlin module, once per selected SDK.
    cmd="$sub"
    require_docs_credentials "docs-snippets-$cmd"
    parse_sdk "${1:-}" "${2:-}"
    take_live_locks "docs-snippets-$cmd"
    unset LIVE_GROUP
    if [ "$cmd" = janitor ]; then tool='com.pubnub.docs.support.tools.JanitorCommandTest'; else tool='com.pubnub.docs.support.tools.PreflightCommandTest'; fi
    rc=0
    for one in kotlin java; do
      [ "$SDK" != both ] && [ "$SDK" != "$one" ] && continue
      export DOCS_SNIPPETS_TOOL_SDK="$one"
      run_gradle "docs-snippets-$cmd-$one" . "$KOTLIN:docsSnippetsTest" --tests "$tool" || rc=1
      show_tool_lines "docs-snippets-$cmd-$one"
    done
    unset DOCS_SNIPPETS_TOOL_SDK
    exit $rc
    ;;
  docs-check)
    unset LIVE_GROUP
    export DOCS_REPO="${1:-${DOCS_REPO:-}}"
    if [ -z "$DOCS_REPO" ] || [ ! -d "$DOCS_REPO/docs/sdks" ]; then echo "FAIL docs-snippets-docs-check: no docs checkout (pass the path as the argument or set DOCS_REPO)"; exit 2; fi
    run_gradle docs-snippets-docs-check . "$KOTLIN:docsSnippetsTest" --tests 'com.pubnub.docs.offline.DocsToolsTest.docsCheckReportOnlyAgainstTheDocsCheckout'
    rc=$?
    show_tool_lines docs-snippets-docs-check
    exit $rc
    ;;
  *)
    sed -n '2,15p' "$0"
    exit 64
    ;;
esac
