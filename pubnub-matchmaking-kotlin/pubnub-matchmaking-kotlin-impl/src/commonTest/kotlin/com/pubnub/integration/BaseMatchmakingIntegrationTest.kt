package com.pubnub.integration

import com.pubnub.matchmaking.User
import com.pubnub.matchmaking.internal.UserImpl
import com.pubnub.matchmaking.internal.sdk.MatchmakingImpl
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.AfterTest
import kotlin.test.BeforeTest


abstract class BaseMatchmakingIntegrationTest : BaseIntegrationTest() {
    private val usersToRemove = mutableSetOf<String>()
    private val userid = randomString() + "!_=-@"
//    private val userid = randomString()
    lateinit var user1: User

    val matchmaking: MatchmakingImpl by lazy(LazyThreadSafetyMode.NONE) {
        MatchmakingImpl(pubnub)
    }


    @BeforeTest
    override fun before() {
        super.before()  // Initializes pubnub, etc.
        user1 = UserImpl(
            matchmaking = matchmaking,
            id = userid,
            name = randomString(),
            externalId = randomString(),
            profileUrl = randomString(),
            email = randomString(),
            status = randomString(),
            type = "type",
        ).also { usersToRemove.add(it.id) }
    }

    @AfterTest
    override fun after(){
        val exceptionHandler = CoroutineExceptionHandler { _, _ -> }
        runTest {
            supervisorScope {
                usersToRemove.forEach {
                    launch(exceptionHandler) {
                        pubnub.removeUUIDMetadata(it).await()
                    }
                }

            }
        }

        super.after()
    }

}

internal suspend fun delayInMillis(timeMillis: Long) {
    withContext(Dispatchers.Default) {
        delay(timeMillis)
    }
}

fun randomString() = (0..6).map { "abcdefghijklmnopqrstuvw".random() }.joinToString("")