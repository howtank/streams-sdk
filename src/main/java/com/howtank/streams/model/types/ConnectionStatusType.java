package com.howtank.streams.model.types;

import java.util.HashMap;

public enum ConnectionStatusType {
    ACTIVE("active"),
    AFK("afk"),
    NOTIFIABLE("notifiable"),
    OFFLINE("offline");

    private static final HashMap<String, ConnectionStatusType> instances;

    static {
        instances = new HashMap<>();
        for (ConnectionStatusType instance : ConnectionStatusType.values()) {
            instances.put(instance.getName(), instance);
        }
    }

    public static ConnectionStatusType fromName(String name) {
        return instances.get(name);
    }

    private final String name;

    ConnectionStatusType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
