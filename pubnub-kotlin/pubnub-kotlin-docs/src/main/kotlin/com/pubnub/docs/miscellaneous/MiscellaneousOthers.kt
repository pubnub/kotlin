package com.pubnub.docs.miscellaneous

import com.pubnub.api.PubNub
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.models.consumer.push.payload.PushPayloadHelper
import com.pubnub.api.utils.Instant
import com.pubnub.api.utils.TimetokenUtil
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class MiscellaneousOthers {
    fun createPushPayloadMethod(pubNub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#methods

        // snippet.createPushPayloadMethod
        val pushPayloadHelper = PushPayloadHelper()

        val fcmPayload = PushPayloadHelper.FCMPayloadV2()
        val apnsPayload = PushPayloadHelper.APNSPayload()

        pushPayloadHelper.fcmPayloadV2 = fcmPayload
        pushPayloadHelper.apnsPayload = apnsPayload
        // snippet.end
    }

    fun encryptString() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#encrypt-part-of-message

        // snippet.encryptString
        val aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = "myCipherKey01", randomIv = true)
        val stringToBeEncrypted = "string to be encrypted"
        val encryptedData = aesCbcCryptoModule.encrypt(stringToBeEncrypted.toByteArray())
        // snippet.end
    }

    fun encryptInputStream() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-2

        // snippet.encryptInputStream
        val aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = "myCipherKey01", randomIv = true)
        val stringToBeEncrypted = "string to be encrypted"
        val encryptedStream = aesCbcCryptoModule.encryptStream(stringToBeEncrypted.byteInputStream())
        // snippet.end
    }

    fun decryptString() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-3

        // snippet.decryptString
        val aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = "myCipherKey01", randomIv = true)
        val stringToBeEncrypted = "string to be encrypted"
        val encryptedData = aesCbcCryptoModule.encrypt(stringToBeEncrypted.toByteArray())
        val decryptedData = aesCbcCryptoModule.decrypt(encryptedData)
        // snippet.end
    }

    fun decryptInputStream() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-4

        // snippet.decryptInputStream
        val aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = "myCipherKey01", randomIv = true)
        val stringToBeEncrypted = "string to be encrypted"

        val encryptedStream = aesCbcCryptoModule.encryptStream(stringToBeEncrypted.byteInputStream())
        val decryptedStream = aesCbcCryptoModule.decryptStream(encryptedStream)
        // snippet.end
    }

    fun destroy(pubNub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-5

        // snippet.destroy
        pubNub.destroy()
        // snippet.end
    }

    fun getSubscribedChannels(pubNub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-6

        // snippet.getSubscribedChannels
        val subscribedChannels = pubNub.getSubscribedChannels()
        // snippet.end
    }

    fun disconnect(pubNub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-7

        // snippet.disconnect
        pubNub.disconnect()
        // snippet.end
    }

    fun reconnect(pubNub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-8

        // snippet.reconnect
        pubNub.reconnect()
        // snippet.end
    }

    fun timetokenToInstant() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-9

        // snippet.timetokenToInstant
        val timetoken: Long = 17276954606232118
        val instant: Instant = TimetokenUtil.timetokenToInstant(timetoken)
        val localDateTimeInUTC = instant.toLocalDateTime(TimeZone.UTC)

        println("PubNub timetoken: ${timetoken}")
        println("Current date: ${localDateTimeInUTC.date}")
        println("Current time: ${localDateTimeInUTC.time}")
        // snippet.end
    }

    fun instantToTimetoken() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-10

        // snippet.instantToTimetoken
        val localDateTime = LocalDateTime(year = 2024, monthNumber = 9, dayOfMonth = 30, hour = 12, minute = 12, second =  44, nanosecond =  123456789)
        val zone = TimeZone.currentSystemDefault()
        val instant = localDateTime.toInstant(zone)
        val timetoken = TimetokenUtil.instantToTimetoken(instant)

        println("Current date: ${localDateTime.date}")
        println("Current time: ${localDateTime.time}")
        println("PubNub timetoken: ${timetoken}")
        // snippet.end
    }

    fun unixToTimetoken() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-11

        // snippet.unixToTimetoken
        val unixTime = 1727866935316
        val timetoken: Long = TimetokenUtil.unixToTimetoken(unixTime)
        val instant: Instant = TimetokenUtil.timetokenToInstant(timetoken)
        val localDateTime = instant.toLocalDateTime(TimeZone.UTC)

        println("Current date: ${localDateTime.date}")
        println("Current time: ${localDateTime.time}")
        println("PubNub timetoken: ${timetoken}")
        // snippet.end
    }

    fun timetokenToUnix() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-12

        // snippet.timetokenToUnix
        val timetoken = 17276954606232118
        val unixTime = TimetokenUtil.timetokenToUnix(timetoken)
        val instant = Instant.fromEpochMilliseconds(unixTime)
        val localDateTime = instant.toLocalDateTime(TimeZone.UTC)

        println("Current date: ${localDateTime.date}")
        println("Current time: ${localDateTime.time}")
        println("PubNub timetoken: ${timetoken}")
        // snippet.end
    }
}
