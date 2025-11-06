package com.pubnub.internal.endpoints.files

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.files.PNFile
import com.pubnub.internal.models.server.files.FileUploadRequestDetails
import com.pubnub.internal.models.server.files.FormField
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertSame
import org.junit.Test

/**
 * Unit tests for S3 XML error parsing in UploadFileEndpoint.
 * These tests directly call parseS3XmlError() to verify proper error handling.
 */
class UploadFileEndpointTest : BaseTest() {
    private val testFileName = "test.txt"
    private val testContent = "Hello World".toByteArray()

    private data class TestPNFile(
        override val id: String,
        override val name: String
    ) : PNFile

    private fun createUploadEndpoint(): UploadFileEndpoint {
        val fileUploadDetails = FileUploadRequestDetails(
            status = 200,
            data = TestPNFile("file-id", testFileName),
            url = "https://s3.amazonaws.com/bucket/path",
            method = "POST",
            expirationDate = "2025-12-31T23:59:59Z",
            keyFormField = FormField("key", "test-key"),
            formFields = listOf(
                FormField("Content-Type", "text/plain"),
                FormField("acl", "private")
            )
        )

        return UploadFileEndpoint.Factory(pubnub).create(
            fileName = testFileName,
            content = testContent,
            fileUploadRequestDetails = fileUploadDetails
        )
    }

    @Test
    fun testEntityTooLargeError_withSizeDetails() {
        val s3ErrorResponse = """
            <?xml version="1.0" encoding="UTF-8"?>
            <Error>
                <Code>EntityTooLarge</Code>
                <Message>Your proposed upload exceeds the maximum allowed size</Message>
                <ProposedSize>5282</ProposedSize>
                <MaxSizeAllowed>5120</MaxSizeAllowed>
                <RequestId>ES1J0M4J8Z1K9R1T</RequestId>
            </Error>
        """.trimIndent()

        val originalException = PubNubException(PubNubError.HTTP_ERROR).copy(errorMessage = s3ErrorResponse)
        val endpoint = createUploadEndpoint()

        try {
            endpoint.parseS3XmlError(originalException)
            throw AssertionError("Expected PubNubException to be thrown")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.FILE_TOO_LARGE, e.pubnubError)
            assertEquals(
                "File size (5282 bytes) exceeds maximum allowed size (5120 bytes)",
                e.errorMessage
            )
        }
    }

    @Test
    fun testEntityTooLargeError_withoutSizeDetails() {
        val s3ErrorResponse = """
            <?xml version="1.0" encoding="UTF-8"?>
            <Error>
                <Code>EntityTooLarge</Code>
                <Message>Your proposed upload exceeds the maximum allowed size</Message>
                <RequestId>ES1J0M4J8Z1K9R1T</RequestId>
            </Error>
        """.trimIndent()

        val originalException = PubNubException(PubNubError.HTTP_ERROR).copy(errorMessage = s3ErrorResponse)
        val endpoint = createUploadEndpoint()

        try {
            endpoint.parseS3XmlError(originalException)
            throw AssertionError("Expected PubNubException to be thrown")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.FILE_TOO_LARGE, e.pubnubError)
            assertEquals(
                "File size exceeds maximum allowed size",
                e.errorMessage
            )
        }
    }

    @Test
    fun testAccessDeniedError_policyExpired() {
        val s3ErrorResponse = """
            <?xml version="1.0" encoding="UTF-8"?>
            <Error>
                <Code>AccessDenied</Code>
                <Message>Invalid according to Policy: Policy expired.</Message>
                <RequestId>A4K52GQJD3EJEJAS</RequestId>
            </Error>
        """.trimIndent()

        val originalException = PubNubException(PubNubError.HTTP_ERROR).copy(errorMessage = s3ErrorResponse)
        val endpoint = createUploadEndpoint()

        try {
            endpoint.parseS3XmlError(originalException)
            throw AssertionError("Expected PubNubException to be thrown")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.UPLOAD_URL_HAS_EXPIRED, e.pubnubError)
        }
    }

    @Test
    fun testAccessDeniedError_otherAccessDenied() {
        val s3ErrorResponse = """
            <?xml version="1.0" encoding="UTF-8"?>
            <Error>
                <Code>AccessDenied</Code>
                <Message>Access Denied</Message>
                <RequestId>A4K52GQJD3EJEJAS</RequestId>
            </Error>
        """.trimIndent()

        val originalException = PubNubException(PubNubError.HTTP_ERROR).copy(errorMessage = s3ErrorResponse)
        val endpoint = createUploadEndpoint()

        val result = endpoint.parseS3XmlError(originalException)

        // Should NOT be S3_PRE_SIGNED_URL_HAS_EXPIRED since message doesn't contain "Policy expired"
        assertNotEquals(PubNubError.UPLOAD_URL_HAS_EXPIRED, result.pubnubError)
        assertEquals("Access Denied", result.errorMessage)
    }

    @Test
    fun testOtherS3Error_invalidRequest() {
        val s3ErrorResponse = """
            <?xml version="1.0" encoding="UTF-8"?>
            <Error>
                <Code>InvalidRequest</Code>
                <Message>The request is not valid</Message>
                <RequestId>A4K52GQJD3EJEJAS</RequestId>
            </Error>
        """.trimIndent()

        val originalException = PubNubException(PubNubError.HTTP_ERROR).copy(errorMessage = s3ErrorResponse)
        val endpoint = createUploadEndpoint()

        val result = endpoint.parseS3XmlError(originalException)

        assertEquals("The request is not valid", result.errorMessage)
    }

    @Test
    fun testNonXmlError_returnedAsIs() {
        val plainTextError = "Internal Server Error"

        val originalException = PubNubException(PubNubError.HTTP_ERROR).copy(errorMessage = plainTextError)
        val endpoint = createUploadEndpoint()

        val result = endpoint.parseS3XmlError(originalException)

        // Should not be parsed as XML, return original exception
        assertSame(originalException, result)
        assertEquals(plainTextError, result.errorMessage)
    }

    @Test
    fun testNullErrorMessage_returnedAsIs() {
        val originalException = PubNubException(PubNubError.HTTP_ERROR).copy(errorMessage = null)
        val endpoint = createUploadEndpoint()

        val result = endpoint.parseS3XmlError(originalException)

        // Should return original exception unchanged
        assertSame(originalException, result)
    }

    @Test
    fun testMalformedXmlError_returnedAsIs() {
        val malformedXml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <Error>
                <Code>AccessDenied
                <Message>Invalid XML
            </Error
        """.trimIndent()

        val originalException = PubNubException(PubNubError.HTTP_ERROR).copy(errorMessage = malformedXml)
        val endpoint = createUploadEndpoint()

        val result = endpoint.parseS3XmlError(originalException)

        // Malformed XML should return original exception
        assertSame(originalException, result)
        assertEquals(malformedXml, result.errorMessage)
    }

    @Test
    fun testS3ErrorWithoutCode_cleansMessage() {
        val s3ErrorResponse = """
            <?xml version="1.0" encoding="UTF-8"?>
            <Error>
                <Message>Some generic error message</Message>
                <RequestId>A4K52GQJD3EJEJAS</RequestId>
            </Error>
        """.trimIndent()

        val originalException = PubNubException(PubNubError.HTTP_ERROR).copy(errorMessage = s3ErrorResponse)
        val endpoint = createUploadEndpoint()

        val result = endpoint.parseS3XmlError(originalException)

        // Should extract and return the clean message
        assertEquals("Some generic error message", result.errorMessage)
    }

    @Test
    fun testAccessDenied_policyExpiredCaseInsensitive() {
        val s3ErrorResponse = """
            <?xml version="1.0" encoding="UTF-8"?>
            <Error>
                <Code>AccessDenied</Code>
                <Message>Invalid according to POLICY EXPIRED check</Message>
            </Error>
        """.trimIndent()

        val originalException = PubNubException(PubNubError.HTTP_ERROR).copy(errorMessage = s3ErrorResponse)
        val endpoint = createUploadEndpoint()

        try {
            endpoint.parseS3XmlError(originalException)
            throw AssertionError("Expected PubNubException to be thrown")
        } catch (e: PubNubException) {
            // Should match "Policy expired" case-insensitively
            assertEquals(PubNubError.UPLOAD_URL_HAS_EXPIRED, e.pubnubError)
        }
    }
}
