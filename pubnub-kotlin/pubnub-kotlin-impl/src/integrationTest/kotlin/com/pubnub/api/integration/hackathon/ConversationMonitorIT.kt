package com.pubnub.api.integration.hackathon

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.integration.BaseIntegrationTest
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.subscriptions.ConversationContext
import com.pubnub.test.CommonUtils.randomChannel
import com.pubnub.test.Keys
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.IOException
import java.util.UUID
import java.util.concurrent.TimeUnit

class ConversationMonitorIT : BaseIntegrationTest() {
    val apiKey = Keys.apiKeyChatGpt
    // todo
    // v# zrobić enum z dostępnymi kontekstami polityka, sport, zdrowie ?
    // v# when pubNub is initialized, it should check conversationContext property and start separate thread
    // v# jak pobrać wszystkie kanały, które są używane <- użyć HereNow(GLOBAL), zamien allUsersInPubNub na allChannels
    // v# stwórz osobnego PubNuba, który będzie monitorował konwersację
    // v# ten osobny PubNub będzie miał w konfiguracji ustawione conversationContext
    // v# stworzyc osobnę klasę ConversationContextMonitor, która będzie nasłuchiwałą i informowała o wybranej kontekscie konwersacji i wysyłała webhooka
    //  # zmien format simplifiedChannels na NewJsonFormat.json tak, żeby można było dostać od GPT informację o konteksie z wielu kanałów
    // v# można by przechowywać timestamp, pytać co 1 minutę, sprawdzać ilość wiadomości i jak się uzbiera odpowiednia ilość to wołać Chat
    // v# ten kodzik mógłby odpalać klient na SDK serwerowym, tym który ma secret-Key i przydziela tokeny. Sprawdzić ile messadzy zwrata history
    //  # create validation if this is server SDK usage e.g. secret key is present
    //  # zrobić demo
    //  # jak wykorzystać translację np Bahasa Indonesia -> English
    //  # posprzątać kod tzn. mieć lekki test to testowania Translacji, ChatGpt,
    //  #sprawdz rózne języki
    //  Indonezyjski
    //     Apa kamu suka sepakbola?       <-- Lubisz piłkę nożną?
    //     Saya lebih suka bermain tenis. <-- Wolę grać w tenisa.
    // Japonski
    //     あなたはサッカーが好きですか？  <-- Lubisz piłkę nożną?
    //     私はテニスをするほうが好きです。<-- Wolę grać w tenisa.
    // Angielski
    //     Did you hear about the Prime Minister's decision?
    //     No, I only heard about the King's decision.

    @Test
    fun canUseConversationContextMonitor() {
        val configBuilder = PNConfiguration.builder(UserId("client-${UUID.randomUUID()}"), Keys.subKey) {
            publishKey = Keys.pubKey
            conversationContext = ConversationContext.SPORTS
            apiKey = Keys.apiKeyChatGpt
            webHookUrl = Keys.webHookUrl
//            monitoredChannels = listOf("monitoredChannel", "sportChannel")
//            monitoredChannels = listOf("sportChannel")
            logVerbosity = PNLogVerbosity.BODY
        }
        val pubnubSupervisor = PubNub.create(configBuilder.build())

        Thread.sleep(200000)
    }

    @Test
    fun deleteMessages() {
        pubnub.deleteMessages(channels = listOf("monitoredChannel", "sportChannel")).sync()
    }

    @Test
    fun canAnalyzeConversation() {
        // make one pubnub instance to simulate server that will be monitoring the conversation
        val pubnubSupervisor = createPubNub {
            conversationContext = ConversationContext.NONE
        }

        // make two pubnub instances to simulate two users
        val pubnub1 = createPubNub {}
        val pubnub2 = createPubNub {}

        // create a channel
        val channel01 = randomChannel()
        val channel02 = randomChannel()

        // subscribe to the channel
        pubnub1.subscribe(listOf(channel01, channel02))
        pubnub2.subscribe(listOf(channel01, channel02))

        // send a message from user 1
        val msgFromUser1ToChannel01 = "Hi"
        val msgFromUser1ToChannel02 = "Did you hear about Prime Minister decision?"
        pubnub1.publish(channel01, msgFromUser1ToChannel01).sync()
        pubnub1.publish(channel02, msgFromUser1ToChannel02).sync()

        // wait 2 seconds to ensure the message is received
        Thread.sleep(2000)

        // send a message from user 2
        val msgFromUser2ToChannel01 = "Hi" // I am surprised what President said. " // todo remove politic context
        val msgFromUser2ToChannel02 = "Yes I heard about King decision."
        pubnub2.publish(channel01, msgFromUser2ToChannel01).sync()
        pubnub2.publish(channel02, msgFromUser2ToChannel02).sync()

        // wait 2 seconds to ensure the message is received
        Thread.sleep(2000)

        // Global HereNow is used. Remember about performance and security concerns.
        // get all channels from Presence. This is not the same as getting all channels from AppContext.
        val globalHereNowResult: PNHereNowResult? = pubnubSupervisor.hereNow().sync()
        val allChannels: MutableSet<String>? = globalHereNowResult?.channels?.keys

        // get timestamp for one minute ago
        val timestamp: Long = System.currentTimeMillis() * 10000
        val timestampOneMinuteAgo = timestamp - 6000000000

        // for each subscribedChannel, fetch the conversation history
        var conversationHistory: PNFetchMessagesResult? = null
        allChannels?.let { allChannels ->
            if (allChannels.isNotEmpty()) {
                conversationHistory = pubnubSupervisor.fetchMessages(
                    channels = allChannels.toList(),
                    page = PNBoundedPage(end = timestampOneMinuteAgo)
                ).sync()
            }
        }

        // I would like to convert using gson "channels" to JSON but only following fields : channel, message, uuid
        val simplifiedChannels: List<SimplifiedMessageItem> =
            conversationHistory?.channels!!.flatMap { (channel: String, messages: List<PNFetchMessageItem>) ->
                messages.map { message ->
                    SimplifiedMessageItem(channel, message.uuid, message.message)
                }
            }

        val gson = Gson()
        // todo zmie strukturę tak żeby dla każdego kanału podana była lista wiadomości
        val conversation = gson.toJson(simplifiedChannels)

        // send json to ChatGpt asking for opinion
        val questions = "I will pass conversation. Please, extract conversation. It is stored in field \"message\".\n" +
            "Answer \\\"yes\\\" if it talks about politics and answer \\\"no\\\" if it doesn't talk about politics. Include probability. Example response should be in JSON format and look like: \n" +
            "{“decision” = “yes”, “probabilityInPercents” = 80, “user” = “client-5f6aac7e-2254-4e30-a55e-2778bd71503e”, “channel” = “ch_1702020815470_1A27301E2A”, message=“Did you hear about Prime Minister decision?”}\n" +
            "Or\n" +
            "{“decision” = “no”, “probabilityInPercents” = 90, “user” = “client-a6be84f4-199d-4d45-aa60-b746c9f6eaab”, “channel” = “ch_1702020815471_6D16D3B3AC”,  message=“”}\n" +
            "There should be only one object containing decision, probabilityInPercents, channel, user, message. Remember in response there should be only one object containing decision, probabilityInPercents, channel, user, message. This is the conversation:"
        val prompt = questions + conversation

        val response: Answer = callChatGptApi(prompt, apiKey)
//        val response: Answer = Answer(decision = "Yes", probabilityInPercents = "30", user = "user1", channel = "ch1", message = "mess")
        println("ChatGPT Response: ")
        println("             Decision: ${response.decision} ")
        println("             ProbabilityInPercents: ${response.probabilityInPercents}")
        println("             User: ${response.user}")
        println("             Channel: ${response.channel}")
        println("             Message: ${response.message}")

        // send info to webhook
        if (response.decision.equals("Yes", ignoreCase = true)) {
            callWebhook(response)
        }

        // assert that the conversation history contains the messages from both users
        assertEquals(2, conversationHistory?.channels?.get(channel01)?.size)
//        assertEquals("\"$message1\"", conversationHistory?.channels?.get(channel01)?.get(0)?.message.toString())
//        assertEquals("\"$message2\"", conversationHistory?.channels?.get(channel01)?.get(1)?.message.toString())

        println("-= End of test =-")
        Thread.sleep(100000)
    }

    @Test
    fun chatGptCanResponseCorrectly() {
        val prompt = "I will pass conversation. Please, extract conversation. It is stored in field \"message\".\n" +
            "Answer \\\"yes\\\" if it talks about politics and answer \\\"no\\\" if it doesn't talk about politics. " +
            "Include probability. Example response should be in JSON format and look like: \n" +
            "{“decision” = “yes”, “probabilityInPercents” = 80, “user” = “client-5f6aac7e-2254-4e30-a55e-2778bd71503e”, “channel” = “ch_1702020815470_1A27301E2A”, message=“Did you hear about Prime Minister decision?”}\n" +
            "Or\n" +
            "{“decision” = “no”, “probabilityInPercents” = 90, “user” = “client-a6be84f4-199d-4d45-aa60-b746c9f6eaab”, “channel” = “ch_1702020815471_6D16D3B3AC”,  message=“”}\n" +
            "There should be only one object containing decision, probabilityInPercents, channel, user, message. " +
            "This is the conversation:[{\"channel\":\"ch_1713789512093_8B669E3421\",\"uuid\":\"client-3a6ac057-9658-47cf-a48d-bf0539c700a0\",\"message\":\"Did you hear about Prime Minister decision?\"},{\"channel\":\"ch_1713789512093_8B669E3421\",\"uuid\":\"client-5c96f1d9-bbd7-4beb-978e-04c6522d1866\",\"message\":\"Yes I heard about King decision.\"},{\"channel\":\"ch_1713789512092_92B8749DFD\",\"uuid\":\"client-3a6ac057-9658-47cf-a48d-bf0539c700a0\",\"message\":\"Hi\"},{\"channel\":\"ch_1713789512092_92B8749DFD\",\"uuid\":\"client-5c96f1d9-bbd7-4beb-978e-04c6522d1866\",\"message\":\"Hi\"}]"
        val response: Answer = callChatGptApi(prompt, apiKey)

        println("ChatGPT Response: ")
        println("             Decision: ${response.decision} ")
        println("             ProbabilityInPercents: ${response.probabilityInPercents}")
        println("             User: ${response.user}")
        println("             Channel: ${response.channel}")
        println("             Message: ${response.message}")

        // send info to webhook
        if (response.decision.equals("Yes", ignoreCase = true)) {
            callWebhook(response)
        }
    }

    @Test
    fun askChatGptForTime() {
        val client = OkHttpClient()
        val body = """
        {
            "model": "gpt-3.5-turbo",
            "messages": [{
                "role": "user",
                "content": "What is the capital of Poland?"
            }],
            "temperature": 0.5,
            "max_tokens": 64
        }
        """.trimIndent()

        val requestBody = body.toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val responseBody = response.body?.string()
            println("ChatGPT Response: $responseBody")
        } else {
            println("Failed to fetch response: ${response.code}")
            val errorBody = response.body?.string()
            println("Error details: $errorBody")
        }
    }

    private fun callWebhook(response: Answer) {
        val webhookUrl = Keys.webHookUrl
        val jsonBody = """
        {
            "decision": ${response.decision},
            "probabilityInPercents": ${response.probabilityInPercents},
            "user": ${response.user},
            "channel": ${response.channel},
            "message": ${response.message},
        }
        """.trimIndent()

        // Specify the media type for the request body (application/json in this case)
        val mediaType = "application/json; charset=utf-8".toMediaType()

        // Create an OkHttpClient instance
        val client = OkHttpClient()

        // Create the request body
        val requestBody = jsonBody.toRequestBody(mediaType)

        // Build the request
        val request = Request.Builder()
            .url(webhookUrl)
            .header("Content-Type", "application/json")
            .post(requestBody)
            .build()

        // Execute the request
        val response = client.newCall(request).execute()

        // Check the response
        if (response.isSuccessful) {
            println("Webhook call successful. Response: ${response.body?.string()}")
        } else {
            println("Webhook call failed. Response code: ${response.code}")
        }
    }

    data class SimplifiedMessageItem(
        val channel: String,
        val uuid: String?,
        val message: JsonElement
    )

    fun callChatGptApi(prompt: String, apiKey: String): Answer {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val jsonBody = JSONObject()
        jsonBody.put("model", "gpt-4")
        jsonBody.put(
            "messages",
            listOf(
                mapOf("role" to "system", "content" to "You are a helpful assistant."),
                mapOf("role" to "user", "content" to prompt)
            )
        )

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonBody.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .post(body)
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Unexpected response from ChatGpt $response")
            }

            val message = response.body!!.string()
            println("-=message=-")
            println(message)
            val gson = Gson()
            val data = gson.fromJson(message, ChatCompletion::class.java)
            val answerAsJson = data.choices[0].message.content
            val answer: Answer = gson.fromJson(answerAsJson, Answer::class.java)
            return answer // we are expecting JSON here
        }
    }

    data class ChatCompletion(
        val id: String,
        val `object`: String,
        val created: Long,
        val model: String,
        val choices: List<Choice>,
        val usage: Usage
    )

    data class Choice(
        val index: Int,
        val message: Message,
        val finish_reason: String
    )

    data class Message(
        val role: String,
        val content: String
    )

    data class Usage(
        val prompt_tokens: Int,
        val completion_tokens: Int,
        val total_tokens: Int
    )

    data class Answer(
        val decision: String, // yes or no
        val probabilityInPercents: String, // change to Int?
        val user: String, // client-dda82791-eb4d-48ca-9fa4-cab910381221
        val channel: String,
        val message: String
    )
}
