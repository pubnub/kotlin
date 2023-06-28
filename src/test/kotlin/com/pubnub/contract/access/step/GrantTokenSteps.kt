package com.pubnub.contract.access.step

import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNChannelGroupPatternGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNChannelGroupResourceGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNChannelPatternGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNChannelResourceGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import com.pubnub.api.models.consumer.access_manager.v3.PNUUIDPatternGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNUUIDResourceGrant
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.contract.access.parameter.PermissionType
import com.pubnub.contract.access.parameter.ResourceType
import com.pubnub.contract.access.parameter.patternPermissionsMap
import com.pubnub.contract.access.parameter.resourcePermissionsMap
import com.pubnub.contract.access.state.FutureCallGrant
import com.pubnub.contract.access.state.GrantTokenState
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert
import kotlin.random.Random

class GrantTokenSteps(private val grantTokenState: GrantTokenState) {
    private val tokenWithAll =
        "qEF2AkF0GmEI03xDdHRsGDxDcmVzpURjaGFuoWljaGFubmVsLTEY70NncnChb2NoYW5uZWxfZ3JvdXAtMQVDdXNyoENzcGOgRHV1aWShZnV1aWQtMRhoQ3BhdKVEY2hhbqFtXmNoYW5uZWwtXFMqJBjvQ2dycKF0XjpjaGFubmVsX2dyb3VwLVxTKiQFQ3VzcqBDc3BjoER1dWlkoWpedXVpZC1cUyokGGhEbWV0YaBEdXVpZHR0ZXN0LWF1dGhvcml6ZWQtdXVpZENzaWdYIPpU-vCe9rkpYs87YUrFNWkyNq8CVvmKwEjVinnDrJJc"

    @Given("I have a known token containing UUID pattern Permissions")
    fun i_have_a_known_token_containing_uuid_pattern_permissions() {
        grantTokenState.result = PNGrantTokenResult(tokenWithAll)
    }

    @Given("I have a known token containing UUID resource permissions")
    fun i_have_a_known_token_containing_uuid_resource_permissions() {
        grantTokenState.result = PNGrantTokenResult(tokenWithAll)
    }

    @Given("I have a known token containing an authorized UUID")
    fun i_have_a_known_token_containing_an_authorized_uuid() {
        grantTokenState.result = PNGrantTokenResult(tokenWithAll)
    }

    @Given("the {string} {resourceType} pattern access permissions")
    fun the_channel_pattern_access_permissions(pattern: String, resourceType: ResourceType) {
        when (resourceType) {
            ResourceType.CHANNEL -> grantTokenState.currentGrant = FutureCallGrant(ChannelGrant.pattern(pattern))
            ResourceType.CHANNEL_GROUP ->
                grantTokenState.currentGrant =
                    FutureCallGrant(ChannelGroupGrant.pattern(pattern))
            ResourceType.UUID -> grantTokenState.currentGrant = FutureCallGrant(UUIDGrant.pattern(pattern))
        }
    }

    @Given("the {string} {resourceType} resource access permissions")
    fun the_channel_resource_access_permissions(name: String, resourceType: ResourceType) {
        when (resourceType) {
            ResourceType.CHANNEL -> grantTokenState.currentGrant = FutureCallGrant(ChannelGrant.name(name))
            ResourceType.CHANNEL_GROUP -> grantTokenState.currentGrant = FutureCallGrant(ChannelGroupGrant.id(name))
            ResourceType.UUID -> grantTokenState.currentGrant = FutureCallGrant(UUIDGrant.id(name))
        }
    }

    @Given("the TTL {ttl}")
    fun the_ttl(ttl: Long) {
        grantTokenState.TTL = ttl
    }

    @And("grant resource permission {permissionType}")
    fun grant_resource_permission(permissionType: PermissionType) {
        grant_permission(permissionType)
    }

    @And("grant pattern permission {permissionType}")
    fun grant_pattern_permission(permissionType: PermissionType) {
        grant_permission(permissionType)
    }

    @Given("deny resource permission GET")
    fun deny_resource_permission_get() {
        // in grant token everything is denied by default
    }

    @Given("a token")
    fun a_token() {
        val characters: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') + '-'
        grantTokenState.tokenString =
            (1..30).map { Random.nextInt(0, characters.size) }.map { characters[it] }.joinToString("")
    }

    @Given("a valid token with permissions to publish with channel {string}")
    fun a_valid_token_with_permissions_to_publish_with_channel(@Suppress("UNUSED_PARAMETER") string: String?) {
        return a_token()
    }

    @Given("an expired token with permissions to publish with channel {string}")
    fun an_expired_token_with_permissions_to_publish_with_channel(@Suppress("UNUSED_PARAMETER") string: String) {
        return a_token()
    }

    @Given("the token string {string}")
    fun the_token_string(string: String) {
        grantTokenState.tokenString = string
    }

    private fun grant_permission(permissionType: PermissionType) {
        when (permissionType) {
            PermissionType.READ ->
                grantTokenState.currentGrant?.let { futureCall ->
                    futureCall.addAction {
                        when (it) {
                            is PNChannelPatternGrant -> {
                                it.copy(read = true)
                            }
                            is PNChannelResourceGrant -> {
                                it.copy(read = true)
                            }
                            is PNChannelGroupPatternGrant -> {
                                it.copy(read = true)
                            }
                            is PNChannelGroupResourceGrant -> {
                                it.copy(read = true)
                            }
                            else -> throw RuntimeException()
                        }
                    }
                }
            PermissionType.WRITE -> grantTokenState.currentGrant?.let { futureCall ->
                futureCall.addAction {
                    when (it) {
                        is PNChannelResourceGrant -> {
                            it.copy(write = true)
                        }
                        is PNChannelPatternGrant -> {
                            it.copy(write = true)
                        }
                        else -> throw RuntimeException()
                    }
                }
            }
            PermissionType.GET -> grantTokenState.currentGrant?.let { futureCall ->
                futureCall.addAction {
                    when (it) {
                        is PNChannelResourceGrant -> {
                            it.copy(get = true)
                        }
                        is PNChannelPatternGrant -> {
                            it.copy(get = true)
                        }
                        is PNUUIDResourceGrant -> {
                            it.copy(get = true)
                        }
                        is PNUUIDPatternGrant -> {
                            it.copy(get = true)
                        }
                        else -> throw RuntimeException()
                    }
                }
            }
            PermissionType.MANAGE -> grantTokenState.currentGrant?.let { futureCall ->
                futureCall.addAction {
                    when (it) {
                        is PNChannelPatternGrant -> {
                            it.copy(manage = true)
                        }
                        is PNChannelResourceGrant -> {
                            it.copy(manage = true)
                        }
                        is PNChannelGroupPatternGrant -> {
                            it.copy(manage = true)
                        }
                        is PNChannelGroupResourceGrant -> {
                            it.copy(manage = true)
                        }
                        else -> throw RuntimeException()
                    }
                }
            }
            PermissionType.UPDATE -> grantTokenState.currentGrant?.let { futureCall ->
                futureCall.addAction {
                    when (it) {
                        is PNChannelResourceGrant -> {
                            it.copy(update = true)
                        }
                        is PNChannelPatternGrant -> {
                            it.copy(update = true)
                        }
                        is PNUUIDResourceGrant -> {
                            it.copy(update = true)
                        }
                        is PNUUIDPatternGrant -> {
                            it.copy(update = true)
                        }
                        else -> throw RuntimeException()
                    }
                }
            }
            PermissionType.JOIN -> grantTokenState.currentGrant?.let { futureCall ->
                futureCall.addAction {
                    when (it) {
                        is PNChannelResourceGrant -> {
                            it.copy(join = true)
                        }
                        is PNChannelPatternGrant -> {
                            it.copy(join = true)
                        }
                        else -> throw RuntimeException()
                    }
                }
            }

            PermissionType.DELETE -> grantTokenState.currentGrant?.let { futureCall ->
                futureCall.addAction {
                    when (it) {
                        is PNChannelResourceGrant -> {
                            it.copy(delete = true)
                        }
                        is PNChannelPatternGrant -> {
                            it.copy(delete = true)
                        }
                        is PNUUIDResourceGrant -> {
                            it.copy(delete = true)
                        }
                        is PNUUIDPatternGrant -> {
                            it.copy(delete = true)
                        }
                        else -> throw RuntimeException()
                    }
                }
            }
        }
    }

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

    @Then("the authorized UUID {string}")
    fun authorized_uuid(uuid: String) {
        grantTokenState.authorizedUUID = uuid
    }

    @Then("the parsed token output contains the authorized UUID {string}")
    fun the_parsed_token_output_contains_the_authorized_uuid(uuid: String) {
        val token = grantTokenState.parsedToken!!
        MatcherAssert.assertThat(token.authorizedUUID, Matchers.`is`(uuid))
    }

    @Then("the token contains the authorized UUID {string}")
    fun the_token_contains_the_authorized_uuid(uuid: String) {
        val result = grantTokenState.result!!
        val parsedToken = grantTokenState.pubnub.parseToken(result.token)
        MatcherAssert.assertThat(parsedToken.authorizedUUID, Matchers.`is`(uuid))
    }

    @Then("the token contains the TTL {ttl}")
    fun the_token_contains_the_ttl(ttl: Long) {
        val result = grantTokenState.result!!
        val token = grantTokenState.pubnub.parseToken(result.token)
        MatcherAssert.assertThat(token.ttl, Matchers.`is`(ttl))
    }

    @Then("the token does not contain an authorized uuid")
    fun the_token_does_not_contain_an_authorized_uuid() {
        val result = grantTokenState.result!!
        val token = grantTokenState.pubnub.parseToken(result.token)
        MatcherAssert.assertThat(token.authorizedUUID, Matchers.nullValue())
    }

    @Then("the token has {string} {resourceType} resource access permissions")
    fun the_token_has_channel_resource_access_permissions(name: String, resourceType: ResourceType) {
        val token = parsedToken()!!
        val permissions = token.resourcePermissionsMap(resourceType)[name]
        MatcherAssert.assertThat(
            "Token doesn't contain required permissions $token",
            permissions,
            Matchers.notNullValue()
        )
        grantTokenState.currentResourcePermissions = permissions
    }

    @Then("the token has {string} {resourceType} pattern access permissions")
    fun the_token_has_channel_pattern_access_permissions(name: String, resourceType: ResourceType) {
        val token = parsedToken()!!
        val permissions = token.patternPermissionsMap(resourceType)[name]
        MatcherAssert.assertThat(
            "Token doesn't contain required permissions $token",
            permissions,
            Matchers.notNullValue()
        )
        grantTokenState.currentResourcePermissions = permissions
    }

    @Then("token resource permission {permissionType}")
    fun token_resource_permission(permissionType: PermissionType) {
        assertPermissions(permissionType)
    }

    @Then("token pattern permission {permissionType}")
    fun token_pattern_permission(permissionType: PermissionType) {
        assertPermissions(permissionType)
    }

    @Then("I get confirmation that token has been revoked")
    fun i_get_confirmation_that_token_has_been_revoked() {
        MatcherAssert.assertThat(grantTokenState.pnException, Matchers.nullValue())
    }

    private fun assertPermissions(permissionType: PermissionType) {
        val permissions = grantTokenState.currentResourcePermissions!!
        when (permissionType) {
            PermissionType.READ -> MatcherAssert.assertThat(permissions.read, Matchers.`is`(true))
            PermissionType.WRITE -> MatcherAssert.assertThat(permissions.write, Matchers.`is`(true))
            PermissionType.GET -> MatcherAssert.assertThat(permissions.get, Matchers.`is`(true))
            PermissionType.MANAGE -> MatcherAssert.assertThat(permissions.manage, Matchers.`is`(true))
            PermissionType.UPDATE -> MatcherAssert.assertThat(permissions.update, Matchers.`is`(true))
            PermissionType.JOIN -> MatcherAssert.assertThat(permissions.join, Matchers.`is`(true))
            PermissionType.DELETE -> MatcherAssert.assertThat(permissions.delete, Matchers.`is`(true))
        }
    }

    private fun parsedToken(): PNToken? {
        return grantTokenState.parsedToken ?: grantTokenState.result?.let { grantTokenState.pubnub.parseToken(it.token) }
    }
}
