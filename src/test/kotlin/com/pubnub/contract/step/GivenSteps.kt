package com.pubnub.contract.step

import com.pubnub.contract.state.World
import io.cucumber.java.en.Given
import kotlin.random.Random

class GivenSteps(private val world: World) {
    @Given("auth key")
    fun auth_key() {
        world.configuration.authKey = randomBase64EncodedString()
    }

    @Given("secret key")
    fun secret_key() {
        world.configuration.secretKey = randomBase64EncodedString()
    }

    @Given("no auth key")
    fun no_auth_key() {
        world.configuration.authKey = ""
    }

    @Given.Givens(
        Given("token"),
        Given("a token"),
        Given("a valid token with permissions to publish with channel {string}"),
        Given("an expired token with permissions to publish with channel {string}")
    )
    fun a_token() {
        world.tokenString = randomBase64EncodedString()
    }

    @Given("the token string {string}")
    fun the_token_string(string: String) {
        world.tokenString = string
    }
}

fun randomBase64EncodedString(): String {
    val characters: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') + '-'
    return java.util.Base64.getUrlEncoder()
        .encodeToString(
            (1..30).map { Random.nextInt(0, characters.size) }.map { characters[it] }.joinToString("")
                .toByteArray()
        )
}
