package io.github.ruijie_lin_42.storage_management_system_backend.auth.service;

import io.github.ruijie_lin_42.storage_management_system_backend.auth.dto.LoginRequestDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.dto.UserAuthDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.mapper.RefreshTokenMapper;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.AuthException;
import io.github.ruijie_lin_42.storage_management_system_backend.user.entity.User;
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
    private final RefreshTokenMapper refreshTokenMapper;

    public UserAuthDTO login(LoginRequestDTO loginRequestDTO){
        UserAuthDTO user = userService.findAuthInfoByUsername(loginRequestDTO.getUsername());
        if(user == null){
            throw new AuthException(ResultCode.USER_NOT_FOUND);
        }
        if(!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPasswordHash())){
            throw new AuthException(ResultCode.LOGIN_FAIL);
        }
        return user;
    }

    public UserVo findUserById(Long userId){
        return userService.findByUserId(userId);
    }

    public UserAuthDTO findAuthInfoByUserId(Long userId){
        return userService.findAuthInfoByUserId(userId);
    }

}
