package com.pubnub.docs.fileSharing
// https://www.pubnub.com/docs/sdks/kotlin/api-reference/files#basic-usage

// snippet.filesBasicUsage
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.v2.PNConfiguration
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * This example demonstrates how to use the sendFile method in PubNub Kotlin SDK.
 *
 * The sendFile method allows you to upload files (up to 5MB) to a PubNub channel
 * where they can be shared with subscribers.
 */
fun main() {
    println("PubNub sendFile() Example")
    println("========================")

    // 1. Configure PubNub
    val userId = UserId("file-upload-demo-user")
    val config = PNConfiguration.builder(userId, "demo").apply {
        publishKey = "demo"
        logVerbosity = PNLogVerbosity.BODY // Enable debug logging of network calls
    }.build()

    // 2. Create PubNub instance
    val pubnub = PubNub.create(config)

    try {
        // 3. First, create a temporary test file (in a real app, you'd use an existing file)
        println("Creating a temporary test file...")
        val tempFile = File.createTempFile("pubnub_test_", ".txt")
        tempFile.deleteOnExit() // Clean up when JVM exits

        // 4. Write some content to the test file
        FileOutputStream(tempFile).use { output ->
            output.write("This is a test file for PubNub file upload demo.".toByteArray())
        }

        println("Temporary file created at: ${tempFile.absolutePath}")

        // 5. Create file input stream
        val inputStream = FileInputStream(tempFile)

        println("Uploading file to PubNub...")

        // 6. Send the file to PubNub
        pubnub.sendFile(
            channel = "demo-file-channel",     // The channel to upload the file to
            fileName = "test_document.txt",    // The name to give the file when uploaded
            inputStream = inputStream,         // The file data to upload
            message = mapOf(                   // Optional message to accompany the file
                "text" to "Here's a test document!",
                "type" to "document"
            ),
            customMessageType = "file-message", // Optional custom message type
            meta = mapOf(                      // Optional metadata for filtering
                "sender" to "demo-user",
                "app" to "kotlin-example"
            )
            // Additional optional parameters:
            // ttl = 24*60*60,                 // Time-to-live in seconds (24 hours)
            // shouldStore = true,             // Whether to store in history (default true)
        ).async { result ->
            result.onSuccess { response ->
                println("\nFile upload successful!")
                println("  • Timetoken: ${response.timetoken}")
                println("  • File ID: ${response.file.id}")
                println("  • File Name: ${response.file.name}")

                // 7. Get a URL for downloading the file
                pubnub.getFileUrl(
                    channel = "demo-file-channel",
                    fileName = response.file.name,
                    fileId = response.file.id
                ).async { urlResult ->
                    urlResult.onSuccess { urlResponse ->
                        println("  • Download URL: ${urlResponse.url}")

                        // 8. Clean up resources
                        inputStream.close()
                        pubnub.destroy() // Clean up PubNub resources
                    }.onFailure { exception ->
                        println("Error getting file URL: ${exception.message}")
                        inputStream.close()
                        pubnub.destroy()
                    }
                }
            }.onFailure { exception ->
                println("Error uploading file: ${exception.message}")
                inputStream.close()
                pubnub.destroy()
            }
        }

        println("\nFile upload initiated. Check console for results.")
        println("The async results will appear above this message.")

        // Keep the program running to allow async operations to complete
        Thread.sleep(5000)
    } catch (e: Exception) {
        println("Error: ${e.message}")
        e.printStackTrace()
    }
}
// snippet.end
