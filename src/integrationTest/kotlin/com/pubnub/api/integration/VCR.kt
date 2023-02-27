package com.pubnub.api.integration

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.FileSource
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.extension.Parameters
import com.github.tomakehurst.wiremock.extension.StubMappingTransformer
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import com.pubnub.api.CommonUtils
import com.pubnub.api.Keys
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionConfigurationException
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolutionException
import org.junit.jupiter.api.extension.ParameterResolver
import org.junit.jupiter.api.extension.RegisterExtension
import org.slf4j.LoggerFactory
import java.lang.reflect.Constructor
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID
import kotlin.random.Random

// VCR inspired
interface Betacam {
    fun nonRandomString(): String
}

class MyStubMappingTransformer : StubMappingTransformer() {
    override fun getName(): String = "MyStubMappingTransformer"

    // "e38a5f0e-3696-44e6-ac4a-41cabadfe088"
    private val transformed = mutableSetOf<UUID>()

    override fun transform(stubMapping: StubMapping, files: FileSource, parameters: Parameters): StubMapping {
        if (transformed.contains(stubMapping.id)) {
            return stubMapping
        }
        transformed.add(stubMapping.id)
        val (path, query) = stubMapping.request.url.split("?")

        val requestPattern = RequestPatternBuilder.newRequestPattern(
            stubMapping.request.method,
            WireMock.urlPathMatching(path)
        )

        // query.split("&")
        //     .map { it.split("=").let { (k, v) -> k to v } }
        //     .filter { (k, _) -> !setOf("uuid", "pnsdk", "requestid").contains(k) }
        //     .forEach { (k, v) ->
        //         requestPattern.withQueryParam(k, EqualToPattern(v))
        //     }

        // requestPattern.withQueryParam()
        stubMapping.request = requestPattern.build()

        return StubMapping(requestPattern.build(), stubMapping.response).apply {
            this.priority = 1
        }
    }
}

annotation class BetacamOrigin

class PubNubProvider

annotation class PreSeededRandom

class RandomExtension : BeforeEachCallback, ParameterResolver {
    lateinit var random: Random
    override fun beforeEach(context: ExtensionContext) {
        val clazzName = context.testClass.get().simpleName
        val method = context.testMethod
        random = Random(clazzName.hashCode() + method.hashCode())
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        val annotated = parameterContext.isAnnotated(BetacamOrigin::class.java)
        if (annotated && parameterContext.declaringExecutable is Constructor<*>) {
            throw ParameterResolutionException(
                "@PreSeededRandom is not supported on constructor parameters. Please use field injection instead."
            )
        }
        return annotated
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any? {
        val parameterType = parameterContext.parameter.type
        assertSupportedType("parameter", parameterType)
        return random
    }

    private fun assertSupportedType(target: String, type: Class<*>) {
        if (type != Random::class.java) {
            throw ExtensionConfigurationException(
                "Can only resolve @PreSeededRandom " + target + " of type " +
                    Random::class.java.name + " but was: " + type.name
            )
        }
    }
}

val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun Random.randomAlphanumericString(length: Int): String {
    return (1..length)
        .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
        .joinToString("")
}

@ExtendWith(RandomExtension::class)
class Something {

    protected val logger = LoggerFactory.getLogger(this.javaClass.simpleName)
    private var clients = mutableListOf<PubNub>()
    lateinit var random: Random

    companion object {
        lateinit var pubNub: PubNub
        lateinit var server: PubNub

        @RegisterExtension
        val betacamExtension = BetacamExtension()
    }

    @BeforeEach
    fun beforeEachSomething(@PreSeededRandom random: Random) {
        this.random = random
    }

    fun randomValue() = random.randomAlphanumericString(10)

    private fun createPubNub(): PubNub {
        val pnConfiguration = getClientPnConfiguration(randomValue())
        val pubNub = PubNub(pnConfiguration)
        clients.add(pubNub)
        return pubNub
    }

    private fun createServer(): PubNub {
        val pubNub = PubNub(getServerPnConfiguration(randomValue()))
        clients.add(pubNub)
        return pubNub
    }

    private fun getBasicPnConfiguration(): PNConfiguration {
        val pnConfiguration = PNConfiguration(userId = UserId(PubNub.generateUUID()))
        pnConfiguration.logVerbosity = PNLogVerbosity.NONE
        pnConfiguration.httpLoggingInterceptor = CommonUtils.createInterceptor(logger)
        return pnConfiguration
    }

    private fun getClientPnConfiguration(postfix: String) = getBasicPnConfiguration().apply {
        subscribeKey = Keys.subKey
        publishKey = Keys.pubKey
        userId = UserId("client-$postfix")
    }

    private fun getServerPnConfiguration(postfix: String): PNConfiguration = getBasicPnConfiguration().apply {
        subscribeKey = Keys.pamSubKey
        publishKey = Keys.pamPubKey
        secretKey = Keys.pamSecKey
        userId = UserId("server-$postfix")
    }

    private fun destroyClient(client: PubNub) {
        client.unsubscribeAll()
        client.forceDestroy()
    }
}

class BetacamExtension : BeforeAllCallback, AfterAllCallback, ParameterResolver {
    lateinit var wm: WireMockServer

    override fun beforeAll(context: ExtensionContext) {
        val clazzName = context.testClass.get().simpleName
        Files.createDirectories(Paths.get("src/integrationTest/resources/$clazzName/mappings"))
        wm = WireMockServer(
            WireMockConfiguration.wireMockConfig().dynamicPort()
                .extensions(MyStubMappingTransformer::class.java)
                .usingFilesUnderDirectory("src/integrationTest/resources/$clazzName")
        )
        wm.start()
        wm.startRecording(
            WireMock.recordSpec()
                .forTarget("http://ps.pndsn.com")
                .transformers("MyStubMappingTransformer")
                .transformerParameters(Parameters.one("Aaa", "bbb"))
        )
    }

    override fun afterAll(context: ExtensionContext?) {
        wm.stopRecording()
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        val annotated = parameterContext.isAnnotated(BetacamOrigin::class.java)
        parameterContext.declaringExecutable
        if (annotated && parameterContext.declaringExecutable is Constructor<*>) {
            throw ParameterResolutionException(
                "@BetacamOrigin is not supported on constructor parameters. Please use field injection instead."
            )
        }
        return annotated
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any? {
        val parameterType = parameterContext.parameter.type
        assertSupportedType("parameter", parameterType)
        return wm.baseUrl().removePrefix("http://")
    }

    private fun assertSupportedType(target: String, type: Class<*>) {
        if (type != String::class.java) {
            throw ExtensionConfigurationException(
                "Can only resolve @BetacamOrigin " + target + " of type " +
                    String::class.java.name + " but was: " + type.name
            )
        }
    }
}
