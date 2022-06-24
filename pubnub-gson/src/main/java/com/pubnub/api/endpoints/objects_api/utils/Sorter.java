package com.pubnub.api.endpoints.objects_api.utils;

import com.pubnub.api.PubNubUtil;
import com.pubnub.api.Endpoint;

import java.util.*;

public class Sorter implements ParameterEnricher {
    static final String SORT_PARAM_NAME = "sort";

    public interface SortingAware<T extends Endpoint<?, ?>> {
        T sort(PNSortKey... sortKeys);
    }

    public interface HavingSorter<T extends Endpoint<?, ?>> extends SortingAware<T>, HavingCompositeParameterEnricher {
        @Override
        default T sort(final PNSortKey... sortKeys) {
            getCompositeParameterEnricher().getSorter().addSortKeys(Arrays.asList(sortKeys));
            return (T) this;
        }
    }

    private List<PNSortKey> sortKeyList = new ArrayList<>();

    public void addSortKeys(final List<PNSortKey> sortKeys) {
        this.sortKeyList = sortKeys;
    }

    @Override
    public Map<String, String> enrichParameters(final Map<String, String> baseParams) {
        final Map<String, String> enrichedMap = new HashMap<>(baseParams);
        if (!sortKeyList.isEmpty()) {
            final List<String> sortKeys = new ArrayList<>();
            for (final PNSortKey sortKey : sortKeyList) {
                sortKeys.add(sortKey.toSortParameter());
            }
            final String sortKeysJoined = PubNubUtil.joinString(sortKeys, ",");
            enrichedMap.put(SORT_PARAM_NAME, sortKeysJoined);
        }
        return enrichedMap;
    }
}
