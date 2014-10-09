package com.pubnub.api;

public class ChannelGroup {
    String group;
    String namespace;

    ChannelGroup(String name) throws PubnubException {
        String[] parts = name.split(":", 2);

        switch (parts.length) {
            case 2:
                setNamespace(parts[0]);
                setGroup(parts[1]);
                break;
            case 1:
                group = name;
                break;
            default:
                throw new PubnubException("Invalid channel group string");
        }
    }

    public void setGroup(String groupName) {
        if (groupName != null && !"".equals(groupName)) {
            group = groupName;
        }
    }

    public void setNamespace(String namespaceName) {
        if (namespaceName != null && !"".equals(namespaceName)) {
            namespace = namespaceName;
        }
    }
}
