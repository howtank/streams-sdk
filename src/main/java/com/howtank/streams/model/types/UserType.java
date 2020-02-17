package com.howtank.streams.model.types;

import java.util.HashMap;

public enum UserType {
    SUPPORT("support"),
    COMMUNITY("community");

    private static final HashMap<String, UserType> instances;

    static {
        instances = new HashMap<String, UserType>();
        for (UserType instance : UserType.values()) {
            instances.put(instance.getName(), instance);
        }
    }

    public static UserType fromName(String name) {
        return instances.get(name);
    }

    private final String name;

    UserType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
