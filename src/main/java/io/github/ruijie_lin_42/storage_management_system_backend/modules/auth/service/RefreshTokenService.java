package io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.AuthenticationException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.DataIntegrityException;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.entity.RefreshToken;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.mapper.RefreshTokenMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenMapper refreshTokenMapper;
    private final SecurityUserDetailsService securityUserDetailsService;
    private final JwtService jwtService;

    public String createRefreshToken(Long userId) {
        LambdaQueryWrapper<RefreshToken> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RefreshToken::getUserId, userId);
        RefreshToken oldToken = refreshTokenMapper.selectOne(lambdaQueryWrapper);
        String rawToken = UUID.randomUUID().toString();
        String tokenHash = DigestUtils.sha256Hex(rawToken);
        if (oldToken == null) {
            RefreshToken token = new RefreshToken();
            token.setUserId(userId);
            token.setTokenHash(tokenHash);
            token.setExpiration(Instant.now().plus(Duration.ofDays(1)));
            token.setRevokedAt(null);
            refreshTokenMapper.insert(token);
        } else {
            LambdaUpdateWrapper<RefreshToken> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(RefreshToken::getId, oldToken.getId())
                    .set(RefreshToken::getTokenHash, tokenHash)
                    .set(RefreshToken::getExpiration, Instant.now().plus(Duration.ofDays(1)))
                    .set(RefreshToken::getRevokedAt, null);
            refreshTokenMapper.update(lambdaUpdateWrapper);
        }
        return rawToken;
    }

    private RefreshToken getTokenByValue(String token) {
        LambdaQueryWrapper<RefreshToken> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RefreshToken::getTokenHash, DigestUtils.sha256Hex(token));
        return refreshTokenMapper.selectOne(lambdaQueryWrapper);
    }

    public String refresh(String refreshToken) {
        if (refreshToken != null) {
            String tokenSnippet = refreshToken.substring(10);
            RefreshToken token = this.getTokenByValue(refreshToken);
            // unrecognized token (couldn't find corresponding data in database)
            if (token == null) {
                throw new AuthenticationException(ResultCode.INVALID_TOKEN, Map.of(
                        "reason", "Unrecognized refresh token",
                        "tokenSnippet", tokenSnippet,
                        // TODO: add unauthenticated user id by moving controller logic to here
                        "userId (unauthenticated)", ""
                ));
            }
            // token already revoked
            if (token.getRevokedAt() != null) {
                throw new AuthenticationException(ResultCode.INVALID_TOKEN, Map.of(
                        "reason", "Refresh token revoked",
                        "tokenSnippet", tokenSnippet,
                        "userId (unauthenticated)", token.getUserId(),
                        "revokedAt", token.getRevokedAt()
                ));
            }
            // token expired
            if (token.getExpiration().isBefore(Instant.now())) {
                this.revokeTokenByValueOnRefresh(refreshToken);
                throw new AuthenticationException(ResultCode.INVALID_TOKEN, Map.of(
                        "reason", "Refresh token expired",
                        "tokenSnippet", tokenSnippet,
                        "userId (unauthenticated)", token.getUserId(),
                        "expiredAt", token.getExpiration()
                ));
            }
            Long userId = token.getUserId();
            UserDetails user = securityUserDetailsService.loadUserByUserId(userId);
            // couldn't find any user who should hold the token
            if (user == null) {
                throw new AuthenticationException(ResultCode.INVALID_TOKEN, Map.of(
                        "reason", "Unrecognized user",
                        "tokenSnippet", tokenSnippet,
                        "userId (unauthenticated)", token.getUserId()
                ));
            }
            // if we didn't encounter all situations above, then refresh token for the user
            return jwtService.getToken(userId);
        }
        // if refreshToken is null
        else {
            throw new AuthenticationException(ResultCode.INVALID_TOKEN, Map.of(
                    "reason", "Null refresh token",
                    // TODO: add unauthenticated user id by moving controller logic to here
                    "userId (unauthenticated)", ""
            ));
        }
    }

    public void revokeTokenByValueOnLogout(String tokenValue) {
        int affectedRows = revokeTokenByValue(tokenValue);
        // TODO: change to formal loggers
        if (affectedRows > 1) {
            throw new DataIntegrityException(ResultCode.UPDATE_AFFECTED_ROWS_INVALID, 1, affectedRows, "revokeRefreshTokenOnLogout",
                    Map.of("tokenSnippet", tokenValue.substring(10)));
        } else if (affectedRows == 0) {
            System.out.println("logout failed");
        }
    }

    private void revokeTokenByValueOnRefresh(String tokenValue) {
        int affectedRows = revokeTokenByValue(tokenValue);
        if (affectedRows > 1) {
            throw new DataIntegrityException(ResultCode.UPDATE_AFFECTED_ROWS_INVALID, 1, affectedRows, "revokeRefreshTokenOnRefresh",
                    Map.of("tokenSnippet", tokenValue.substring(10)));
        }
    }

    private int revokeTokenByValue(String tokenValue) {
        LambdaUpdateWrapper<RefreshToken> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(RefreshToken::getTokenHash, DigestUtils.sha256Hex(tokenValue))
                .set(RefreshToken::getRevokedAt, Instant.now());
        return refreshTokenMapper.update(lambdaUpdateWrapper);
    }

}
