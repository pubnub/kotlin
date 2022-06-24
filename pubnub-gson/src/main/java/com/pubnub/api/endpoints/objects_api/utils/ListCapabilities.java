package com.pubnub.api.endpoints.objects_api.utils;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.objects_api.utils.Filter.FilterAware;
import com.pubnub.api.endpoints.objects_api.utils.Filter.HavingFilter;
import com.pubnub.api.endpoints.objects_api.utils.Limiter.HavingLimiter;
import com.pubnub.api.endpoints.objects_api.utils.Limiter.LimitAware;
import com.pubnub.api.endpoints.objects_api.utils.Pager.HavingPager;
import com.pubnub.api.endpoints.objects_api.utils.Pager.PagingAware;
import com.pubnub.api.endpoints.objects_api.utils.Sorter.HavingSorter;
import com.pubnub.api.endpoints.objects_api.utils.Sorter.SortingAware;
import com.pubnub.api.endpoints.objects_api.utils.TotalCounter.HavingTotalCounter;
import com.pubnub.api.endpoints.objects_api.utils.TotalCounter.TotalCountAware;

public interface ListCapabilities {
    interface ListCapabilitiesAware<T extends Endpoint<?, ?>>
            extends LimitAware<T>, TotalCountAware<T>, SortingAware<T>, PagingAware<T>, FilterAware<T> {
    }

    interface HavingListCapabilites<T extends Endpoint<?, ?>> extends HavingLimiter<T>,
            HavingTotalCounter<T>, HavingSorter<T>, HavingPager<T>, HavingFilter<T> {
    }

}
