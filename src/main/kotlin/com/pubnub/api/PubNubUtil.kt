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

    }
}