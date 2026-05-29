package io.github.ruijie_lin_42.storage_management_system_backend.common.enums;

import lombok.Getter;

@Getter
public enum Role {

    SUPER_ADMIN("ROLE_SUPER_ADMIN"),
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

}
