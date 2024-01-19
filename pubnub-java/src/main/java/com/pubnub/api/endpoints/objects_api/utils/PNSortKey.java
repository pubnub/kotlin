package com.pubnub.api.endpoints.objects_api.utils;

import lombok.AccessLevel;
import lombok.Getter;

import static com.pubnub.api.endpoints.objects_api.utils.PNSortKey.Dir.ASC;
import static com.pubnub.api.endpoints.objects_api.utils.PNSortKey.Dir.DESC;

final public class PNSortKey {
    public enum Dir {
        ASC("asc"), DESC("desc");

        @Getter(AccessLevel.PACKAGE)
        private final String dir;

        Dir(String dir) {
            this.dir = dir;
        }
    }

    public enum Key {
        ID("id"), NAME("name"), UPDATED("updated");

        @Getter(AccessLevel.PACKAGE)
        private final String fieldName;

        Key(String fieldName) {
            this.fieldName = fieldName;
        }
    }

    @Getter
    private final Dir dir;

    @Getter
    private final Key key;

    private PNSortKey(Key key, Dir dir) {
        this.key = key;
        this.dir = dir;
    }

    public static PNSortKey of(Key key, Dir dir) {
        return new PNSortKey(key, dir);
    }

    public static PNSortKey of(Key key) {
        return new PNSortKey(key, ASC);
    }

    public static PNSortKey asc(Key key) {
        return new PNSortKey(key, ASC);
    }

    public static PNSortKey desc(Key key) {
        return new PNSortKey(key, DESC);
    }

    public String toSortParameter() {
        return key.fieldName + ":" + dir.dir;
    }

}
