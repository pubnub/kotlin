#!/usr/bin/env bash
# Non-integration checks with a compact result. Full Gradle output goes to build/agent-logs/<name>.log.
#
# Usage:
#   scripts/dev/check.sh lint                         # ktlintCheck, all modules
#   scripts/dev/check.sh static                       # ktlintCheck detekt checkstyleMain spotbugsMain (as in CI)
#   scripts/dev/check.sh static compile               # lint/static/compile/format can be combined in one run
#   scripts/dev/check.sh compile                      # main + test sources, all modules
#   scripts/dev/check.sh unit                         # all unit tests
#   scripts/dev/check.sh unit <module> [--tests X]    # e.g. :pubnub-kotlin:pubnub-kotlin-impl --tests "com.pubnub.foo.BarTest"
#   scripts/dev/check.sh format                       # ktlintFormat (WRITES files; run on request only)
#
# Integration tests are intentionally NOT here; see integration-kotlin.sh / integration-gson.sh.
set -uo pipefail
source "$(dirname "${BASH_SOURCE[0]}")/_lib.sh"

if [ "${1:-}" = "unit" ]; then
  shift
  if [ $# -eq 0 ]; then
    run_gradle unit . test
  else
    module="$1"; shift
    run_gradle unit . "$module:test" "$@"
  fi
  exit $?
fi

# lint / static / compile / format can be combined in one Gradle run, e.g. `check.sh static compile`.
tasks=()
name=""
for word in "$@"; do
  case "$word" in
    lint)    tasks+=(ktlintCheck) ;;
    static)  tasks+=(ktlintCheck detekt checkstyleMain spotbugsMain) ;;  # same set as CI run-validations.yml
    compile) tasks+=(compileKotlin compileKotlinJvm compileJava compileTestKotlin compileTestKotlinJvm compileTestJava) ;;
    format)  tasks+=(ktlintFormat) ;;
    *) sed -n '2,13p' "$0"; exit 64 ;;
  esac
  name="${name:+$name+}$word"
done
[ ${#tasks[@]} -eq 0 ] && { sed -n '2,13p' "$0"; exit 64; }

run_gradle "$name" . "${tasks[@]}"
