package io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.service;

import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request.LoginRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request.ResetPasswordRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.dto.UserAuthDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request.VerifyPasswordRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.AuthException;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.service.UserService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.response.UserQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public UserAuthDTO login(LoginRequest loginRequest) {
        UserAuthDTO user = userService.findAuthInfoByUsername(loginRequest.getUsername());
        if (verifyPassword(user, loginRequest.getPassword())) {
            return user;
        } else {
            throw new AuthException(ResultCode.LOGIN_FAIL);
        }
    }

    public void logout(String tokenValue) {
        refreshTokenService.revokeTokenByValueOnLogout(tokenValue);
    }

    public UserQueryResponse findUserById(Long userId) {
        return userService.findUserById(userId);
    }

    public boolean verifyPassword(VerifyPasswordRequest verifyPasswordRequest) {
        return verifyPassword(userService.findAuthInfoByUsername(verifyPasswordRequest.getUsername()), verifyPasswordRequest.getPassword());
    }

    public boolean resetPassword(ResetPasswordRequest resetPasswordRequest) {
        UserAuthDTO user = userService.findAuthInfoByUsername(resetPasswordRequest.getUsername());
        if (verifyPassword(user, resetPasswordRequest.getOldPassword())) {
            userService.changePasswordById(user.getUserId(), resetPasswordRequest.getNewPassword());
            return true;
        } else {
            return false;
        }
    }

    private boolean verifyPassword(UserAuthDTO user, String password) {
        if (user == null) {
            throw new AuthException(ResultCode.USER_NOT_FOUND);
        }
        if (password == null || password.isBlank()) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPasswordHash());
    }

}
