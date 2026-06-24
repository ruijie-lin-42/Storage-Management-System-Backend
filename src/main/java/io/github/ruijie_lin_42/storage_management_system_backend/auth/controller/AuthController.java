package io.github.ruijie_lin_42.storage_management_system_backend.auth.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.auth.dto.ResetPasswordDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.dto.VerifyPasswordDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.vo.AuthVo;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.dto.LoginRequestDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.dto.UserAuthDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.entity.RefreshToken;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.service.AuthService;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.service.JwtService;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.service.RefreshTokenService;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.AuthException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.DataIntegrityException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.result.Result;
import io.github.ruijie_lin_42.storage_management_system_backend.user.vo.UserVo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<Result<AuthVo>> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        UserAuthDTO user = authService.login(loginRequestDTO);
        String accessToken = jwtService.getToken(user.getUserId(), user.getRole());
        String refreshToken = refreshTokenService.createRefreshToken(user.getUserId());
        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                // TODO: change to true when in production
                .secure(false)
                .path("/")
                .sameSite("strict")
                .build();
        UserVo userVo = authService.findUserById(user.getUserId());
        AuthVo authVo = new AuthVo();
        authVo.setAccessToken(accessToken);
        authVo.setUser(userVo);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Result.success(authVo));
    }

    @PostMapping("/logout")
    public Void logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }
        String tokenValue = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
        if(tokenValue == null){
            return null;
        }
        authService.logout(tokenValue);
        return null;
    }

    @PostMapping("/refresh")
    public ResponseEntity<Result<AuthVo>> refresh(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthException(ResultCode.INVALID_TOKEN);
        }
        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
        if (refreshToken != null) {
            RefreshToken token = refreshTokenService.getToken(refreshToken);
            if (token == null || token.getRevokedAt() != null) {
                throw new AuthException(ResultCode.INVALID_TOKEN);
            }
            if (token.getExpiration().isBefore(Instant.now())) {
                int affectedRows = refreshTokenService.revokeTokenByValue(refreshToken);
                if (affectedRows > 1) {
                    throw new DataIntegrityException(ResultCode.DELETE_AFFECTED_ROWS_INVALID);
                }
                throw new AuthException(ResultCode.INVALID_TOKEN);
            } else {
                UserVo user = authService.findUserById(token.getUserId());
                if (user == null) {
                    throw new AuthException(ResultCode.INVALID_TOKEN);
                }
                String newToken = jwtService.getToken(user.getId(), user.getRole());
                AuthVo authVo = new AuthVo();
                authVo.setAccessToken(newToken);
                authVo.setUser(user);
                return ResponseEntity.ok().body(Result.success(authVo));
            }
        } else {
            throw new AuthException(ResultCode.INVALID_TOKEN);
        }
    }

    @GetMapping("/me")
    public UserVo me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        return authService.findUserById(userId);
    }

    @PostMapping("/verifyPassword")
    public Boolean verifyPassword(@RequestBody @Valid VerifyPasswordDTO verifyPasswordDTO){
        return authService.verifyPassword(verifyPasswordDTO);
    }

    @PostMapping("/resetPassword")
    public Boolean resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO){
        return authService.resetPassword(resetPasswordDTO);
    }

}
