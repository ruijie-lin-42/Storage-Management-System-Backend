package io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.service;

import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.AuthenticationException;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.dto.SecurityUser;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request.LoginRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request.ResetPasswordRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request.VerifyPasswordRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SecurityUserDetailsService securityUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public SecurityUser login(LoginRequest loginRequest) {
        SecurityUser user = (SecurityUser) securityUserDetailsService.loadUserByUsername(loginRequest.getUsername());
        if (verifyPassword(user, loginRequest.getPassword())) {
            return user;
        } else {
            throw new AuthenticationException(ResultCode.LOGIN_FAIL);
        }
    }

    public void logout(String tokenValue) {
        refreshTokenService.revokeTokenByValueOnLogout(tokenValue);
    }

    public boolean verifyPassword(VerifyPasswordRequest verifyPasswordRequest) {
        return verifyPassword(securityUserDetailsService.loadUserByUsername(verifyPasswordRequest.getUsername()), verifyPasswordRequest.getPassword());
    }

    public boolean resetPassword(ResetPasswordRequest resetPasswordRequest) {
        SecurityUser user = (SecurityUser) securityUserDetailsService.loadUserByUsername(resetPasswordRequest.getUsername());
        if (verifyPassword(user, resetPasswordRequest.getOldPassword())) {
            securityUserDetailsService.changePasswordById(user.getUserId(), resetPasswordRequest.getNewPassword());
            return true;
        } else {
            return false;
        }
    }

    // ==================== helper methods ====================

    private boolean verifyPassword(UserDetails user, String password) {
        if (user == null) {
            throw new AuthenticationException(ResultCode.USER_NOT_FOUND);
        }
        if (password == null || password.isBlank()) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }

}
