package com.pubnub.api.models.consumer.push.payload;

import com.pubnub.api.enums.PNPushEnvironment;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Accessors(chain = true)
public class PushPayloadHelper {

    private Map<String, Object> commonPayload;
    private FCMPayload fcmPayload;
    private MPNSPayload mpnsPayload;
    private APNSPayload apnsPayload;

    public Map<String, Object> build() {
        Map<String, Object> payload = new HashMap<>();

        if (apnsPayload != null) {
            Map<String, Object> apnsMap = apnsPayload.toMap();
            if (!apnsMap.isEmpty()) {
                payload.put("pn_apns", apnsMap);
            }
        }

        if (fcmPayload != null) {
            Map<String, Object> fcmMap = fcmPayload.toMap();
            if (!fcmMap.isEmpty()) {
                payload.put("pn_gcm", fcmMap);
            }
        }

        if (mpnsPayload != null) {
            Map<String, Object> mpnsMap = mpnsPayload.toMap();
            if (!mpnsMap.isEmpty()) {
                payload.put("pn_mpns", mpnsMap);
            }
        }

        payload.putAll(filterNonNullEntries(commonPayload));

        return payload;
    }

    @Setter
    @Accessors(chain = true)
    public static class APNSPayload implements PushPayloadSerializer {
        private APS aps;
        private List<APNS2Configuration> apns2Configurations;
        private Map<String, Object> custom;

        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (aps != null) {
                Map<String, Object> apsMap = aps.toMap();
                if (apsMap != null && !apsMap.isEmpty()) {
                    map.put("aps", apsMap);
                }
            }
            if (apns2Configurations != null) {
                List<Map<String, Object>> pnPushArray = new ArrayList<>();
                for (APNS2Configuration configuration : apns2Configurations) {
                    Map<String, Object> pushItemMap = configuration.toMap();
                    if (pushItemMap != null && !pushItemMap.isEmpty()) {
                        pnPushArray.add(pushItemMap);
                    }
                }
                map.put("pn_push", pnPushArray);
            }
            map.putAll(filterNonNullEntries(custom));
            return map;
        }

        @Setter
        @Accessors(chain = true)
        public static class APS implements PushPayloadSerializer {
            private Object alert;
            private Integer badge;
            private String sound;

            @Override
            public Map<String, Object> toMap() {
                Map<String, Object> map = new HashMap<>();
                if (alert != null) {
                    map.put("alert", alert);
                }
                if (badge != null) {
                    map.put("badge", badge);
                }
                if (sound != null) {
                    map.put("sound", sound);
                }
                return map;
            }
        }

        @Setter
        @Accessors(chain = true)
        public static class APNS2Configuration implements PushPayloadSerializer {
            private String collapseId;
            private String expiration;
            private List<Target> targets;
            private String version;

            @Override
            public Map<String, Object> toMap() {
                Map<String, Object> map = new HashMap<>();

                if (collapseId != null) {
                    map.put("collapse_id", collapseId);
                }
                if (expiration != null) {
                    map.put("expiration", expiration);
                }

                if (targets != null && !targets.isEmpty()) {
                    List<Map<String, Object>> targetsList = new ArrayList<>();
                    for (Target target : targets) {
                        Map<String, Object> targetMap = target.toMap();
                        if (targetMap != null && !targetMap.isEmpty()) {
                            targetsList.add(targetMap);
                        }
                    }
                    map.put("targets", targetsList);
                }

                if (version != null) {
                    map.put("version", version);
                }

                return map;
            }

            @Setter
            @Accessors(chain = true)
            public static class Target implements PushPayloadSerializer {
                private String topic;
                private List<String> excludeDevices;
                private PNPushEnvironment environment;

                @Override
                public Map<String, Object> toMap() {
                    Map<String, Object> map = new HashMap<>();
                    if (topic != null) {
                        map.put("topic", topic);
                    }
                    if (excludeDevices != null && !excludeDevices.isEmpty()) {
                        map.put("excluded_devices", excludeDevices);
                    }
                    if (environment != null) {
                        map.put("environment", environment.name().toLowerCase());
                    }
                    return map;
                }
            }
        }
    }

    // MPNS

    @Setter
    @Accessors(chain = true)
    public static class MPNSPayload implements PushPayloadSerializer {
        private Integer count;
        private String backTitle;
        private String title;
        private String backContent;
        private String type;
        private Map<String, Object> custom;

        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (count != null) {
                map.put("count", count);
            }
            if (backTitle != null) {
                map.put("back_title", backTitle);
            }
            if (title != null) {
                map.put("title", title);
            }
            if (backContent != null) {
                map.put("back_content", backContent);
            }
            if (type != null) {
                map.put("type", type);
            }
            map.putAll(filterNonNullEntries(custom));
            return map;
        }
    }

    // FCM

    @Setter
    @Accessors(chain = true)
    public static class FCMPayload implements PushPayloadSerializer {
        private Map<String, Object> custom;
        private Map<String, Object> data;
        private Notification notification;

        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (notification != null) {
                Map<String, Object> notificationMap = notification.toMap();
                if (notificationMap != null && !notificationMap.isEmpty()) {
                    map.put("notification", notification.toMap());
                }
            }
            if (data != null && !data.isEmpty()) {
                map.put("data", data);
            }
            map.putAll(filterNonNullEntries(custom));
            return map;
        }

        @Setter
        @Accessors(chain = true)
        public static class Notification implements PushPayloadSerializer {
            private String title;
            private String body;
            private String image;

            @Override
            public Map<String, Object> toMap() {
                Map<String, Object> map = new HashMap<>();
                if (title != null) {
                    map.put("title", title);
                }
                if (body != null) {
                    map.put("body", body);
                }
                if (image != null) {
                    map.put("image", image);
                }
                return map;
            }
        }
    }


    // todo mapof

    private static Map<String, Object> filterNonNullEntries(Map<String, Object> targetMap) {
        if (targetMap == null) {
            return new HashMap<>();
        }

        Map<String, Object> map = new HashMap<>();

        for (Map.Entry<String, Object> entry : targetMap.entrySet()) {
            if (entry.getValue() != null) {
                map.put(entry.getKey(), entry.getValue());
            }
        }

        return map;
    }

}
