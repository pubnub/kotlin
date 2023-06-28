package com.pubnub.contract.access.step

import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.contract.access.state.GrantTokenState
import io.cucumber.java.en.When
import org.junit.Assert

class WhenSteps(
    private val grantTokenState: GrantTokenState,
) {

    @When("I grant a token specifying those permissions")
    fun grant_token() {
        val definedGrants = grantTokenState.definedGrants.map { it.evaluate() }
        @Suppress("DEPRECATION")
        grantTokenState.result = grantTokenState.pubnub.grantToken(
            ttl = grantTokenState.TTL?.toInt() ?: throw RuntimeException("TTL expected"),
            authorizedUUID = grantTokenState.authorizedUUID,
            channels = definedGrants.filterIsInstance(ChannelGrant::class.java),
            channelGroups = definedGrants.filterIsInstance(ChannelGroupGrant::class.java),
            uuids = definedGrants.filterIsInstance(UUIDGrant::class.java)
        ).sync()
    }

    @When("I attempt to grant a token specifying those permissions")
    fun i_attempt_to_grant_a_token_specifying_those_permissions() {
        try {
            grant_token()
            Assert.fail("Expected exception")
        } catch (ex: PubNubException) {
            grantTokenState.pnException = ex
        } catch (ex: AssertionError) {
            throw ex
        } catch (t: Throwable) {
            Assert.fail("Expected PubNubException but got throwable $t")
        }
    }

    @When("I parse the token")
    fun i_parse_the_token() {
        grantTokenState.parsedToken = grantTokenState.pubnub.parseToken(grantTokenState.result?.token!!)
    }

    @When("I revoke a token")
    fun i_revoke_the_token() {
        try {
            grantTokenState.pubnub.revokeToken(grantTokenState.tokenString!!).sync()
        } catch (e: PubNubException) {
            grantTokenState.pnException = e
        }
    }

    @When("I publish a message using that auth token with channel {string}")
    fun i_publish_a_message_using_that_auth_token_with_channel(channel: String) {
        grantTokenState.pubnub.setToken(grantTokenState.tokenString)
        grantTokenState.pubnub.publish(
            channel = channel,
            message = "Message"
        ).sync()
    }

    @When("I attempt to publish a message using that auth token with channel {string}")
    fun i_attempt_to_publish_a_message_using_that_auth_token_with_channel(channel: String) {
        grantTokenState.pubnub.setToken(grantTokenState.tokenString)

        try {
            grantTokenState.pubnub.publish(
                channel = channel,
                message = "Message"
            ).sync()
        } catch (e: PubNubException) {
            grantTokenState.pnException = e
        }
    }
}
