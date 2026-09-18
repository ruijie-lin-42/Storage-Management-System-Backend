package io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.AuthenticationException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.ApiErrorResponseExample;
import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.CommonErrorApiResponses;
import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.RequiresAuthApiResponses;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.convert.AuthConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.dto.SecurityUser;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request.ResetPasswordRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request.VerifyPasswordRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.response.LoginResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request.LoginRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.response.RefreshResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.service.AuthService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.service.JwtService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.service.RefreshTokenService;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for authenticating")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthConverter authConverter;

    @PostMapping("/login")
    @Operation(summary = "User login",
            description = """
                    Checks username and password;\s\s
                    Will set refresh token in http-Only cookie, and give access token in response body if succeeded;\s\s
                    Users with any role could login""")
    @ApiResponse(responseCode = "200", description = "User login succeeded")
    @ApiResponse(responseCode = "401", description = "User login failed")
    @ApiErrorResponseExample(responseCode = "401", resultCode = ResultCode.LOGIN_FAIL)
    @CommonErrorApiResponses
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        SecurityUser user = authService.login(authConverter.toLoginRequestDTO(loginRequest, httpServletRequest.getRemoteAddr(), httpServletRequest.getHeader("User-Agent")));
        String accessToken = jwtService.getToken(user.getUserId());
        String refreshToken = refreshTokenService.createRefreshToken(user.getUserId());
        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                // TODO: change to true when in production
                .secure(false)
                .path("/")
                .sameSite("strict")
                .build();
        httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        return loginResponse;
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout",
            description = """
                    Revoke user authentication info;\s\s
                    Idempotent operation, always returns a success response without throwing errors on repeated calls;\s\s
                    User with any role could logout""")
    @ApiResponse(responseCode = "200", description = "User logout succeeded")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public Void logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        String tokenValue = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
        if (tokenValue == null) {
            return null;
        }
        authService.logout(authConverter.toLogoutRequestDTO(tokenValue, request.getRemoteAddr(), request.getHeader("User-Agent")));
        return null;
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token",
            description = """
                    Gives a new valid access token if request holds valid refresh token;\s\s
                    User with any role could refresh""")
    @ApiResponse(responseCode = "200", description = "Access token refreshed, new token sent to user")
    @ApiResponse(responseCode = "401", description = "User does not hold valid refresh token")
    @ApiErrorResponseExample(responseCode = "401", resultCode = ResultCode.INVALID_TOKEN)
    @CommonErrorApiResponses
    public RefreshResponse refresh(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String accessToken = header.substring(7);
                throw new AuthenticationException(ResultCode.INVALID_TOKEN, Map.of(
                        "refreshTokenSnippet", "",
                        "userId (unauthenticated)", jwtService.getUserId(accessToken)
                ));
            } else {
                throw new AuthenticationException(ResultCode.INVALID_TOKEN, Map.of(
                        "refreshTokenSnippet", "",
                        "userId (unauthenticated)", ""
                ));
            }
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

    @PostMapping("/verifyPassword")
    @Operation(summary = "Verify whether given username corresponds to given password",
            description = """
                    This verification only happens when the user wants to change their password after they logs in;\s\s
                    Requires current user to be logged in;\s\s
                    Users with any role could verify their password;\s\s
                    Should always return true or false without throwing exceptions, where false could mean user not found or password not correct""")
    @ApiResponse(responseCode = "200", description = "The result of if the verification passes is sent through response")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public Boolean verifyPassword(@RequestBody @Valid VerifyPasswordRequest verifyPasswordRequest) {
        return authService.verifyPassword(verifyPasswordRequest);
    }

    @PostMapping("/resetPassword")
    @Operation(summary = "Reset a user's password",
            description = """
                    This reset only happens when user wants to change their password after they logged in and passes the password verification step;\s\s
                    Requires current user to be logged in;\s\s
                    Users with any role could reset their password;\s\s
                    Would check old password again, if old password not correct then reset would fail;\s\s
                    Should always return true or false without throwing exceptions, false could mean user not found or old password incorrect""")
    @ApiResponse(responseCode = "200", description = "Password reset succeeded")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public Void resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        authService.resetPassword(resetPasswordRequest);
        return null;
    }

}
