package io.github.ruijie_lin_42.storage_management_system_backend.common.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getUserIdFromContext() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
