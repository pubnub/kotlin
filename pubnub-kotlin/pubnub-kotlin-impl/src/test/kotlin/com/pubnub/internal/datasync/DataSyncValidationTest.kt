package com.pubnub.internal.datasync

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.anyRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.anyUrl
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * Local validation shared by the DataSync endpoints: a blank sort property is rejected on every list endpoint
 * (instead of being silently dropped or sent as `sort=name,,x`), and a set-but-blank `className` is rejected on the
 * User/Channel APIs, where `null` keeps its "built-in class / whole family" meaning. Nothing reaches the wire.
 */
class DataSyncValidationTest : BaseTest() {
    private val usersPath = "/v1/datasync/subkeys/mySubscribeKey/users"
    private val blankOnly = listOf(PNDataSyncSortField(" "))
    private val blankAmongOthers =
        listOf(PNDataSyncSortField("name"), PNDataSyncSortField(""), PNDataSyncSortField("x"))

    private fun assertRejectedOffline(
        expected: PubNubError,
        call: () -> Unit,
    ) {
        val e = assertThrows(PubNubException::class.java) { call() }
        assertEquals(expected, e.pubnubError)
        verify(0, anyRequestedFor(anyUrl()))
    }

    @Test
    fun blank_sort_property_is_rejected_on_every_list_endpoint() {
        listOf(blankOnly, blankAmongOthers).forEach { sort ->
            assertRejectedOffline(PubNubError.INVALID_ARGUMENTS) {
                pubnub.dataSync.getEntities(className = "TestUser", sort = sort).sync()
            }
            assertRejectedOffline(PubNubError.INVALID_ARGUMENTS) { pubnub.dataSync.getUsers(sort = sort).sync() }
            assertRejectedOffline(PubNubError.INVALID_ARGUMENTS) { pubnub.dataSync.getChannels(sort = sort).sync() }
            assertRejectedOffline(PubNubError.INVALID_ARGUMENTS) { pubnub.dataSync.getMemberships(sort = sort).sync() }
            assertRejectedOffline(PubNubError.INVALID_ARGUMENTS) {
                pubnub.dataSync.getRelationships(className = "TestFriendship", sort = sort).sync()
            }
        }
    }

    @Test
    fun blank_className_is_rejected_on_user_and_channel_apis() {
        listOf("", " ").forEach { blank ->
            assertRejectedOffline(PubNubError.ENTITY_CLASS_MISSING) {
                pubnub.dataSync.createUser(classVersion = 1, className = blank).sync()
            }
            assertRejectedOffline(PubNubError.ENTITY_CLASS_MISSING) {
                pubnub.dataSync.createChannel(classVersion = 1, className = blank).sync()
            }
            assertRejectedOffline(PubNubError.ENTITY_CLASS_MISSING) {
                pubnub.dataSync.getUsers(className = blank).sync()
            }
            assertRejectedOffline(PubNubError.ENTITY_CLASS_MISSING) {
                pubnub.dataSync.getChannels(className = blank).sync()
            }
        }
    }

    @Test
    fun null_className_and_non_blank_sort_still_reach_the_wire() {
        stubFor(get(urlPathEqualTo(usersPath)).willReturn(aResponse().withBody("""{"status":200,"data":[]}""")))

        pubnub.dataSync.getUsers(sort = listOf(PNDataSyncSortField("name"))).sync()

        verify(
            getRequestedFor(urlPathEqualTo(usersPath))
                .withQueryParam("entity_class", absent())
                .withQueryParam("sort", equalTo("name")),
        )
    }
}
