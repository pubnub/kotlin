package com.pubnub.api.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class UnwrapSingleField<T> implements JsonDeserializer<T> {
    @Override
    public T deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        if (jsonObject.keySet().size() != 1) {
            throw new IllegalStateException("Couldn't unwrap field for object containing more than 1 field. Actual number of fields: " + jsonObject.keySet().size());
        }
        final String key = jsonObject.keySet().toArray(new String[]{})[0];
        final JsonElement element = jsonObject.get(key);
        return context.deserialize(element, typeOfT);
    }
}
