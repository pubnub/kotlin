package com.pubnub.api;

public class ChannelGroup {
    String group;
    String namespace;

    ChannelGroup(String name) throws PubnubException {
        int index = name.indexOf(":");

        if (index == -1) {
            setGroup(name);
        } else {
            setNamespace(name.substring(0, index));
            setGroup(name.substring(index + 1));
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
