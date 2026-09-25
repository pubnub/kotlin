package com.pubnub.internal.workers

import com.google.gson.Gson
import com.pubnub.api.UserId
import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncUserEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncUserEventMessage
import com.pubnub.api.models.consumer.pubsub.datasync.PNUnknownDataSyncEventMessage
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.managers.DuplicationManager
import com.pubnub.internal.models.server.SubscribeMessage
import com.pubnub.internal.v2.PNConfigurationImpl
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.nullValue
import org.junit.Test
import org.hamcrest.core.Is.`is` as iz

/**
 * Dispatch tests for the DataSync (`e=5`) branch of [SubscribeMessageProcessor]: correct leaf typing,
 * forward-compat unknown leaf, and per-message hardening (a malformed payload degrades to one dropped
 * event, not a dropped batch).
 */
class SubscribeMessageProcessorDataSyncTest {
    private val gson = Gson()

    private fun config(): PNConfiguration =
        PNConfigurationImpl.Builder(userId = UserId("test"), "").build()

    private fun messageProcessor(
        configuration: PNConfiguration = config(),
        customLoggers: List<CustomLogger>? = null,
    ) = SubscribeMessageProcessor(
        pubnub = PubNubImpl(configuration),
        duplicationManager = DuplicationManager(configuration),
        logConfig = LogConfig("testPnInstanceId", "testUserId", customLoggers),
    )

    /** An `e=5` subscribe frame whose `d` payload is [dataJson]. */
    private fun dataSyncMessage(dataJson: String): SubscribeMessage {
        val frame =
            """
            {"a":"1","f":0,"e":5,"i":"client-1","p":{"t":"17000393136828867","r":43},
             "k":"sub-key","c":"user-1","b":"user-1","d":$dataJson}
            """.trimIndent()
        return gson.fromJson(frame, SubscribeMessage::class.java)
    }

    private fun setUserData(extra: String = "") =
        """
        {
          "version": "1.0",
          "metadata": { "event": "create", "type": "user", "className": "User" },
          "data": {
            "id": "user-1",
            "createdAt": "2026-01-01T00:00:00Z",
            "updatedAt": "2026-01-02T00:00:00Z",
            "eTag": "etag-1",
            "expiresAt": "2026-02-01T00:00:00Z"$extra
          }
        }
        """.trimIndent()

    @Test
    fun `e5 create user is dispatched to a set user leaf`() {
        val result = messageProcessor().processIncomingPayload(dataSyncMessage(setUserData()))
        assertThat(result, instanceOf(PNDataSyncEventResult::class.java))
        assertThat(
            (result as PNDataSyncEventResult).extractedMessage,
            instanceOf(PNSetDataSyncUserEventMessage::class.java),
        )
        assertThat(result.timetoken, iz(17000393136828867L))
        assertThat(result.channel, iz("user-1"))
    }

    @Test
    fun `e5 delete user is dispatched to a delete user leaf`() {
        val data =
            """{"version":"1.0","metadata":{"event":"delete","type":"user"},"data":{"id":"user-1","deletedAt":"2026-03-01T00:00:00Z"}}"""
        val result = messageProcessor().processIncomingPayload(dataSyncMessage(data))
        assertThat(
            (result as PNDataSyncEventResult).extractedMessage,
            instanceOf(PNDeleteDataSyncUserEventMessage::class.java),
        )
    }

    @Test
    fun `e5 membership event carries both endpoint refs`() {
        val data =
            """
            {"version":"1.0","metadata":{"event":"create","type":"membership"},
             "data":{"id":"m-1","channelId":"ch-1","userId":"u-1","createdAt":"2026-01-01T00:00:00Z",
                     "updatedAt":"2026-01-01T00:00:00Z","eTag":"e","expiresAt":"2026-02-01T00:00:00Z"}}
            """.trimIndent()
        val result = messageProcessor().processIncomingPayload(dataSyncMessage(data))
        val leaf = (result as PNDataSyncEventResult).extractedMessage
        assertThat(leaf, instanceOf(PNSetDataSyncMembershipEventMessage::class.java))
        assertThat((leaf as PNSetDataSyncMembershipEventMessage).data.channelId, iz("ch-1"))
        assertThat(leaf.data.userId, iz("u-1"))
    }

    @Test
    fun `e5 unrecognized type is dispatched to the unknown leaf, not dropped`() {
        val data =
            """{"version":"1.0","metadata":{"event":"create","type":"brandNew"},"data":{"id":"x-1"}}"""
        val result = messageProcessor().processIncomingPayload(dataSyncMessage(data))
        assertThat(
            (result as PNDataSyncEventResult).extractedMessage,
            instanceOf(PNUnknownDataSyncEventMessage::class.java),
        )
    }

    @Test
    fun `e5 malformed payload degrades to a single dropped event, logged at debug`() {
        val capturingLogger = CapturingLogger()
        // Routes to the Set-user leaf (valid metadata) but `data` is a bare string instead of the
        // expected object -> binding to PNDataSyncData throws inside the branch.
        val malformed =
            """{"version":"1.0","metadata":{"event":"create","type":"user"},"data":"not-an-object"}"""
        val result = messageProcessor(customLoggers = listOf(capturingLogger))
            .processIncomingPayload(dataSyncMessage(malformed))

        assertThat(result, iz(nullValue()))
        val logged = capturingLogger.debugMessages.mapNotNull {
            (it.message as? LogMessageContent.Text)?.message
        }
        assertThat(logged.any { it.contains("DataSync") }, iz(true))
    }

    private class CapturingLogger : CustomLogger {
        val debugMessages = mutableListOf<LogMessage>()

        override fun debug(logMessage: LogMessage) {
            debugMessages.add(logMessage)
        }
    }
}
