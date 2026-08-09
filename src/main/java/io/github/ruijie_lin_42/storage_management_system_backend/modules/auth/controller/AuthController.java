package io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request.ResetPasswordRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request.VerifyPasswordRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.response.AuthResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request.LoginRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.response.RefreshResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.dto.UserAuthDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.service.AuthService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.service.JwtService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.service.RefreshTokenService;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.AuthException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.result.Result;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.response.UserQueryResponse;
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

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<Result<AuthResponse>> login(@RequestBody LoginRequest loginRequest) {
        UserAuthDTO user = authService.login(loginRequest);
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
        UserQueryResponse userQueryResponse = authService.findUserById(user.getUserId());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(accessToken);
        authResponse.setUser(userQueryResponse);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Result.success(authResponse));
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
    public RefreshResponse refresh(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthException(ResultCode.INVALID_TOKEN);
        }
        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
        String newToken = refreshTokenService.refresh(refreshToken);
        RefreshResponse refreshResponse = new RefreshResponse();
        refreshResponse.setAccessToken(newToken);
        return refreshResponse;
    }

    @GetMapping("/me")
    public UserQueryResponse me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        return authService.findUserById(userId);
    }

    @PostMapping("/verifyPassword")
    public Boolean verifyPassword(@RequestBody @Valid VerifyPasswordRequest verifyPasswordRequest){
        return authService.verifyPassword(verifyPasswordRequest);
    }

    @PostMapping("/resetPassword")
    public Boolean resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest){
        return authService.resetPassword(resetPasswordRequest);
    }

}
