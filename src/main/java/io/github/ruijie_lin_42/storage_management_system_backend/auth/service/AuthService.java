package io.github.ruijie_lin_42.storage_management_system_backend.auth.service;

import io.github.ruijie_lin_42.storage_management_system_backend.auth.dto.LoginRequestDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.dto.ResetPasswordDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.dto.UserAuthDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.dto.VerifyPasswordDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.AuthException;
import io.github.ruijie_lin_42.storage_management_system_backend.user.service.UserService;
import io.github.ruijie_lin_42.storage_management_system_backend.user.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public UserAuthDTO login(LoginRequestDTO loginRequestDTO) {
        UserAuthDTO user = userService.findAuthInfoByUsername(loginRequestDTO.getUsername());
        if (verifyPassword(user, loginRequestDTO.getPassword())) {
            return user;
        } else {
            throw new AuthException(ResultCode.LOGIN_FAIL);
        }
    }

    public Integer logout(String tokenValue) {
        return refreshTokenService.revokeTokenByValue(tokenValue);
    }

    public UserVo findUserById(Long userId) {
        return userService.findByUserId(userId);
    }

    public boolean verifyPassword(VerifyPasswordDTO verifyPasswordDTO) {
        return verifyPassword(userService.findAuthInfoByUsername(verifyPasswordDTO.getUsername()), verifyPasswordDTO.getPassword());
    }

    public boolean resetPassword(ResetPasswordDTO resetPasswordDTO) {
        UserAuthDTO user = userService.findAuthInfoByUsername(resetPasswordDTO.getUsername());
        if (verifyPassword(user, resetPasswordDTO.getOldPassword())) {
            userService.changePassword(user, resetPasswordDTO.getNewPassword());
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
