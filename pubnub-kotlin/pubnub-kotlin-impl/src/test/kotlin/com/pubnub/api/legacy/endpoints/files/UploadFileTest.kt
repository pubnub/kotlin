package com.pubnub.api.legacy.endpoints.files

import com.pubnub.api.PubNubException
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.internal.endpoints.files.UploadFileEndpoint
import com.pubnub.internal.models.server.files.FormField
import com.pubnub.internal.services.S3Service
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Response
import java.io.InputStream
import java.util.Scanner
import org.hamcrest.Matchers.`is` as iz

class UploadFileTest : TestsWithFiles {
    private val s3Service: S3Service = mockk {}

    @Test
    fun keyIsFirstInMultipart() {
        // given
        val captured = mutableListOf<MultipartBody>()
        val uploadFile =
            UploadFileEndpoint(
                s3Service,
                fileName(),
                byteArrayOf(),
                FormField("key", "keyValue"),
                listOf(FormField("other", "otherValue")),
                "https://s3.aws.com/bucket",
            )
        every { s3Service.upload(any(), any()) } returns mockRetrofitSuccessfulCall {}

        // when
        uploadFile.sync()

        // then
        verify(exactly = 1) { s3Service.upload(any(), capture(captured)) }

        val capturedBody: MultipartBody = captured[0]
        Assert.assertEquals(
            "form-data; name=\"key\"",
            capturedBody.part(0).headers!!["Content-Disposition"],
        )
        assertPartExist("other", capturedBody.parts)
        assertPartExist("file", capturedBody.parts)
    }

    @Test
    fun contentTypeIsUsedForFileIfPresentInFormFields() {
        // given
        val captured = mutableListOf<MultipartBody>()
        val contentTypeValue = "application/json"
        val uploadFile =
            UploadFileEndpoint(
                s3Service,
                fileName(),
                byteArrayOf(),
                FormField("key", "keyValue"),
                listOf(FormField("Content-Type", contentTypeValue)),
                "https://s3.aws.com/bucket",
            )
        every { s3Service.upload(any(), any()) } returns mockRetrofitSuccessfulCall { null }

        // when
        uploadFile.sync()

        // then
        verify(exactly = 1) { s3Service.upload(any(), capture(captured)) }
        val capturedBody: MultipartBody = captured[0]
        assertPartExist("file", capturedBody.parts)
        val filePart = getPart("file", capturedBody.parts)
        Assert.assertEquals(contentTypeValue.toMediaType(), filePart!!.body.contentType())
    }

    @Test
    fun defaultContentTypeIsUsedForFileIfNotPresentInFormFields() {
        // given
        val captured = mutableListOf<MultipartBody>()
        val uploadFile =
            UploadFileEndpoint(
                s3Service,
                fileName(),
                byteArrayOf(),
                FormField("key", "keyValue"),
                emptyList(),
                "https://s3.aws.com/bucket",
            )
        every { s3Service.upload(any(), any()) } returns mockRetrofitSuccessfulCall { null }

        // when
        uploadFile.sync()

        // then
        verify(exactly = 1) { s3Service.upload(any(), capture(captured)) }
        val capturedBody: MultipartBody = captured[0]
        assertPartExist("file", capturedBody.parts)
        val filePart = getPart("file", capturedBody.parts)
        Assert.assertEquals("application/octet-stream".toMediaType(), filePart!!.body.contentType())
    }

    @Test
    fun testEncryptedFileContentIsSendProperly() {
        // given
        val captured = mutableListOf<MultipartBody>()
        val content = "content"
        val cipher = "enigma"
        val cryptoModule = CryptoModule.createLegacyCryptoModule(cipher, true)
        val encrypted = cryptoModule.encryptStream(content.byteInputStream())
        val uploadFile =
            UploadFileEndpoint(
                s3Service,
                fileName(),
                encrypted.readBytes(),
                FormField("key", "keyValue"),
                emptyList(),
                "https://s3.aws.com/bucket",
            )
        every { s3Service.upload(any(), any()) } returns mockRetrofitSuccessfulCall { null }

        // when
        uploadFile.sync()

        // then
        verify(exactly = 1) { s3Service.upload(any(), capture(captured)) }
        val capturedBody: MultipartBody = captured[0]
        assertPartExist("file", capturedBody.parts)
        val filePart = getPart("file", capturedBody.parts)
        val buffer = Buffer()
        filePart?.body?.writeTo(buffer)

        val decrypted = cryptoModule.decryptStream(buffer.readByteArray().inputStream()).readBytes()
        assertThat(String(decrypted), iz(content))
        assertEquals(content, String(decrypted))
    }

    @Test
    fun errorMessageIsCopiedFromS3XMLResponse() {
        // given
        val uploadFile =
            UploadFileEndpoint(
                s3Service,
                fileName(),
                byteArrayOf(),
                FormField("key", "keyValue"),
                emptyList(),
                "https://s3.aws.com/bucket",
            )
        every { s3Service.upload(any(), any()) } returns
            mockRetrofitErrorCall {
                readToString(
                    UploadFileTest::class.java.getResourceAsStream("/entityTooLarge.xml")!!,
                )
            }

        // when
        try {
            uploadFile.sync()
            Assert.fail("Exception expected")
        } catch (ex: PubNubException) {
            // then
            Assert.assertEquals("Your proposed upload exceeds the maximum allowed size", ex.errorMessage)
        }
    }

    private fun readToString(inputStream: InputStream): String {
        val s = Scanner(inputStream).useDelimiter("\\A")
        return if (s.hasNext()) {
            s.next()
        } else {
            ""
        }
    }

    private fun getPart(
        partName: String,
        parts: List<MultipartBody.Part>,
    ): MultipartBody.Part? {
        var result: MultipartBody.Part? = null
        for (part in parts) {
            if (part.headers!!["Content-Disposition"]!!.contains(partName)) {
                result = part
                break
            }
        }
        return result
    }

    private fun assertPartExist(
        partName: String,
        parts: List<MultipartBody.Part>,
    ) {
        val result = getPart(partName, parts)
        if (result == null) {
            Assert.fail("There's no part $partName in parts $parts")
        }
    }

    companion object {
        protected fun <T> mockRetrofitSuccessfulCall(block: () -> T?): Call<T> {
            val mockCall: Call<T> = mockk {}
            every { mockCall.execute() } returns Response.success(block())
            return mockCall
        }

        protected fun <U> mockRetrofitErrorCall(block: () -> String): Call<U> {
            val mockCall: Call<U> = mockk {}
            every { mockCall.execute() } returns
                Response.error<U>(
                    400,
                    block().toResponseBody("application/xml".toMediaType()),
                )
            return mockCall
        }
    }
}
