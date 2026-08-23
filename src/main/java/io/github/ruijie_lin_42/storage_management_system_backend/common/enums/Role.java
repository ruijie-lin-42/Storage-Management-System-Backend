package io.github.ruijie_lin_42.storage_management_system_backend.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    SUPER_ADMIN("ROLE_SUPER_ADMIN", 300),
    ADMIN("ROLE_ADMIN", 200),
    USER("ROLE_USER", 100);

    private final String authority;
    private final int hierarchy;

    public boolean hasHigherRoleThan(Role target) {
        return target != null && this.hierarchy > target.hierarchy;
    }

}
