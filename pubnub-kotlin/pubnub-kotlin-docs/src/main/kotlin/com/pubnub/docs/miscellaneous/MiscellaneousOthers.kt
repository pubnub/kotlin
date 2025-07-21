package com.pubnub.docs.miscellaneous

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.crypto.cryptor.Cryptor
import com.pubnub.api.crypto.data.EncryptedData
import com.pubnub.api.crypto.data.EncryptedStreamData
import com.pubnub.api.models.consumer.push.payload.PushPayloadHelper
import com.pubnub.api.utils.Instant
import com.pubnub.api.utils.TimetokenUtil
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.io.InputStream

class MiscellaneousOthers {
    private fun createPushPayloadMethod(pubNub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#methods

        // snippet.createPushPayloadMethod
        val pushPayloadHelper = PushPayloadHelper()

        val fcmPayload = PushPayloadHelper.FCMPayloadV2()
        val apnsPayload = PushPayloadHelper.APNSPayload()

        pushPayloadHelper.fcmPayloadV2 = fcmPayload
        pushPayloadHelper.apnsPayload = apnsPayload
        // snippet.end
    }

    private fun encryptString() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#encrypt-part-of-message

        // snippet.encryptString
        val aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = "myCipherKey01", randomIv = true)
        val stringToBeEncrypted = "string to be encrypted"
        val encryptedData = aesCbcCryptoModule.encrypt(stringToBeEncrypted.toByteArray())
        // snippet.end
    }

    private fun encryptInputStream() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-2

        // snippet.encryptInputStream
        val aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = "myCipherKey01", randomIv = true)
        val stringToBeEncrypted = "string to be encrypted"
        val encryptedStream = aesCbcCryptoModule.encryptStream(stringToBeEncrypted.byteInputStream())
        // snippet.end
    }

    private fun decryptString() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-3

        // snippet.decryptString
        val aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = "myCipherKey01", randomIv = true)
        val stringToBeEncrypted = "string to be encrypted"
        val encryptedData = aesCbcCryptoModule.encrypt(stringToBeEncrypted.toByteArray())
        val decryptedData = aesCbcCryptoModule.decrypt(encryptedData)
        // snippet.end
    }

    private fun decryptInputStream() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-4

        // snippet.decryptInputStream
        val aesCbcCryptoModule = CryptoModule.createAesCbcCryptoModule(cipherKey = "myCipherKey01", randomIv = true)
        val stringToBeEncrypted = "string to be encrypted"

        val encryptedStream = aesCbcCryptoModule.encryptStream(stringToBeEncrypted.byteInputStream())
        val decryptedStream = aesCbcCryptoModule.decryptStream(encryptedStream)
        // snippet.end
    }

    private fun destroy(pubNub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-5

        // snippet.destroy
        pubNub.destroy()
        // snippet.end
    }

    private fun getSubscribedChannelGroups(pubNub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-6

        // snippet.getSubscribedChannelGroups
        val subscribedChannelGroups = pubNub.getSubscribedChannelGroups()
        // snippet.end
    }

    private fun getSubscribedChannels(pubNub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-7

        // snippet.getSubscribedChannels
        val subscribedChannels = pubNub.getSubscribedChannels()
        // snippet.end
    }

    private fun disconnect(pubNub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-8

        // snippet.disconnect
        pubNub.disconnect()
        // snippet.end
    }

    private fun reconnect(pubNub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-9

        // snippet.reconnect
        pubNub.reconnect()
        // or
        val timetoken = 17276954606232118L // Example timetoken received in publish/signal response
        pubNub.reconnect(timetoken)
        // snippet.end
    }

    private fun timetokenToInstant() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-9

        // snippet.timetokenToInstant
        val timetoken: Long = 17276954606232118
        val instant: Instant = TimetokenUtil.timetokenToInstant(timetoken)
        val localDateTimeInUTC = instant.toLocalDateTime(TimeZone.UTC)

        println("PubNub timetoken: $timetoken")
        println("Current date: ${localDateTimeInUTC.date}")
        println("Current time: ${localDateTimeInUTC.time}")
        // snippet.end
    }

    private fun instantToTimetoken() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-10

        // snippet.instantToTimetoken
        val localDateTime = LocalDateTime(
            year = 2024,
            monthNumber = 9,
            dayOfMonth = 30,
            hour = 12,
            minute = 12,
            second = 44,
            nanosecond = 123456789
        )

        val zone = TimeZone.currentSystemDefault()
        val instant = localDateTime.toInstant(zone)
        val timetoken = TimetokenUtil.instantToTimetoken(instant)

        println("Current date: ${localDateTime.date}")
        println("Current time: ${localDateTime.time}")
        println("PubNub timetoken: $timetoken")
        // snippet.end
    }

    private fun unixToTimetoken() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-11

        // snippet.unixToTimetoken
        val unixTime = 1727866935316
        val timetoken: Long = TimetokenUtil.unixToTimetoken(unixTime)
        val instant: Instant = TimetokenUtil.timetokenToInstant(timetoken)
        val localDateTime = instant.toLocalDateTime(TimeZone.UTC)

        println("Current date: ${localDateTime.date}")
        println("Current time: ${localDateTime.time}")
        println("PubNub timetoken: $timetoken")
        // snippet.end
    }

    private fun timetokenToUnix() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#basic-usage-12

        // snippet.timetokenToUnix
        val timetoken = 17276954606232118
        val unixTime = TimetokenUtil.timetokenToUnix(timetoken)
        val instant = Instant.fromEpochMilliseconds(unixTime)
        val localDateTime = instant.toLocalDateTime(TimeZone.UTC)

        println("Current date: ${localDateTime.date}")
        println("Current time: ${localDateTime.time}")
        println("PubNub timetoken: $timetoken")
        // snippet.end
    }

    private fun createCryptoModuleBasic() {
        // https://www.pubnub.com/docs/general/setup/data-security#encrypting-messages

        // snippet.createCryptoModuleBasic
        val config = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
            cryptoModule = CryptoModule.createAesCbcCryptoModule("enigma")
        }.build()

        val pubnub = PubNub.create(config)
        // snippet.end
    }

    private fun customCryptor() {
        // https://www.pubnub.com/docs/general/setup/data-security#example-custom-cryptor-implementation

        // snippet.customCryptor
        fun myCustomCryptor() = object : Cryptor {
            override fun id(): ByteArray {
                // Should return a ByteArray of exactly 4 bytes.
                return byteArrayOf('C'.code.toByte(), 'U'.code.toByte(), 'S'.code.toByte(), 'T'.code.toByte())
            }

            override fun encrypt(data: ByteArray): EncryptedData {
                // implement your crypto logic
                return EncryptedData(metadata = null, data = data)
            }

            override fun decrypt(encryptedData: EncryptedData): ByteArray {
                // implement your crypto logic
                return encryptedData.data
            }

            override fun encryptStream(stream: InputStream): EncryptedStreamData {
                // implement your crypto logic
                return EncryptedStreamData(metadata = null, stream = stream)
            }

            override fun decryptStream(encryptedData: EncryptedStreamData): InputStream {
                // implement your crypto logic
                return encryptedData.stream
            }
        }
        // snippet.end
    }
}
