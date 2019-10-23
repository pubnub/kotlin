package com.pubnub.api.managers.token_manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pubnub.api.PubNubException;
import com.pubnub.api.vendor.Base64;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_INVALID_ACCESS_TOKEN;
import static com.pubnub.api.managers.token_manager.PNMatchType.PATTERN;
import static com.pubnub.api.managers.token_manager.PNMatchType.RESOURCE;

public class TokenManager {

    private HashMap<String, HashMap<String, HashMap<String, String>>> map;

    public TokenManager() {
        initMap();
    }

    private synchronized void initMap() {
        PNResourceType[] resources = new PNResourceType[]{
                PNResourceType.USER,
                PNResourceType.SPACE
        };
        map = new HashMap<>();
        for (PNResourceType resource : resources) {
            HashMap<String, HashMap<String, String>> skeletonMap = new HashMap<>();
            skeletonMap.put(RESOURCE.toString(), new HashMap<>());
            skeletonMap.put(PATTERN.toString(), new HashMap<>());
            map.put(resource.toString(), skeletonMap);
        }
    }

    public synchronized void setToken(String token) throws PubNubException {
        JsonObject unwrappedToken = unwrapToken(token);
        storeToken(unwrappedToken, token);
    }

    public void setTokens(List<String> tokens) throws PubNubException {
        for (String token : tokens) {
            setToken(token);
        }
    }

    public synchronized String getToken(TokenManagerProperties tokenManagerProperties) {
        String resourceToken = getTokenByMatch(tokenManagerProperties, RESOURCE);
        if (resourceToken == null) {
            return getTokenByMatch(tokenManagerProperties, PATTERN);
        }
        return resourceToken;
    }

    public synchronized HashMap<String, HashMap<String, HashMap<String, String>>> getTokens() {
        return map;
    }

    public synchronized HashMap<String, HashMap<String, String>> getTokensByResource(PNResourceType resourceType) {
        return map.get(resourceType.toString());
    }

    private void storeToken(JsonObject jsonObject, String token) {
        PNMatchType[] matchTypes = new PNMatchType[]{RESOURCE, PATTERN};

        for (PNMatchType asset : matchTypes) {
            JsonObject resObject = jsonObject
                    .get(getShortenedMatchType(asset))
                    .getAsJsonObject();
            for (String rType : resObject.keySet()) {
                JsonObject singleResObject = resObject.get(rType).getAsJsonObject();
                for (String rName : singleResObject.keySet()) {
                    if (asset == PATTERN) {
                        map.get(getExtendedResourceType(rType))
                                .get(asset.toString())
                                .clear();
                    }
                    map.get(getExtendedResourceType(rType))
                            .get(asset.toString())
                            .put(rName, token);
                }
            }
        }
    }

    private JsonObject unwrapToken(String token) throws PubNubException {
        try {
            String raw = token;

            raw = raw.replace("_", "/").replace("-", "+");
            byte[] byteArray = Base64.decode(raw.getBytes("UTF-8"), 0);

            CBORFactory f = new CBORFactory();
            ObjectMapper mapper = new ObjectMapper(f);
            Object o = mapper.readValue(byteArray, Object.class);
            return new JsonParser().parse(new Gson().toJson(o)).getAsJsonObject();
        } catch (IOException e) {
            throw PubNubException.builder()
                    .pubnubError(PNERROBJ_INVALID_ACCESS_TOKEN)
                    .build();
        }
    }

    private String getTokenByMatch(TokenManagerProperties tokenManagerProperties, PNMatchType pnMatchType) {
        if (tokenManagerProperties == null || tokenManagerProperties.getPnResourceType() == null
                || tokenManagerProperties.getResourceId() == null) {
            return null;
        }
        if (pnMatchType != PATTERN) {
            String token = map.get(tokenManagerProperties.getPnResourceType().toString())
                    .get((pnMatchType.toString()))
                    .get(tokenManagerProperties.getResourceId());
            if (token != null) {
                return token;
            }
        } else {
            HashMap<String, String> stringTokenWrapperHashMap = map.get(
                    tokenManagerProperties.getPnResourceType().toString())
                    .get(pnMatchType.toString());
            if (!stringTokenWrapperHashMap.keySet().isEmpty()) {
                String firstKey = (String) stringTokenWrapperHashMap.keySet().toArray()[0];
                return stringTokenWrapperHashMap.get(firstKey);
            }
        }
        return null;
    }

    private String getExtendedResourceType(String resourceTypeAbbreviation) {
        switch (resourceTypeAbbreviation) {
            case "usr":
                return PNResourceType.USER.toString();
            case "spc":
                return PNResourceType.SPACE.toString();
            default:
        }
        return resourceTypeAbbreviation;
    }

    private String getShortenedMatchType(PNMatchType pnMatchType) {
        switch (pnMatchType) {
            case RESOURCE:
                return "res";
            case PATTERN:
                return "pat";
            default:
        }
        return pnMatchType.toString();
    }
}