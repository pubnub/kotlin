package com.pubnub.api.endpoints.objects_api;

import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.objects_api.utils.*;
import lombok.Getter;

import java.util.*;

public class CompositeParameterEnricher implements ParameterEnricher {
    @Getter
    private final Include include;
    @Getter private final Sorter sorter;
    @Getter private final Pager pager;
    @Getter private final Filter filter;
    @Getter private final TotalCounter totalCounter;
    @Getter private final Limiter limiter;

    public static CompositeParameterEnricher createDefault() {
        final Include include = new Include();
        final Sorter sorter = new Sorter();
        final Pager pager = new Pager();
        final Filter filter = new Filter();
        final Limiter limiter = new Limiter();
        final TotalCounter totalCounter = new TotalCounter();

        return new CompositeParameterEnricher(include, sorter, pager, filter, totalCounter, limiter);
    }

    public CompositeParameterEnricher(final Include include,
                                      final Sorter sorter,
                                      final Pager pager,
                                      final Filter filter,
                                      final TotalCounter totalCounter,
                                      final Limiter limiter) {
        this.include = include;
        this.sorter = sorter;
        this.pager = pager;
        this.filter = filter;
        this.totalCounter = totalCounter;
        this.limiter = limiter;
    }



    @Override
    public Map<String, String> enrichParameters(final Map<String, String> baseParams) {
        Map<String, String> enrichedMap = new HashMap<>(baseParams);

        final List<ParameterEnricher> parameterEnrichers = new ArrayList<>();
        for (final ParameterEnricher enricher : Arrays.asList(include, sorter, pager, filter, totalCounter, limiter)) {
            if (enricher != null) {
                parameterEnrichers.add(enricher);
            }
        }

        for (final ParameterEnricher parameterEnricher : parameterEnrichers) {
            enrichedMap = parameterEnricher.enrichParameters(enrichedMap);
        }

        return enrichedMap;
    }

    @Override
    public void validateParameters() throws PubNubException {
        for (final ParameterEnricher parameterEnricher
                : Arrays.asList(include, sorter, pager, filter, totalCounter, limiter)) {
            parameterEnricher.validateParameters();
        }
    }
}
