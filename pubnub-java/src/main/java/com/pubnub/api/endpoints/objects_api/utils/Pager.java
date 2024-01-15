package com.pubnub.api.endpoints.objects_api.utils;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.PNPage;

import java.util.HashMap;
import java.util.Map;

public class Pager implements ParameterEnricher {
    static final String START_PARAM_NAME = "start";
    static final String END_PARAM_NAME = "end";

    public interface PagingAware<T extends Endpoint<?, ?>> {
        T page(PNPage page);
    }

    public interface HavingPager<T extends Endpoint<?, ?>> extends PagingAware<T>, HavingCompositeParameterEnricher {
        @Override
        default T page(PNPage page) {
            getCompositeParameterEnricher().getPager().setPage(page);
            return (T) this;
        }
    }

    private PNPage page;

    public void setPage(final PNPage page) {
        this.page = page;
    }

    @Override
    public Map<String, String> enrichParameters(Map<String, String> baseParams) {
        final Map<String, String> enrichedMap = new HashMap<>(baseParams);
        if (page != null) {
            if (page instanceof PNPage.Next) {
                enrichedMap.put(START_PARAM_NAME, page.getHash());
            }
            if (page instanceof PNPage.Previous) {
                enrichedMap.put(END_PARAM_NAME, page.getHash());
            }
        }
        return enrichedMap;
    }

    @Override
    public void validateParameters() throws PubNubException {
        if (page != null) {
            if (page.getHash() == null || page.getHash().isEmpty()) {
                if (page instanceof PNPage.Next) {
                    throw PubNubException.builder()
                            .pubnubError(PubNubErrorBuilder.PNERROBJ_PAGINATION_NEXT_OUT_OF_BOUNDS)
                            .build();
                }
                if (page instanceof PNPage.Previous) {
                    throw PubNubException.builder()
                            .pubnubError(PubNubErrorBuilder.PNERROBJ_PAGINATION_PREV_OUT_OF_BOUNDS)
                            .build();
                }
            }
        }
    }
}
