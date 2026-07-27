package com.pubnub.api.integration

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.datasync.entity.PNRemoveEntityResult
import com.pubnub.test.CommonUtils.randomValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class DataSyncEntityIntegrationTest : BaseIntegrationTest() {
    private val entityClass = "TestUser"
    private val entityClassVersion = 1
    private val entityId = "entity-" + randomValue()

    data class TestUserPayload(
        val username: String,
        val email: String,
        val hobby: String? = null,
        val custom: String? = null,
    )

    @Test
    fun createGetAndDeleteEntity() {
        // create
        val payload = TestUserPayload(
            username = "Alice",
            email = "alice@example.com",
            hobby = "poetry",
            custom = "value",
        )
        val createResult = server.dataSync.entity.create(
            entityClass = entityClass,
            entityClassVersion = entityClassVersion,
            entityId = entityId,
            status = "active",
            payload = payload,
        ).sync()

        assertEquals(entityId, createResult.data.id)
        assertEquals(entityClass, createResult.data.entityClass)
        assertEquals(entityClassVersion, createResult.data.entityClassVersion)
        assertNotNull(createResult.data.eTag)
        assertEquals(payload.username, createResult.data.payload?.get("username"))
        assertEquals(payload.email, createResult.data.payload?.get("email"))

        // get
        val getResult = server.dataSync.entity.get(entityId).sync()
        assertEquals(entityId, getResult.data.id)
        assertEquals(entityClass, getResult.data.entityClass)
        assertEquals("active", getResult.data.status)

        // delete
        val pnRemoveEntityResult = server.dataSync.entity.delete(entityId).sync()

        // get after delete -> 404
        try {
            server.dataSync.entity.get(entityId).sync()
            fail("Expected a 404 after deleting the entity")
        } catch (e: PubNubException) {
            assertEquals(404, e.statusCode)
        }
    }

    /**
     * Same create/get/delete flow as [createGetAndDeleteEntity], but instead of relying on the client's
     * own secretKey, the "server" (the only party holding the secretKey) mints a scoped PAM token for the
     * client's authorized UUID and the client authenticates with it via [PubNub.setToken]. This mirrors the
     * production setup where the client never sees the secretKey.
     */
    @Test
    fun createGetAndDeleteEntityWithServerGrantedToken() {
        // server grants a DataSync token scoped to this client's authorized UUID
        val token = server.grantToken(
            ttl = 60,
            authorizedUUID = pubnub.configuration.userId.value,
            dataSync = listOf(
                DataSyncGrant.entity(
                    entityId,
                    get = true,
//                    update = true,
                    delete = true,
                    create = true
                ),
            ),
        ).sync().token

        // client authenticates with the server-issued token
        pubnub.setToken(token)

        // create
        val payload = TestUserPayload(
            username = "Alice",
            email = "alice@example.com",
            hobby = "poetry",
            custom = "value",
        )
        val createResult = pubnub.dataSync.entity.create(
            entityClass = entityClass,
            entityClassVersion = entityClassVersion,
            entityId = entityId,
            status = "active",
            payload = payload,
        ).sync()

        assertEquals(entityId, createResult.data.id)
        assertEquals(entityClass, createResult.data.entityClass)
        assertEquals(entityClassVersion, createResult.data.entityClassVersion)
        assertNotNull(createResult.data.eTag)
        assertEquals(payload.username, createResult.data.payload?.get("username"))
        assertEquals(payload.email, createResult.data.payload?.get("email"))

        // get
        val getResult = pubnub.dataSync.entity.get(entityId).sync()
        assertEquals(entityId, getResult.data.id)
        assertEquals(entityClass, getResult.data.entityClass)
        assertEquals("active", getResult.data.status)

        // delete
        pubnub.dataSync.entity.delete(entityId).sync()

        // get after delete -> 404
        try {
            pubnub.dataSync.entity.get(entityId).sync()
            fail("Expected a 404 after deleting the entity")
        } catch (e: PubNubException) {
            assertEquals(404, e.statusCode)
        }
    }

    @Test
    fun createWithServerGeneratedId() {
        val createResult = server.dataSync.entity.create(
            entityClass = entityClass,
            entityClassVersion = entityClassVersion,
            payload = mapOf("username" to "Bob")
        ).sync()

        val generatedId = createResult.data.id
        assertTrue(generatedId.isNotBlank())

        // cleanup
        server.dataSync.entity.delete(generatedId).sync()
    }

    @Test
    fun getBlankEntityIdThrows() {
        try {
            server.dataSync.entity.get("").sync()
            fail("Expected validation to reject a blank entityId")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.ENTITY_ID_MISSING, e.pubnubError)
        }
    }
}
