package com.pubnub.api.integration.util;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.pubsub.Publish;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import okhttp3.Request;

import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

    private Utils() {
    }

    public static String parseDate(Long timetoken) {
        return new SimpleDateFormat("HH:mm:ss:SSS").format(timetoken / 10_000L);
    }

    public static boolean isSorted(List<Long> list) {
        final List<Long> sorted = new ArrayList<Long>() {{
            addAll(list);
        }};
        Collections.sort(sorted);

        final List<Long> reversed = new ArrayList<Long>() {{
            addAll(list);
        }};
        Collections.sort(reversed);
        Collections.reverse(reversed);

        return list.equals(sorted) || list.equals(reversed);
    }

    public static PNPublishResult publish(PubNub pubnub, String channel, int indicator) {
        try {
            return pubnub.publish()
                    .channel(channel)
                    .message(indicator + "_" + randomChannel())
                    .meta("Metata_".concat(String.valueOf(indicator)))
                    .shouldStore(true)
                    .sync();
        } catch (PubNubException e) {
            return null;
        }
    }

    public static String random() {
        return RandomGenerator.newValue(10);
    }

    public static String randomChannel() {
        return "ch_".concat(RandomGenerator.newValue(10)).toLowerCase();
    }

    public static String queryParam(PNStatus pnStatus, String param) {
        final Request request = (Request) pnStatus.getClientRequest();
        return request.url().queryParameter(param);
    }

    public static List<PNPublishResult> publishMixed(PubNub pubnub, int count, String channel) {
        final List<PNPublishResult> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final Publish publishBuilder = pubnub.publish()
                    .channel(channel)
                    .message(String.valueOf(i).concat("_msg"))
                    .shouldStore(true);
            if (i % 2 == 0) {
                publishBuilder.meta(generateMap());
            } else if (i % 3 == 0) {
                publishBuilder.meta(RandomGenerator.newValue(4));
            } else {
                publishBuilder.meta(null);
            }

            PNPublishResult pnPublishResult = null;
            try {
                pnPublishResult = publishBuilder.sync();
            } catch (PubNubException e) {
                e.printStackTrace();
            }

            list.add(pnPublishResult);

        }
        return list;
    }

    public static Map<String, String> generateMap() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("text", RandomGenerator.newValue(8));
        map.put("uncd", RandomGenerator.unicode(6));
        map.put("info", RandomGenerator.newValue(8));
        return map;
    }
}
