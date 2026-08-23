package io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.service;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.Status;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.dto.SecurityUser;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.dto.UserAuthDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthDTO userInfo = userService.findAuthInfoByUsername(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("User does not exist: " + username);
        }
        return toSecurityUser(userInfo);
    }

    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
        UserAuthDTO userInfo = userService.findAuthInfoByUserId(userId);
        if (userInfo == null) throw new UsernameNotFoundException("User does not exist: " + userId);
        return toSecurityUser(userInfo);
    }

    public void changePasswordById(Long id, String newPassword){
        int affectedRows = userService.changePasswordById(id, newPassword);
        // TODO: add formal loggers
    }

    // ==================== helper methods ====================

    private SecurityUser toSecurityUser(UserAuthDTO userInfo) {
        return new SecurityUser(userInfo.getUserId(),
                userInfo.getUsername(),
                userInfo.getPasswordHash(),
                userInfo.getRole(),
                userInfo.getStatus() == Status.VALID);
    }

}
