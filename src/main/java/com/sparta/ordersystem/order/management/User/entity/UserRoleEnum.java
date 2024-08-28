package com.sparta.ordersystem.order.management.User.entity;

public enum UserRoleEnum {
    CUSTOMER(Authority.CUSTOMER),  // 사용자 권한
    OWNER(Authority.OWNER),  // 사업주 권한
    MANAGER(Authority.MANAGER),  // 관리자 권한
    MASTER(Authority.MASTER); // 마스터 권한

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String CUSTOMER = "ROLE_CUSTOMER";
        public static final String OWNER = "ROLE_OWNER";
        public static final String MANAGER = "ROLE_MANAGER";
        public static final String MASTER = "ROLE_MASTER";
    }
}
