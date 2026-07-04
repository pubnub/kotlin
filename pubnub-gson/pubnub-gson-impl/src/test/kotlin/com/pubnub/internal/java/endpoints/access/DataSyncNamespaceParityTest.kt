package com.pubnub.internal.java.endpoints.access

import com.pubnub.api.models.consumer.access_manager.v3.DataSyncNamespace
import org.junit.Assert.assertEquals
import org.junit.Test
import com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant as JavaDataSyncGrant

/**
 * The Java-facing [JavaDataSyncGrant] namespace constants must duplicate the Kotlin [DataSyncNamespace] values
 * because the two live in different modules.
 *
 * This test is that single point of enforcement: if either side drifts, a token minted by one SDK and parsed by
 * the other would silently lose its DataSync grants. `pubnub-gson-impl` sees both modules, so it can assert equality.
 */
class DataSyncNamespaceParityTest {
    @Test
    fun javaConstantsMatchKotlinDataSyncNamespace() {
        assertEquals(DataSyncNamespace.ENTITIES, JavaDataSyncGrant.DATASYNC_ENTITIES)
        assertEquals(DataSyncNamespace.RELATIONSHIPS, JavaDataSyncGrant.DATASYNC_RELATIONSHIPS)
        assertEquals(DataSyncNamespace.MEMBERSHIPS, JavaDataSyncGrant.DATASYNC_MEMBERSHIPS)
    }
}
