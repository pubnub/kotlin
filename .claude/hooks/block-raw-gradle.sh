#!/usr/bin/env bash
# PreToolUse hook (matcher: Bash). Blocks raw ./gradlew calls from the agent and points it to the
# wrapper scripts, whose compact output keeps Gradle noise out of the conversation.
# Exit 2 = block the tool call; stderr is shown to the agent.
# Scripts under scripts/dev/ call ./gradlew themselves; that is fine because this hook only sees the
# command the agent typed (e.g. "scripts/dev/check.sh lint"), not what the script runs.

cmd="$(jq -r '.tool_input.command // empty' 2>/dev/null)"
[ -z "$cmd" ] && exit 0

# "./gradlew" anywhere, or a bare "gradlew" at the start of a command segment.
if printf '%s' "$cmd" | grep -Eq '(\./gradlew)|((^|[;&|(])[[:space:]]*gradlew([[:space:]]|$))'; then
  cat >&2 <<'EOF'
Raw ./gradlew is blocked in this repo (its output floods the context). Use the wrappers:
  scripts/dev/check.sh lint | static | compile | unit [module] [--tests X]   (words can be combined: static compile)
  scripts/dev/integration-kotlin.sh | integration-gson.sh | integration-both.sh   (opt-in, need credentials)
They print a one-line PASS/FAIL plus the first failures; full output goes to build/agent-logs/.
EOF
  exit 2
fi
exit 0