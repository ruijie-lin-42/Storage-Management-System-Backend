package io.github.ruijie_lin_42.storage_management_system_backend.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.entity.RefreshToken;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.mapper.RefreshTokenMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenMapper refreshTokenMapper;

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
            UpdateWrapper<RefreshToken> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", oldToken.getId())
                    .set("token_hash", tokenHash)
                    .set("expiration", Instant.now().plus(Duration.ofDays(1)))
                    .set("revoked_at", null);
            refreshTokenMapper.update(updateWrapper);
        }
        return rawToken;
    }

    public RefreshToken getToken(String token) {
        LambdaQueryWrapper<RefreshToken> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RefreshToken::getTokenHash, DigestUtils.sha256Hex(token));
        return refreshTokenMapper.selectOne(lambdaQueryWrapper);
    }

    public int revokeTokenByValue(String tokenValue) {
        return refreshTokenMapper.revokeTokenByHash(Instant.now(), DigestUtils.sha256Hex(tokenValue));
    }

}
