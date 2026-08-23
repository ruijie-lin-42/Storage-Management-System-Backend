package io.github.ruijie_lin_42.storage_management_system_backend.common.utils;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class SecurityUtils {

    public static Long getUserIdFromContext() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Role getCurrentUserHighestRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = new ArrayList<>();
        authorities.forEach((grantedAuthority) -> {
            roles.add(grantedAuthority.getAuthority());
        });
        int numKnownRole = 0;
        Role highestRole = Role.USER;
        Iterator<String> roleIterator = roles.iterator();
        String role;
        while (roleIterator.hasNext()) {
            role = roleIterator.next();
            numKnownRole++;
            if (role != null) {
                if (role.equals(Role.USER.getAuthority())) {
                    highestRole = Role.USER;
                } else if (role.equals(Role.ADMIN.getAuthority())) {
                    highestRole = Role.ADMIN;
                } else if (role.equals(Role.SUPER_ADMIN.getAuthority())) {
                    highestRole = Role.SUPER_ADMIN;
                } else {
                    numKnownRole--;
                }
            } else {
                numKnownRole--;
            }
        }
        if(numKnownRole == 0) {
            return null;
        }
        return highestRole;
    }

}
