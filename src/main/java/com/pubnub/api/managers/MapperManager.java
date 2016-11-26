package com.pubnub.api.managers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import lombok.Getter;
import retrofit2.Converter;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class MapperManager {

    @Getter
    private ObjectMapper objectMapper;
    @Getter
    private Converter.Factory converterFactory;

    public MapperManager() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.converterFactory = JacksonConverterFactory.create(this.getObjectMapper());
    }

    @SuppressWarnings("unchecked")
    public <T> T fromJson(String input, Class<T> clazz) throws PubNubException {
        try {
            return this.objectMapper.readValue(input, clazz);
        } catch (IOException e) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PARSING_ERROR).errormsg(e.getMessage()).build();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T convertValue(Object input, Class clazz) {
        return (T) this.objectMapper.convertValue(input, clazz);
    }

    public String toJson(Object input) throws PubNubException {
        try {
            return this.objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_JSON_ERROR).errormsg(e.getMessage()).build();
        }
    }


}
