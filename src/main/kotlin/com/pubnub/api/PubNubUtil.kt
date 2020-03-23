package com.pubnub.api

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class PubNubUtil {

    companion object A {

        private const val CHARSET = "UTF-8"

        /**
         * Returns encoded String
         *
         * @param stringToEncode , input string
         * @return , encoded string
         */
        fun urlEncode(stringToEncode: String): String {
            return try {
                URLEncoder.encode(stringToEncode, CHARSET).replace("+", "%20")
            } catch (e: UnsupportedEncodingException) {
                ""
            }

        }

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
    }
}

internal fun <E> List<E>.toCsv(): String {
    if (this.isNotEmpty())
        return this.joinToString(",")
    return ","
}