#!/usr/bin/env bash
# Shared helpers for scripts/dev/*.sh. Sourced, not executed.
#
# Goal: run Gradle but keep the full output OUT of the caller's context (an AI agent or a human
# terminal). The full output goes to a log file; stdout gets a few lines:
#   PASS <name> (Ns, N tests) log=<path>
#   FAIL <name> (Ns, exit=N) log=<path>   + failed tests / first errors, capped.

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
LOG_DIR="$REPO_ROOT/build/agent-logs"
MAX_FAILED_TESTS="${MAX_FAILED_TESTS:-15}"
MAX_ERROR_LINES="${MAX_ERROR_LINES:-25}"

# Gradle needs Java 21 to drive the build (see CLAUDE.md). Pick it up on macOS if JAVA_HOME is unset.
ensure_java() {
  if [ -z "${JAVA_HOME:-}" ] && [ -x /usr/libexec/java_home ]; then
    local jh
    jh="$(/usr/libexec/java_home -v 21 2>/dev/null)" && export JAVA_HOME="$jh"
  fi
}

# Integration tests hit real PubNub; fail fast (without printing any secret) if credentials are missing.
require_credentials() {
  if [ -f "$REPO_ROOT/test.properties" ]; then return 0; fi
  # The integration suites read these exact names (OWNER keys in Keys.kt / ITTestConfig.java) via System.getenv().
  if [ -n "${pubKey:-}" ] && [ -n "${subKey:-}" ]; then return 0; fi
  echo "FAIL $1: no credentials. Create test.properties (copy test-example.properties) or export pubKey/subKey/pamPubKey/pamSubKey/pamSecKey env vars."
  exit 2
}

# run_gradle <name> <search-dir-relative-to-repo-root> <gradle args...>
run_gradle() {
  local name="$1" search_dir="$2"
  shift 2
  mkdir -p "$LOG_DIR"
  local log="$LOG_DIR/$name.log" marker="$LOG_DIR/.$name.start"
  : > "$log"
  touch "$marker"
  ensure_java

  local start end rc
  start=$(date +%s)
  (cd "$REPO_ROOT" && ./gradlew --console=plain --continue "$@") > "$log" 2>&1
  rc=$?
  end=$(date +%s)

  # JUnit XML written during THIS run, restricted to the modules this script owns.
  local xmls
  xmls="$(find "$REPO_ROOT/$search_dir" -path '*/build/test-results/*' -name 'TEST-*.xml' -newer "$marker" 2>/dev/null)"
  local total=0 failed=0
  if [ -n "$xmls" ]; then
    total=$(echo "$xmls" | xargs grep -h -o '<testcase ' 2>/dev/null | wc -l | tr -d ' ')
    failed=$(echo "$xmls" | xargs grep -h -E -o '<(failure|error) ' 2>/dev/null | wc -l | tr -d ' ')
  fi

  local secs=$((end - start))
  if [ "$rc" -eq 0 ] && [ "$failed" -eq 0 ]; then
    echo "PASS $name (${secs}s, ${total} tests) log=$log"
    return 0
  fi

  echo "FAIL $name (${secs}s, exit=$rc, ${total} tests, ${failed} failed) log=$log"

  if [ "$failed" -gt 0 ]; then
    echo "-- failed tests (max $MAX_FAILED_TESTS) --"
    echo "$xmls" | xargs awk '
      /<testcase / {
        cls = ""; nm = ""
        if (match($0, /classname="[^"]*"/)) cls = substr($0, RSTART + 11, RLENGTH - 12)
        if (match($0, / name="[^"]*"/))     nm  = substr($0, RSTART + 7,  RLENGTH - 8)
      }
      /<(failure|error) / {
        msg = ""
        if (match($0, /message="[^"]*"/)) msg = substr($0, RSTART + 9, RLENGTH - 10)
        print cls "." nm ": " substr(msg, 1, 200)
      }' 2>/dev/null | head -n "$MAX_FAILED_TESTS"
  fi

  # Build/compile/lint errors: Kotlin "e: file.kt:..." lines, Checkstyle/detekt/ktlint "file.kt:L:C" lines,
  # and Gradle's "What went wrong" block. Skip anything that is only a test-failure restatement.
  echo "-- errors (max $MAX_ERROR_LINES lines) --"
  {
    grep -E '^e: |\.(kt|java):[0-9]+(:[0-9]+)?' "$log" | grep -v '^\s*at '
    awk '/^\* What went wrong:/ {c=6} c>0 {print; c--}' "$log"
  } | awk '!seen[$0]++' | head -n "$MAX_ERROR_LINES"
  echo "(full output: $log)"
  return 1
}
