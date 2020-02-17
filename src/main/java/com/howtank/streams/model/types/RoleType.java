package com.howtank.streams.model.types;

public enum RoleType {
    USER,
    MANAGER,
    ADMIN,
    MODERATOR;

    public static RoleType fromName(final String name) {
        switch (name) {
            case "ROLE_USER":
            case "ROLE_SUPPORT":
                    return USER;
            case "ROLE_MANAGER":
                return MANAGER;
            case "ROLE_ADMIN":
            case "ROLE_SUPER_ADMIN":
                return ADMIN;
            case "ROLE_MODERATOR":
                return MODERATOR;
            default:
                return null;
        }
    }
}
