package com.pubnub.docs.configuration

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.docs.SnippetBase
import java.net.InetSocketAddress
import java.net.Proxy

class ConfigurationOther: SnippetBase() {

    private fun configurationMethod() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/configuration#methods

        // snippet.configurationMethod
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            // config options
        }
        val pubnub = PubNub.create(builder.build())
        // snippet.end
    }

    private fun configurationWithCryptoModule() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/configuration#cryptomodule-configuration

        // snippet.configurationWithCryptoModule
        val builderWithAesCbcCryptoModule = PNConfiguration.builder(UserId("abc"), "subscribeKey").apply {
            // encrypts using 256-bit AES-CBC cipher (recommended)
            // decrypts data encrypted with the legacy and the 256-bit AES-CBC ciphers
            cryptoModule = CryptoModule.createAesCbcCryptoModule("enigma")
        }

        // OR

        val builderWithLegacyCryptoModule = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            // encrypts with 128-bit cipher key entropy (legacy)
            // decrypts data encrypted with the legacy and the 256-bit AES-CBC ciphers
            cryptoModule = CryptoModule.createLegacyCryptoModule("enigma")
        }
        val pubnub = PubNub.create(builderWithAesCbcCryptoModule.build())
        // snippet.end
    }

    private fun valueOverride() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/configuration#value-override

        val pubNub = createPubNub()

        // snippet.valueOverride
        // publish
        pubNub.publish("myChannel", "mymessage").overrideConfiguration {
            publishKey = "overridePublishKey"
        }.sync()
        // snippet.end
    }

    private fun initializePubNub() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/configuration#methods-1
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/configuration#basic-usage-1

        // snippet.initializePubNub
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            publishKey = "publishKey"
        }

        val pubnub = PubNub.create(builder.build())
        // snippet.end
    }

    private fun initializeNonSecureClient(){
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/configuration#initialize-a-non-secure-client
        // snippet.initializeNonSecureClient
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            publishKey = "publishKey"
            secure = false
        }
        val pubnub = PubNub.create(builder.build())
        // snippet.end
    }

    private fun initializationReadOnlyClient() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/configuration#initialization-for-a-read-only-client
        // snippet.initializationReadOnlyClient
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey")
        val pubnub = PubNub.create(builder.build())
        // snippet.end
    }


    private fun initializeWithAccessManager() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/configuration#initializing-with-access-manager
        // snippet.initializeWithAccessManager
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            publishKey = "publishKey"
            secretKey = "SecretKey"
            secure = true
        }
        val pubnub = PubNub.create(builder.build())
        // snippet.end
    }

    private fun initializeWithProxy() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/configuration#how-to-set-proxy
        // snippet.initializeWithProxy
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            publishKey = "publishKey"
            secretKey = "SecretKey"
            proxy = Proxy(
                Proxy.Type.HTTP,
                InetSocketAddress("http://mydomain.com", 8080)
            )
        }
        val pubnub = PubNub.create(builder.build())
        // snippet.end
    }

    private fun configurationSetUserId(){
        // todo add link once created
        // snippet.configurationSetUserId
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey")
        val pubnub = PubNub.create(builder.build())
        // snippet.end

    }

    private fun configurationGetUserId(){
        // todo add link once created
        // snippet.configurationGetUserId
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey")
        val pubnub = PubNub.create(builder.build())

        val userId: UserId = pubnub.configuration.userId
        // snippet.end
    }

    private fun configurationSetFileterExpression() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/configuration#basic-usage-3

        // snippet.configurationSetFileterExpression
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            publishKey = "publishKey"
            filterExpression = "such=wow"
        }
        val pubnub = PubNub.create(builder.build())
        // snippet.end
    }

}
