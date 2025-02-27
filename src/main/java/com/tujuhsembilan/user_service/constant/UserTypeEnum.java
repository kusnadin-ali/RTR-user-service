package com.tujuhsembilan.user_service.constant;

public enum UserTypeEnum {
    CUSTOMER("CUSTOMER"), STAFF("STAFF"), ADMIN("ADMIN");

    private final String name;

    UserTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
