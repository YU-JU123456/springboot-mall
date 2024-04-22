package com.ruby.mall.constant;

public enum RoleCategory {
    ROLE_ADMIN(0, "ROLE_ADMIN"),
    ROLE_USER(1, "ROLE_USER");

    private Integer roleIdx;
    private String role;

    RoleCategory(Integer roleIdx, String role){
        this.roleIdx = roleIdx;
        this.role = role;
    }

    public Integer getRoleIdx() {
        return roleIdx;
    }

    public String getRole() {
        return role;
    }

    public static RoleCategory getRoleByValue(Integer roleIdx) {
        for (RoleCategory roleEnum : RoleCategory.values()) {
            if (roleEnum.roleIdx.equals(roleIdx)) {
                return roleEnum;
            }
        }
        throw new IllegalArgumentException("No category found for value: " + roleIdx);
    }
}
