package com.pubnub.api.endpoints.objects_api.utils;

import com.pubnub.api.Endpoint;

import java.util.HashMap;
import java.util.Map;

public class TotalCounter implements ParameterEnricher {
    static final String COUNT_PARAM_NAME = "count";

    public interface TotalCountAware<T extends Endpoint<?, ?>> {
        T includeTotalCount(boolean includeTotalCount);
    }

    public interface HavingTotalCounter<T extends Endpoint<?, ?>> extends TotalCountAware<T>,
            HavingCompositeParameterEnricher {
        @Override
        default T includeTotalCount(boolean includeTotalCount) {
            getCompositeParameterEnricher().getTotalCounter().setIncludeTotalCount(includeTotalCount);
            return (T) this;
        }
    }

    private Boolean includeTotalCount;

    public void setIncludeTotalCount(boolean includeTotalCount) {
        this.includeTotalCount = includeTotalCount;
    }

    @Override
    public Map<String, String> enrichParameters(Map<String, String> baseParams) {
        final Map<String, String> enrichedMap = new HashMap<>(baseParams);
        if (includeTotalCount != null) {
            enrichedMap.put(COUNT_PARAM_NAME, includeTotalCount.toString());
        }
        return enrichedMap;
    }

}
