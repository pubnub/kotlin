package com.pubnub.internal.crypto.cryptor

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import java.io.FilterInputStream
import java.io.InputStream
import java.security.GeneralSecurityException

/**
 * Wraps a decrypting [InputStream] (typically a [javax.crypto.CipherInputStream]) so that failures
 * surfacing lazily during stream consumption are masked behind a generic error.
 *
 * The cipher's `doFinal` runs only when the caller reads the stream to its end, i.e. after the
 * cryptor's own try/catch has already returned. Without this wrapper the underlying JCE exception
 * (e.g. `BadPaddingException` vs. `IllegalBlockSizeException`) would escape to the caller during
 * `read`, acting as a padding-oracle distinguisher. This wrapper converts any such failure into a
 * uniform [PubNubException] carrying [errorMessage], so the failure mode is not leaked.
 */
internal class DecryptingInputStream(
    delegate: InputStream,
    private val errorMessage: String,
) : FilterInputStream(delegate) {
    // Only the single-byte and (b, off, len) reads are overridden: FilterInputStream.read(b) routes
    // through read(b, 0, b.length), which dispatches back to the masked (b, off, len) override below.
    override fun read(): Int = masked { super.read() }

    override fun read(
        b: ByteArray,
        off: Int,
        len: Int,
    ): Int = masked { super.read(b, off, len) }

    override fun skip(n: Long): Long = masked { super.skip(n) }

    private inline fun <T> masked(block: () -> T): T {
        try {
            return block()
        } catch (e: PubNubException) {
            // The SDK's own errors are meaningful and already generic; pass them through unchanged.
            throw e
        } catch (e: Exception) {
            // CipherInputStream wraps doFinal failures (BadPadding/IllegalBlockSize) in an IOException;
            // surface them, and any other crypto/read failure, as a generic crypto error so the
            // underlying failure mode is not leaked.
            if (e is GeneralSecurityException || e.cause is GeneralSecurityException || e is java.io.IOException) {
                throw PubNubException(errorMessage = errorMessage, pubnubError = PubNubError.CRYPTO_ERROR)
            }
            throw e
        }
    }
}
