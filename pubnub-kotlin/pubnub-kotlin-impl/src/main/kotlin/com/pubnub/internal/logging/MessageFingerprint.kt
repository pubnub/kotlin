package com.pubnub.internal.logging

// Server-parity message fingerprint. Mirrors the server's `message_fingerprint`.
//
// The input string MUST be derived by the caller using the same rule (see [getMessageFingerprintInput]):
// raw String / string-typed JsonPrimitive values are passed unquoted; everything else is canonical
// JSON. Encrypted publishes pass the ciphertext String unquoted.
//
// Algorithm notes — Java `String.hashCode()` accumulates `h = 31*h + charAt(i)` as a signed `int`,
// which is bit-identical to the server's `Uint32Array(1)` accumulation modulo 2^32. The
// `& 0xFFFFFFFFL` mask reinterprets the signed result as unsigned for the 8-char hex format.
//
// Both sides walk UTF-16 code units (`charCodeAt` / `charAt`), so surrogate pairs are hashed as
// their two halves. Do not migrate to code-point or byte iteration without bumping a fingerprint
// version and updating the server reference in lockstep.
internal fun pnMfp(input: String): String = "%08x".format(input.hashCode().toLong() and 0xFFFFFFFFL)
