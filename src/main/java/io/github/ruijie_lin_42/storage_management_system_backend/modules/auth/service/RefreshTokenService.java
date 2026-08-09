package io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.AuthException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.DataIntegrityException;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.entity.RefreshToken;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.mapper.RefreshTokenMapper;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.response.UserQueryResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenMapper refreshTokenMapper;
    private final UserService userService;
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
            RefreshToken token = this.getTokenByValue(refreshToken);
            if (token == null || token.getRevokedAt() != null) {
                throw new AuthException(ResultCode.INVALID_TOKEN);
            }
            if (token.getExpiration().isBefore(Instant.now())) {
                this.revokeTokenByValueOnRefresh(refreshToken);
                throw new AuthException(ResultCode.INVALID_TOKEN);
            } else {
                UserQueryResponse user = userService.findUserById(token.getUserId());
                if (user == null) {
                    throw new AuthException(ResultCode.INVALID_TOKEN);
                }
                return jwtService.getToken(user.getId(), user.getRole());
            }
        } else {
            throw new AuthException(ResultCode.INVALID_TOKEN);
        }
    }

    public void revokeTokenByValueOnLogout(String tokenValue) {
        int affectedRows = revokeTokenByValue(tokenValue);
        // TODO: change to formal loggers
        if (affectedRows > 1) {
            System.out.println("logout affected rows > 1");
        } else if (affectedRows == 0) {
            System.out.println("logout failed");
        }
    }

    private void revokeTokenByValueOnRefresh(String tokenValue) {
        int affectedRows = revokeTokenByValue(tokenValue);
        // TODO: change to formal loggers
        if (affectedRows > 1) {
            System.out.println("refresh token revoked > 1");
            throw new DataIntegrityException(ResultCode.DELETE_AFFECTED_ROWS_INVALID);
        } else if (affectedRows == 0) {
            // not likely to happen
            System.out.println("unable to revoke refresh token");
        }
    }

    private int revokeTokenByValue(String tokenValue) {
        LambdaUpdateWrapper<RefreshToken> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(RefreshToken::getTokenHash, DigestUtils.sha256Hex(tokenValue))
                .set(RefreshToken::getRevokedAt, Instant.now());
        return refreshTokenMapper.update(lambdaUpdateWrapper);
    }

}
