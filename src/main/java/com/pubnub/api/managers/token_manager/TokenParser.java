package com.pubnub.api.managers.token_manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.pubnub.api.PubNubException;
import com.pubnub.api.models.consumer.access_manager.v3.PNToken;
import com.pubnub.api.vendor.Base64;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_INVALID_ACCESS_TOKEN;

public class TokenParser {
    private final ObjectMapper mapper = new ObjectMapper(new CBORFactory());

    public PNToken unwrapToken(String token) throws PubNubException {
        try {
            byte[] byteArray = Base64.decode(token.getBytes(StandardCharsets.UTF_8), Base64.URL_SAFE);
            return mapper.readValue(byteArray, PNToken.class);
        } catch (IOException e) {
            throw PubNubException.builder()
                    .pubnubError(PNERROBJ_INVALID_ACCESS_TOKEN)
                    .build();
        }
    }
}
