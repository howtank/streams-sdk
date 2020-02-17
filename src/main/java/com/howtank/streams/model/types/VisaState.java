package com.howtank.streams.model.types;

import java.util.HashMap;

public enum VisaState {
    // The visa is pending approval
    PENDING("pending"),
    // The visa is active
    ACTIVE("active"),
    // The visa was refused by manager or admin
    REFUSED("refused");

    private static final HashMap<String, VisaState> instances;

    static {
        instances = new HashMap<>();
        for (VisaState instance : VisaState.values()) {
            instances.put(instance.getName(), instance);
        }
    }

    public static VisaState fromName(String name) {
        return instances.get(name);
    }

    private final String name;

    VisaState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
