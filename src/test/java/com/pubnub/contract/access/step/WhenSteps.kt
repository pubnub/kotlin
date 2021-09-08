package com.pubnub.contract.access.step

import com.pubnub.contract.access.state.GrantTokenState
import com.pubnub.contract.state.World
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import io.cucumber.java.en.When
import org.junit.Assert

class WhenSteps(
    private val grantTokenState: GrantTokenState,
    private val world: World
) {


    @When("I grant a token specifying those permissions")
    fun grant_token() {
        grantTokenState.result = world.pubnub.grantToken().let {
            grantTokenState.TTL?.let { ttl ->
                it.ttl(ttl.toInt())
            }
            grantTokenState.authorizedUUID?.let { authorizedUUID -> it.authorizedUUID(authorizedUUID) }
            it.channels(grantTokenState.definedGrants.filterIsInstance(ChannelGrant::class.java))
            it.channelGroups(grantTokenState.definedGrants.filterIsInstance(ChannelGroupGrant::class.java))
            it.uuids(grantTokenState.definedGrants.filterIsInstance(UUIDGrant::class.java))

            it.sync()
        }
    }

    @When("I attempt to grant a token specifying those permissions")
    fun i_attempt_to_grant_a_token_specifying_those_permissions() {
        try {
            grant_token()
            Assert.fail("Expected exception")
        } catch (ex: PubNubException) {
            world.pnException = ex
        } catch (ex: AssertionError) {
            throw ex
        } catch (t: Throwable) {
            Assert.fail("Expected PubNubException but got throwable $t")
        }
    }

    @When("I parse the token")
    fun i_parse_the_token() {
        grantTokenState.parsedToken = world.pubnub.parseToken(grantTokenState.result?.token)
    }
}