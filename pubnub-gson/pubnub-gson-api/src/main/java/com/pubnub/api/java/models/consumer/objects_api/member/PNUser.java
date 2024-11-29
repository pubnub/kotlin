package com.pubnub.api.java.models.consumer.objects_api.member;

public class PNUser {
    private final String userId;
    private String status;
    private String type;
    private Object custom;

    public static Builder builder(String userId) {
        return new PNUser.Builder(userId);
    }

    private PNUser(Builder builder) {
        this.userId = builder.userId;
        this.custom = builder.custom;
        this.status = builder.status;
        this.type = builder.type;
    }

    public static class Builder {
        private final String userId;
        private Object custom;
        private String status;
        private String type;

        public Builder(String userId) {
            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("userId cannot be null or empty");
            }
            this.userId = userId;
        }

        public PNUser.Builder custom(Object custom) {
            this.custom = custom;
            return this;
        }

        public PNUser.Builder status(String status) {
            this.status = status;
            return this;
        }

        public PNUser.Builder type(String type) {
            this.type = type;
            return this;
        }

        public PNUser build() {
            return new PNUser(this);
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public Object getCustom() {
        return custom;
    }
}
