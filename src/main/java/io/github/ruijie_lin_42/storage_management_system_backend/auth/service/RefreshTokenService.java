package io.github.ruijie_lin_42.storage_management_system_backend.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.entity.RefreshToken;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.mapper.RefreshTokenMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenMapper refreshTokenMapper;

    public String createRefreshToken(Long userId){
        LambdaQueryWrapper<RefreshToken> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RefreshToken::getUserId, userId);
        RefreshToken oldToken = refreshTokenMapper.selectOne(lambdaQueryWrapper);
        String rawToken = UUID.randomUUID().toString();
        RefreshToken token = new RefreshToken();
        token.setUserId(userId);
        token.setTokenHash(DigestUtils.sha256Hex(rawToken));
        token.setExpiration(LocalDateTime.now().plusDays(1));
        token.setRevoked(false);
        if(oldToken == null){
            refreshTokenMapper.insert(token);
        }else{
            token.setId(oldToken.getId());
            refreshTokenMapper.updateById(token);
        }
        return rawToken;
    }

    public RefreshToken getToken(String token){
        LambdaQueryWrapper<RefreshToken> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RefreshToken::getTokenHash, DigestUtils.sha256Hex(token));
        return refreshTokenMapper.selectOne(lambdaQueryWrapper);
    }

    public int removeTokenById(Long tokenId){
        return refreshTokenMapper.deleteById(tokenId);
    }

}
