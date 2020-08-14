package com.pubnub.api.endpoints.files;

import com.pubnub.api.PubNubException;
import com.pubnub.api.models.server.files.FormField;
import com.pubnub.api.services.S3Service;
import okhttp3.MultipartBody;
import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;
import retrofit2.Call;
import retrofit2.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UploadFileTest implements TestsWithFiles {
    private final S3Service s3Service = mock(S3Service.class);
    private final ArgumentCaptor<MultipartBody> requestBodyArgumentCaptor = ArgumentCaptor.forClass(MultipartBody.class);

    @NotNull
    protected static <T> Answer<Call<T>> mockRetrofitSuccessfulCall(final Supplier<T> block) {
        return invocation -> {
            final Call<T> mockCall = mock(Call.class);
            when(mockCall.execute()).thenAnswer(blockInvocation -> Response.success(block.get()));
            return mockCall;
        };
    }

    @Test
    public void keyIsFirstInMultipart() throws PubNubException, IOException {
        //given
        File file = getTemporaryFile("file.txt");
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            UploadFile uploadFile = new UploadFile(s3Service,
                    file.getName(),
                    fileInputStream,
                    null,

                    new FormField("key", "keyValue"),
                    Collections.singletonList(new FormField("other", "otherValue")),
                    "https://s3.aws.com/bucket"
            );

            when(s3Service.upload(any(), any())).then(mockRetrofitSuccessfulCall(() -> null));

            //when
            uploadFile.sync();

            //then
            verify(s3Service, times(1)).upload(any(), requestBodyArgumentCaptor.capture());
        }

        MultipartBody capturedBody = requestBodyArgumentCaptor.getValue();

        assertEquals("form-data; name=\"key\"", capturedBody.part(0).headers().get("Content-Disposition"));
        assertPartExist("other", capturedBody.parts());
        assertPartExist("file", capturedBody.parts());

    }

    private void assertPartExist(String partName, List<MultipartBody.Part> parts) {
        MultipartBody.Part result = null;
        for (MultipartBody.Part part : parts) {
            if (part.headers().get("Content-Disposition").contains(partName)) {
                result = part;
            }
        }
        if (result == null) {
            fail("There's no part " + partName + " in parts " + parts);
        }
    }

    @Override
    @Rule
    public TemporaryFolder getTemporaryFolder() {
        return folder;
    }
}
