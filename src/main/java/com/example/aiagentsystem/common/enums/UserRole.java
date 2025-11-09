package com.example.aiagentsystem.common.enums;

public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    ANALYST("ROLE_ANALYST"),
    VIEWER("ROLE_VIEWER");

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}