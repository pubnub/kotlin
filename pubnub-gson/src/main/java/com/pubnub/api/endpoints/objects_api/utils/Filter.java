package com.pubnub.api.endpoints.objects_api.utils;

import com.pubnub.api.PubNubUtil;
import com.pubnub.api.Endpoint;

import java.util.HashMap;
import java.util.Map;

public class Filter implements ParameterEnricher {
    static final String FILTER_PARAM_NAME = "filter";

    public interface FilterAware<T extends Endpoint<?, ?>> {
        T filter(String filter);
    }

    public interface HavingFilter<T extends Endpoint<?, ?>> extends FilterAware<T>, HavingCompositeParameterEnricher {
        @Override
        default T filter(String filter) {
            getCompositeParameterEnricher().getFilter().setFilter(filter);
            return (T) this;
        }
    }

    private String filter;

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public Map<String, String> enrichParameters(Map<String, String> baseParams) {
        final Map<String, String> enrichedMap = new HashMap<>(baseParams);
        if (filter != null) {
            enrichedMap.put(FILTER_PARAM_NAME, PubNubUtil.urlEncode(filter));
        }
        return enrichedMap;
    }
}
