package com.pubnub.api

import com.pubnub.api.vendor.Base64
import com.pubnub.api.vendor.Crypto
import okhttp3.Request
import okio.Buffer
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.TreeSet
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

internal object PubNubUtil {

    private val log = LoggerFactory.getLogger("PubNubUtil")

    private const val CHARSET = "UTF-8"

    fun replaceLast(string: String, toReplace: String, replacement: String): String {
        val pos = string.lastIndexOf(toReplace)
        return if (pos > -1) {
            string.substring(0, pos) + replacement + string.substring(
                pos + toReplace.length,
                string.length
            )
        } else {
            string
        }
    }

    /**
     * Returns decoded String
     *
     * @param stringToEncode , input string
     * @return , decoded string
     */
    fun urlDecode(stringToEncode: String?): String? {
        return try {
            URLDecoder.decode(stringToEncode, CHARSET)
        } catch (e: UnsupportedEncodingException) {
            null
        }
    }

    fun signRequest(
        originalRequest: Request,
        pnConfiguration: PNConfiguration,
        timestamp: Int
    ): Request {
        // only sign if we have a secret key in place.
        if (!pnConfiguration.isSecretKeyValid()) {
            return originalRequest
        }
        val signature = generateSignature(pnConfiguration, originalRequest, timestamp)
        val rebuiltUrl = originalRequest.url().newBuilder()
            .addQueryParameter("timestamp", timestamp.toString())
            .addQueryParameter("signature", signature)
            .build()
        return originalRequest.newBuilder().url(rebuiltUrl).build()
    }

    internal fun signSHA256(key: String, data: String): String {
        val sha256HMAC: Mac
        val hmacData: ByteArray
        val secretKey = SecretKeySpec(key.toByteArray(charset(CHARSET)), "HmacSHA256")
        sha256HMAC = try {
            Mac.getInstance("HmacSHA256")
        } catch (e: NoSuchAlgorithmException) {
            throw Crypto.newCryptoError(0, e)
        }
        try {
            sha256HMAC.init(secretKey)
        } catch (e: InvalidKeyException) {
            throw Crypto.newCryptoError(0, e)
        }
        hmacData = sha256HMAC.doFinal(data.toByteArray(charset(CHARSET)))
        val signed = String(Base64.encode(hmacData, 0), Charset.forName(CHARSET))
            .replace('+', '-')
            .replace('/', '_')
            .replace("\n", "")
        return signed
    }

    private fun generateSignature(
        configuration: PNConfiguration,
        request: Request,
        timestamp: Int
    ): String? {
        val isV2Signature: Boolean
        val signatureBuilder = StringBuilder()
        val requestURL = request.url().encodedPath()
        val queryParams = mutableMapOf<String, String>()
        for (queryKey in request.url().queryParameterNames()) {
            queryParams[queryKey] = request.url().queryParameter(queryKey)!!
            // queryParams[queryKey] = request.url().encoded
        }
        queryParams["timestamp"] = timestamp.toString()

        // todo AB testing
        val classic = true
        val encodedQueryString = if (classic) {
            preparePamArguments(queryParams)
        } else {
            preparePamArguments("${request.url().encodedQuery()}&timestamp=$timestamp")
        }

        isV2Signature = !(requestURL.startsWith("/publish") && request.method().equals("post", ignoreCase = true))
        if (!isV2Signature) {
            signatureBuilder.append(configuration.subscribeKey).append("\n")
            signatureBuilder.append(configuration.publishKey).append("\n")
            signatureBuilder.append(requestURL).append("\n")
            signatureBuilder.append(encodedQueryString)
        } else {
            signatureBuilder.append(request.method().toUpperCase()).append("\n")
            signatureBuilder.append(configuration.publishKey).append("\n")
            signatureBuilder.append(requestURL).append("\n")
            signatureBuilder.append(encodedQueryString).append("\n")
            signatureBuilder.append(requestBodyToString(request))
        }

        var signature = ""
        try {
            signature = signSHA256(configuration.secretKey, signatureBuilder.toString())
            if (isV2Signature) {
                signature = removeTrailingEqualSigns(signature)
                signature = "v2.$signature"
            }
        } catch (e: PubNubException) {
            log.warn("signature failed on SignatureInterceptor: $e")
        } catch (e: UnsupportedEncodingException) {
            log.warn("signature failed on SignatureInterceptor: $e")
        }
        return signature
    }

    fun removeTrailingEqualSigns(signature: String): String {
        var cleanSignature = signature
        while (cleanSignature[cleanSignature.length - 1] == '=') {
            cleanSignature = cleanSignature.substring(0, cleanSignature.length - 1)
        }
        return cleanSignature
    }

    internal fun requestBodyToString(request: Request): String? {
        if (request.body() == null) {
            return ""
        }
        try {
            val buffer = Buffer()
            request.body()!!.writeTo(buffer)
            return buffer.readUtf8()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

    internal fun preparePamArguments(pamArgs: Map<String, String>): String {
        val pamKeys: Set<String?> = TreeSet(pamArgs.keys)
        var stringifiedArguments = ""
        var i = 0
        for (pamKey in pamKeys) {
            if (i != 0) {
                stringifiedArguments = "$stringifiedArguments&"
            }
            stringifiedArguments = stringifiedArguments + pamKey + "=" + pamEncode(pamArgs[pamKey]!!)
            i += 1
        }
        return stringifiedArguments
    }

    private fun preparePamArguments(encodedQueryString: String): String {
        return encodedQueryString.split("&")
            .toSortedSet()
            .map { pamEncode(it, true) }
            .joinToString("&")
    }

    /**
     * Returns encoded String
     *
     * @param stringToEncode , input string
     * @return , encoded string
     */
    internal fun pamEncode(stringToEncode: String, alreadyPercentEncoded: Boolean = false): String {
        /* !'()*~ */

        return if (alreadyPercentEncoded) {
            stringToEncode
        } else {
            URLEncoder.encode(stringToEncode, "UTF-8")
                .replace("+", "%20")
        }.run {
            replace("*", "%2A")
        }
    }
}

internal fun <E> List<E>.toCsv(): String {
    if (this.isNotEmpty())
        return this.joinToString(",")
    return ","
}
