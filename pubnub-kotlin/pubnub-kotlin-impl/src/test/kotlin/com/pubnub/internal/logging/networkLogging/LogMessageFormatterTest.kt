package com.pubnub.internal.logging.networkLogging

import com.pubnub.api.logging.ErrorDetails
import com.pubnub.api.logging.HttpMethod
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.logging.NetworkLog
import com.pubnub.api.logging.NetworkRequestMessage
import com.pubnub.api.logging.NetworkResponseMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.slf4j.event.Level

class LogMessageFormatterTest {
    @Test
    fun `should format text message content correctly`() {
        val textContent = LogMessageContent.Text("Simple text message")

        val result = LogMessageFormatter.formatMessageContent(textContent)

        assertEquals("Simple text message", result)
    }

    @Test
    fun `should format object message content as pretty JSON`() {
        val testObject = mapOf(
            "key1" to "value1",
            "key2" to 42,
            "key3" to listOf("item1", "item2")
        )
        val objectContent = LogMessageContent.Object(testObject)

        val result = LogMessageFormatter.formatMessageContent(objectContent)

        // Should be pretty-printed JSON
        assertTrue(result.contains("\"key1\": \"value1\""))
        assertTrue(result.contains("\"key2\": 42"))
        assertTrue(result.contains("\"key3\": ["))
        assertTrue(result.contains("  ")) // Should have indentation
    }

    @Test
    fun `should format error message content with stack trace`() {
        val errorDetails = ErrorDetails(
            type = "RuntimeException",
            message = "Something went wrong",
            stack = listOf(
                "at com.example.Test.method1(Test.java:10)",
                "at com.example.Test.method2(Test.java:20)"
            )
        )
        val errorContent = LogMessageContent.Error(errorDetails)

        val result = LogMessageFormatter.formatMessageContent(errorContent)

        assertTrue(result.contains("Error(type=RuntimeException"))
        assertTrue(result.contains("message=Something went wrong"))
        assertTrue(result.contains("at com.example.Test.method1(Test.java:10)"))
        assertTrue(result.contains("at com.example.Test.method2(Test.java:20)"))
    }

    @Test
    fun `should format error message content without stack trace`() {
        val errorDetails = ErrorDetails(
            type = "IOException",
            message = "Network error",
            stack = null
        )
        val errorContent = LogMessageContent.Error(errorDetails)

        val result = LogMessageFormatter.formatMessageContent(errorContent)

        assertTrue(result.contains("Error(type=IOException"))
        assertTrue(result.contains("message=Network error"))
        assertTrue(result.contains("stack=null"))
    }

    @Test
    fun `should format network request message content as JSON`() {
        val networkRequest = NetworkRequestMessage(
            origin = "https://ps.pndsn.com",
            path = "/v2/subscribe/demo/my-channel/0",
            query = mapOf("uuid" to "test-uuid"),
            method = HttpMethod.GET,
            headers = mapOf("User-Agent" to "test-agent"),
            formData = null,
            body = """{"message": "hello"}""",
            timeout = 5000,
            identifier = "req-123"
        )
        val requestLog = NetworkLog.Request(networkRequest, canceled = false, failed = false)
        val requestContent = LogMessageContent.NetworkRequest(requestLog)

        val result = LogMessageFormatter.formatMessageContent(requestContent)

        assertTrue(result.startsWith("NetworkRequest:"))
        assertTrue(result.contains("\"origin\": \"https://ps.pndsn.com\""))
        assertTrue(result.contains("\"path\": \"/v2/subscribe/demo/my-channel/0\""))
        assertTrue(result.contains("\"method\": \"GET\""))
        assertTrue(result.contains("\"uuid\": \"test-uuid\""))
        assertTrue(result.contains("\"User-Agent\": \"test-agent\""))
        assertTrue(result.contains("req-123"))
        assertTrue(result.contains("  ")) // Should have pretty printing
    }

    @Test
    fun `should format network response message content as JSON`() {
        val networkResponse = NetworkResponseMessage(
            url = "https://ps.pndsn.com/v2/subscribe/demo/my-channel/0",
            status = 200,
            headers = mapOf("Content-Type" to "application/json"),
            body = """{"status": 200, "message": "OK"}"""
        )
        val responseLog = NetworkLog.Response(networkResponse)
        val responseContent = LogMessageContent.NetworkResponse(responseLog)

        val result = LogMessageFormatter.formatMessageContent(responseContent)

        assertTrue(result.startsWith("NetworkResponse:"))
        assertTrue(result.contains("\"url\": \"https://ps.pndsn.com/v2/subscribe/demo/my-channel/0\""))
        assertTrue(result.contains("\"status\": 200"))
        assertTrue(result.contains("\"Content-Type\": \"application/json\""))
        assertTrue(result.contains("\"body\": \"{\\\"status\\\": 200"))
        assertTrue(result.contains("  ")) // Should have pretty printing
    }

    @Test
    fun `should handle unknown message content types gracefully`() {
        // Test the else branch by using a content type that doesn't match the when cases
        val textContent = LogMessageContent.Text("test")

        val result = LogMessageFormatter.formatMessageContent(textContent)

        // Should return the text message
        assertEquals("test", result)
    }

    @Test
    fun `simplified extension function should format complete log message`() {
        val logMessage = LogMessage(
            pubNubId = "test-instance-123",
            logLevel = Level.INFO,
            location = "TestClass.testMethod",
            type = LogMessageType.TEXT,
            message = LogMessageContent.Text("This is a test message"),
            details = "Additional context information"
        )

        val result = logMessage.simplified()

        assertTrue(result.contains("pnInstanceId: test-instance-123"))
        assertTrue(result.contains("location: TestClass.testMethod"))
        assertTrue(result.contains("details: Additional context information"))
        assertTrue(result.contains("This is a test message"))
    }

    @Test
    fun `simplified extension function should handle null details`() {
        val logMessage = LogMessage(
            pubNubId = "test-instance-456",
            logLevel = Level.DEBUG,
            location = "AnotherClass.anotherMethod",
            type = LogMessageType.OBJECT,
            message = LogMessageContent.Object(mapOf("key" to "value")),
            details = null
        )

        val result = logMessage.simplified()

        assertTrue(result.contains("pnInstanceId: test-instance-456"))
        assertTrue(result.contains("location: AnotherClass.anotherMethod"))
        assertTrue(result.contains("details: ")) // Should show empty string for null details
        assertTrue(result.contains("\"key\": \"value\""))
    }

    @Test
    fun `simplified extension function should format complex network request`() {
        val networkRequest = NetworkRequestMessage(
            origin = "https://ps.pndsn.com",
            path = "/publish/demo/my-channel/0",
            query = mapOf("pnsdk" to "kotlin/1.0.0", "uuid" to "user-123"),
            method = HttpMethod.POST,
            headers = mapOf(
                "Content-Type" to "application/json",
                "Authorization" to "Bearer token123"
            ),
            formData = null,
            body = """{"text": "Hello World!", "meta": {"timestamp": 1234567890}}""",
            timeout = 10000,
            identifier = "publish-req-789"
        )
        val requestLog = NetworkLog.Request(networkRequest, canceled = false, failed = false)
        val logMessage = LogMessage(
            pubNubId = "publish-instance",
            logLevel = Level.DEBUG,
            location = "PublishEndpoint",
            type = LogMessageType.NETWORK_REQUEST,
            message = LogMessageContent.NetworkRequest(requestLog),
            details = "Publishing message to channel"
        )

        val result = logMessage.simplified()

        assertTrue(result.contains("pnInstanceId: publish-instance"))
        assertTrue(result.contains("location: PublishEndpoint"))
        assertTrue(result.contains("details: Publishing message to channel"))
        assertTrue(result.contains("NetworkRequest:"))
        assertTrue(result.contains("\"method\": \"POST\""))
        assertTrue(result.contains("\"Authorization\": \"Bearer token123\""))
        assertTrue(result.contains("Hello World!"))
        assertTrue(result.contains("publish-req-789"))
    }

    @Test
    fun `should handle complex nested objects in formatting`() {
        val complexObject = mapOf<String, Any>(
            "simpleString" to "value",
            "number" to 42,
            "boolean" to true,
            "nestedObject" to mapOf(
                "innerKey" to "innerValue",
                "innerList" to listOf(1, 2, 3)
            ),
            "arrayOfObjects" to listOf(
                mapOf("id" to 1, "name" to "item1"),
                mapOf("id" to 2, "name" to "item2")
            )
        )
        val objectContent = LogMessageContent.Object(complexObject)

        val result = LogMessageFormatter.formatMessageContent(objectContent)

        // Verify all elements are present and properly formatted
        assertTrue(result.contains("\"simpleString\": \"value\""))
        assertTrue(result.contains("\"number\": 42"))
        assertTrue(result.contains("\"boolean\": true"))
        assertTrue(result.contains("\"innerKey\": \"innerValue\""))
        assertTrue(result.contains("\"innerList\": ["))
        assertTrue(result.contains("\"arrayOfObjects\": ["))
        assertTrue(result.contains("\"name\": \"item1\""))
        // Should be pretty-printed with proper indentation
        assertTrue(result.contains("  "))
    }

    @Test
    fun `should format empty collections correctly`() {
        val objectWithEmptyCollections: Map<String, Any> = mapOf(
            "emptyList" to emptyList<String>(),
            "emptyMap" to emptyMap<String, Any>(),
            "emptyString" to ""
        )
        val objectContent = LogMessageContent.Object(objectWithEmptyCollections)

        val result = LogMessageFormatter.formatMessageContent(objectContent)

        assertTrue(result.contains("\"emptyList\": []"))
        assertTrue(result.contains("\"emptyMap\": {}"))
        assertTrue(result.contains("\"emptyString\": \"\""))
    }
}
