package com.pubnub.api.legacy.endpoints.files

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.files.UploadFile
import com.pubnub.api.legacy.endpoints.files.TestsWithFiles.Companion.folder
import com.pubnub.api.models.server.files.FormField
import com.pubnub.api.services.S3Service
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import retrofit2.Call
import retrofit2.Response
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.Scanner

class UploadFileTest : TestsWithFiles {
    private val s3Service: S3Service = mockk {}

    @Test
    @Throws(PubNubException::class, IOException::class)
    fun keyIsFirstInMultipart() {
        // given
        val file = getTemporaryFile("file.txt")
        val captured = mutableListOf<MultipartBody>()
        FileInputStream(file).use { fileInputStream ->
            val uploadFile = UploadFile(
                s3Service,
                file!!.name,
                fileInputStream.readBytes(),
                null,
                FormField("key", "keyValue"), listOf(FormField("other", "otherValue")),
                "https://s3.aws.com/bucket"
            )
            every { s3Service.upload(any(), any()) } returns mockRetrofitSuccessfulCall {}

            // when
            uploadFile.sync()

            // then
            verify(exactly = 1) { s3Service.upload(any(), capture(captured)) }
        }

        val capturedBody: MultipartBody = captured[0]
        Assert.assertEquals(
            "form-data; name=\"key\"",
            capturedBody.part(0).headers()!!["Content-Disposition"]
        )
        assertPartExist("other", capturedBody.parts())
        assertPartExist("file", capturedBody.parts())
    }

    @Test
    @Throws(PubNubException::class, IOException::class)
    fun contentTypeIsUsedForFileIfPresentInFormFields() {
        // given
        val captured = mutableListOf<MultipartBody>()
        val file = getTemporaryFile("file.txt")
        val contentTypeValue = "application/json"
        FileInputStream(file).use { fileInputStream ->
            val uploadFile = UploadFile(
                s3Service,
                file!!.name,
                fileInputStream.readBytes(),
                null,
                FormField("key", "keyValue"), listOf(FormField("Content-Type", contentTypeValue)),
                "https://s3.aws.com/bucket"
            )
            every { s3Service.upload(any(), any()) } returns mockRetrofitSuccessfulCall { null }

            // when
            uploadFile.sync()

            // then
            verify(exactly = 1) { s3Service.upload(any(), capture(captured)) }
        }
        val capturedBody: MultipartBody = captured[0]
        assertPartExist("file", capturedBody.parts())
        val filePart = getPart("file", capturedBody.parts())
        Assert.assertEquals(MediaType.get(contentTypeValue), filePart!!.body().contentType())
    }

    @Test
    @Throws(PubNubException::class, IOException::class)
    fun defaultContentTypeIsUsedForFileIfNotPresentInFormFields() {
        // given
        val captured = mutableListOf<MultipartBody>()
        val file = getTemporaryFile("file.txt")
        FileInputStream(file).use { fileInputStream ->
            val uploadFile = UploadFile(
                s3Service,
                file!!.name,
                fileInputStream.readBytes(),
                null,
                FormField("key", "keyValue"), emptyList(),
                "https://s3.aws.com/bucket"
            )
            every { s3Service.upload(any(), any()) } returns mockRetrofitSuccessfulCall { null }

            // when
            uploadFile.sync()

            // then
            verify(exactly = 1) { s3Service.upload(any(), capture(captured)) }
        }
        val capturedBody: MultipartBody = captured[0]
        assertPartExist("file", capturedBody.parts())
        val filePart = getPart("file", capturedBody.parts())
        Assert.assertEquals(MediaType.get("application/octet-stream"), filePart!!.body().contentType())
    }

    @Test
    @Throws(IOException::class)
    fun errorMessageIsCopiedFromS3XMLResponse() {
        // given
        val file = getTemporaryFile("file.txt")
        FileInputStream(file).use { fileInputStream ->
            val uploadFile = UploadFile(
                s3Service,
                file!!.name,
                fileInputStream.readBytes(),
                null,
                FormField("key", "keyValue"), emptyList(),
                "https://s3.aws.com/bucket"
            )
            every { s3Service.upload(any(), any()) } returns mockRetrofitErrorCall {
                readToString(
                    UploadFileTest::class.java.getResourceAsStream("/entityTooLarge.xml")
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
    }

    private fun readToString(inputStream: InputStream): String {
        val s = Scanner(inputStream).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }

    private fun getPart(partName: String, parts: List<MultipartBody.Part>): MultipartBody.Part? {
        var result: MultipartBody.Part? = null
        for (part in parts) {
            if (part.headers()!!["Content-Disposition"]!!.contains(partName)) {
                result = part
                break
            }
        }
        return result
    }

    private fun assertPartExist(partName: String, parts: List<MultipartBody.Part>) {
        val result = getPart(partName, parts)
        if (result == null) {
            Assert.fail("There's no part $partName in parts $parts")
        }
    }

    @get:Rule
    override val temporaryFolder: TemporaryFolder
        get() = folder

    companion object {
        protected fun <T> mockRetrofitSuccessfulCall(block: () -> T?): Call<T> {
            val mockCall: Call<T> = mockk {}
            every { mockCall.execute() } returns Response.success(block())
            return mockCall
        }

        protected fun <T : String?, U> mockRetrofitErrorCall(block: () -> T): Call<U> {
            val mockCall: Call<U> = mockk {}
            every { mockCall.execute() } returns Response.error<U>(
                400,
                ResponseBody.create(MediaType.get("application/xml"), block())
            )
            return mockCall
        }
    }
}
