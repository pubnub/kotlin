package com.pubnub.api.endpoints.objects_api.utils;

import com.pubnub.api.endpoints.Endpoint;

import java.util.HashMap;
import java.util.Map;

public class Limiter implements ParameterEnricher {
    static final String LIMIT_PARAM_NAME = "limit";

    public interface LimitAware<T extends Endpoint<?, ?>> {
        T limit(int limit);
    }

    public interface HavingLimiter<T extends Endpoint<?, ?>> extends LimitAware<T>, HavingCompositeParameterEnricher {
        @Override
        default T limit(int limit) {
            getCompositeParameterEnricher().getLimiter().setLimit(limit);
            return (T) this;
        }
    }

    private Integer limit;

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public Map<String, String> enrichParameters(Map<String, String> baseParams) {
        final Map<String, String> enrichedMap = new HashMap<>(baseParams);
        if (limit != null) {
            enrichedMap.put(LIMIT_PARAM_NAME, limit.toString());
        }
        return enrichedMap;
    }
}
