package com.pubnub.api.managers.token_manager;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.pubnub.api.PubNubException;
import com.pubnub.api.models.consumer.access_manager.v3.PNToken;
import com.pubnub.api.vendor.Base64;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_INVALID_ACCESS_TOKEN;

public class TokenParser {
    private final ObjectMapper mapper = objectMapper();

    public PNToken unwrapToken(String token) throws PubNubException {
        try {
            byte[] byteArray = Base64.decode(token.getBytes(StandardCharsets.UTF_8), Base64.URL_SAFE);
            return mapper.readValue(byteArray, PNToken.class);
        } catch (IOException e) {
            throw PubNubException.builder()
                    .cause(e)
                    .pubnubError(PNERROBJ_INVALID_ACCESS_TOKEN)
                    .build();
        }
    }

    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper(new CBORFactory());
        objectMapper.configOverride(Map.class).setSetterInfo(JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
