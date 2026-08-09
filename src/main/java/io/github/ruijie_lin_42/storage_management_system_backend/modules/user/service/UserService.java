package io.github.ruijie_lin_42.storage_management_system_backend.modules.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.dto.UserAuthDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.common.converter.PageConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.convert.UserConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.request.ProfileEditUserRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.entity.User;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.request.CreateUserRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.request.DashboardEditUserRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.request.UserQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.response.UserQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * <p>
 * user service implementation
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-05-24
 */
@Service
@RequiredArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserConverter userConverter;

    public int createUser(CreateUserRequest createUserRequest) {
        User user = userConverter.toEntity(createUserRequest, passwordEncoder.encode((createUserRequest.getPassword())));
        return userMapper.insert(user);
    }

    public int dashboardEditUserById(DashboardEditUserRequest dashboardEditUserRequest, Long id) {
        User user = userConverter.toEntity(dashboardEditUserRequest);
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getId, id)
                .isNull(User::getDeletedAt);
        return userMapper.update(user, lambdaUpdateWrapper);
    }

    public int profileEditUserById(ProfileEditUserRequest profileEditUserRequest, Long id) {
        User user = userConverter.toEntity(profileEditUserRequest);
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getId, id)
                .isNull(User::getDeletedAt);
        return userMapper.update(user, lambdaUpdateWrapper);
    }

    public int deleteUserById(Long id) {
        User user = new User();
        user.setId(id);
        user.setDeletedAt(Instant.now());
        return userMapper.updateById(user);
    }

    public PageResultResponse<UserQueryResponse> queryUserInPage(UserQueryRequest userQueryRequest) {
        Page<User> page = new Page<>(userQueryRequest.getPageQueryRequest().getPageNum(), userQueryRequest.getPageQueryRequest().getPageSize());
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName, userQueryRequest.getName())
                .isNull(User::getDeletedAt);
        Page<User> result = userMapper.selectPage(page, lambdaQueryWrapper);
        return PageConverter.convert(result, userConverter::toQueryResponse);
    }

    public UserAuthDTO findAuthInfoByUsername(String username) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        if (user == null) {
            return null;
        }
        return userConverter.toAutoDTO(user);
    }

    public UserQueryResponse findUserById(Long userId) {
        User user = userMapper.selectById(userId);
        return userConverter.toQueryResponse(user);
    }

    public Integer changePasswordById(Long id, String newPassword) {
        User user = new User();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        return userMapper.updateById(user);
    }

}
