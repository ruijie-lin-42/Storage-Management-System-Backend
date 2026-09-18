package io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.service;

import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.AuthenticationException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.BusinessException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.utils.SecurityUtils;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.dto.LoginRequestDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.dto.LogoutRequestDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.dto.SecurityUser;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request.LoginRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request.ResetPasswordRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request.VerifyPasswordRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final SecurityUserDetailsService securityUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public SecurityUser login(LoginRequestDTO loginRequestDTO) {
        LoginRequest loginRequest = loginRequestDTO.getLoginRequest();
        SecurityUser user = loadUserByUsername(loginRequest.getUsername());
        if (verifyPassword(user, loginRequest.getPassword())) {
            log.info("User login successful, userId={}, ip={}, userAgent={}",
                    user.getUserId(), loginRequestDTO.getIp(), loginRequestDTO.getUserAgent());
            return user;
        } else {
            throw new AuthenticationException(ResultCode.LOGIN_FAIL, Map.of(
                    "username", loginRequest.getUsername()
            ));
        }
    }

    public void logout(LogoutRequestDTO logoutRequestDTO) {
        String tokenValue = logoutRequestDTO.getTokenValue();
        refreshTokenService.revokeTokenByValueOnLogout(tokenValue);
        log.info("User logout successful, userId={}, ip={}, userAgent={}",
                SecurityUtils.getUserIdFromContext(), logoutRequestDTO.getIp(), logoutRequestDTO.getUserAgent());
    }

    public boolean verifyPassword(VerifyPasswordRequest verifyPasswordRequest) {
        SecurityUser user = loadUserByUsername(verifyPasswordRequest.getUsername());
        return verifyPassword(user, verifyPasswordRequest.getPassword());
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        SecurityUser user = loadUserByUsername(resetPasswordRequest.getUsername());
        if (verifyPassword(user, resetPasswordRequest.getOldPassword())) {
            securityUserDetailsService.changePasswordById(user.getUserId(), resetPasswordRequest.getNewPassword());
            log.info("Password reset successfully, operatorId={}, userId={}",
                    SecurityUtils.getUserIdFromContext(), user.getUserId());
        } else {
            throw new BusinessException(ResultCode.PASSWORD_RESET_FAIL, Map.of(
                    "userId", user.getUserId()
            ));
        }
    }

    // ==================== helper methods ====================

    private boolean verifyPassword(@Nonnull UserDetails user, String password) {
        if (password == null || password.isBlank()) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }

    private SecurityUser loadUserByUsername(String username) {
        SecurityUser user = (SecurityUser) securityUserDetailsService.loadUserByUsername(username);
        if (user == null) {
            throw new AuthenticationException(ResultCode.USER_NOT_FOUND, Map.of(
                    "username", username)
            );
        }
        return user;
    }

}
