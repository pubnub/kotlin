package com.pubnub.internal

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.api.v2.subscriptions.ConversationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class ConversationContextMonitor(private val pubNub: PubNubCore) {
    private var timetoken: Long = 0L
    private val apiKey: String? = pubNub.configuration.apiKey
    private val conversationContext: ConversationContext = pubNub.configuration.conversationContext
    private val webHookUrl: String? = pubNub.configuration.webHookUrl
    private val monitoredChannelsFromConfig: List<String>? = pubNub.configuration.monitoredChannels
    private val gson = Gson()

    fun monitorConversationAndSendNotification() {
        // todo if there are less than 50 messages wait for next execution of conversationSupervisorExecutor
        var monitoredChannels: MutableSet<String>?

        // no channels have been specified by user
        if (monitoredChannelsFromConfig.isNullOrEmpty()) {
            // get all currently subscribe channels. What about channels that someone only publish to? They are not support for now.
            val globalHereNowResult: PNHereNowResult = pubNub.hereNow().sync()
            monitoredChannels = globalHereNowResult.channels.keys
        } else {
            monitoredChannels = monitoredChannelsFromConfig?.toMutableSet()
        }

        var messagesForAllChannels: PNFetchMessagesResult? = null
        monitoredChannels?.let {
            messagesForAllChannels = getMessagesForMonitoredChannelAndUpdateTimestamp(it)
        }

        // I would like to convert using gson "channels" to JSON but only following fields : channel, message, uuid
        val simplifiedMessages: List<SimplifiedMessageItem> =
            convertMessagesFromAllChannelsIntoSimplifiedMessages(messagesForAllChannels)

        if (simplifiedMessages.isNotEmpty()) {
            val conversations: String = gson.toJson(simplifiedMessages)
            val prompt = buildPromptToChatGpt(conversations)
            val response: List<Answer> = callChatGptApi(prompt, apiKey!!)
            printResponseDetails(response)
            // send info to webhook
            callWebhook(response)
        }
    }

    private fun printResponseDetails(response: List<Answer>) {
        for (answer in response) {
            println("ChatGPT Response: ")
            println("             Decision: ${answer.decision} ")
            println("             ProbabilityInPercents: ${answer.probabilityInPercents}")
            println("             User: ${answer.user}")
            println("             Channel: ${answer.channel}")
            println("             Message: ${answer.message}")
            println("             ConversationContext: ${answer.conversationContext}")
        }
    }

    private fun buildPromptToChatGpt(conversations: String): String {
        // send json to ChatGpt asking for opinion
        val questions =
            "I will pass conversation. Please, extract conversation. It is stored in field \"message\". Aggragate messages by channel\n" +
                "Answer \\\"yes\\\" if it talks about $conversationContext and answer \\\"no\\\" if it doesn't talk about $conversationContext. Include probability. Response should be in JSON format and look like: \n" +
                "{“decision” = “yes”, “probabilityInPercents” = 80, “user” = “client-5f6aac7e-2254-4e30-a55e-2778bd71503e”, “channel” = “ch_1702020815470_1A27301E2A”, message=“Did you hear about Prime Minister decision?”, conversationContext=“politics”}\n" +
                "Or\n" +
                "{“decision” = “no”, “probabilityInPercents” = 90, “user” = “client-a6be84f4-199d-4d45-aa60-b746c9f6eaab”, “channel” = “ch_1702020815471_6D16D3B3AC”,  message=“”, conversationContext=“politics”}\n" +
                "Your response should be only JSON  string containing list of objects containing decision, probabilityInPercents, channel, user, message. This is the conversation:"
        return questions + conversations
    }

    private fun convertMessagesFromAllChannelsIntoSimplifiedMessages(messagesForAllChannels: PNFetchMessagesResult?) =
        messagesForAllChannels?.channels!!.flatMap { (channel: String, messages: List<PNFetchMessageItem>) ->
            messages.map { message ->
                SimplifiedMessageItem(channel, message.uuid, message.message)
            }
        }

    private fun getMessagesForMonitoredChannelAndUpdateTimestamp(channels: MutableSet<String>): PNFetchMessagesResult {
        val conversationHistory: PNFetchMessagesResult = pubNub.fetchMessages(
            channels = channels.toList(),
            // if you specify only the end parameter (without start), you will receive messages from that end timetoken and newer
            page = PNBoundedPage(end = timetoken)
        ).sync()
        // find the newest timetoken in all messages
        conversationHistory.channels.forEach { (_, messageList: List<PNFetchMessageItem>) ->
            messageList.forEach { message: PNFetchMessageItem ->
                message.timetoken?.let {
                    timetoken = it + 1
                }
            }
        }
        return conversationHistory
    }

    fun callChatGptApi(prompt: String, apiKey: String): List<Answer> {
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
            val answerListType = object : TypeToken<List<Answer>>() {}.type
            val answerList: List<Answer> = gson.fromJson(answerAsJson, answerListType)
            return answerList // we are expecting JSON here
        }
    }

    private fun callWebhook(response: List<Answer>) {
        for (answer in response) {
            if (answer.decision.equals("Yes", ignoreCase = true)) {
                val jsonBody = """
                {
                    "decision": ${answer.decision},
                    "probabilityInPercents": ${answer.probabilityInPercents},
                    "user": ${answer.user},
                    "channel": ${answer.channel},
                    "message": ${answer.message},
                    "conversationContext“: ${answer.conversationContext}
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
                    .url(webHookUrl!!)
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
        }
    }
}

data class SimplifiedMessageItem(
    val channel: String,
    val uuid: String?,
    val message: JsonElement
)

data class Answer(
    val decision: String, // yes or no
    val probabilityInPercents: Int,
    val user: String, // client-dda82791-eb4d-48ca-9fa4-cab910381221
    val channel: String,
    val message: String,
    val conversationContext: String,
)

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

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

data class Message(
    val role: String,
    val content: String
)
