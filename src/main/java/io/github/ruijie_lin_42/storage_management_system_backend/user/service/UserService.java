package io.github.ruijie_lin_42.storage_management_system_backend.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.dto.UserAuthDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.Status;
import io.github.ruijie_lin_42.storage_management_system_backend.common.vo.PageResultVo;
import io.github.ruijie_lin_42.storage_management_system_backend.user.entity.User;
import io.github.ruijie_lin_42.storage_management_system_backend.user.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.ruijie_lin_42.storage_management_system_backend.user.dto.CreateUserDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.user.dto.EditUserDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.user.dto.UserQueryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.user.vo.UserVo;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * user service implementation
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-05-24
 */
@Service
@Primary
public class UserService extends ServiceImpl<UserMapper, User> {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public int register(CreateUserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setAge(dto.getAge());
        user.setGender(dto.getGender());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setStatus(Status.VALID);
        return userMapper.insert(user);
    }

    public int editById(EditUserDTO dto, Long id) {
        User user = new User();
        user.setId(id);
        user.setName(dto.getName());
        user.setAge(dto.getAge());
        user.setGender(dto.getGender());
        user.setEmail(dto.getEmail());
        return userMapper.update(user, new LambdaQueryWrapper<User>()
                .eq(User::getId, user.getId())
                .eq(User::getIsDeleted, false));
    }

    public int removeById(Long id) {
        User user = new User();
        user.setId(id);
        user.setIsDeleted(true);
        return userMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getId, user.getId()));
    }

    public PageResultVo<UserVo> queryInPage(UserQueryDTO dto) {
        Page<User> page = new Page<>(dto.getPageQueryDTO().getPageNum(), dto.getPageQueryDTO().getPageSize());
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName, dto.getName());
        lambdaQueryWrapper.eq(User::getIsDeleted, false);
        Page<User> result = userMapper.selectPage(page, lambdaQueryWrapper);
        PageResultVo<UserVo> userVoPage = new PageResultVo<>();
        userVoPage.setRecords(result.getRecords().stream().map(this::userToVo).toList());
        userVoPage.setTotal(result.getTotal());
        userVoPage.setPageNum(result.getCurrent());
        userVoPage.setPageSize(result.getSize());
        return userVoPage;
    }

    public UserAuthDTO findAuthInfoByUsername(String username){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        if(user == null){
            return null;
        }
        UserAuthDTO userAuthDTO = new UserAuthDTO();
        userAuthDTO.setUserId(user.getId());
        userAuthDTO.setPasswordHash(user.getPassword());
        userAuthDTO.setRole(user.getRole());
        return userAuthDTO;
    }

    public UserAuthDTO findAuthInfoByUserId(Long userId){
        User user = userMapper.selectById(userId);
        if(user == null){
            return null;
        }
        UserAuthDTO userAuthDTO = new UserAuthDTO();
        userAuthDTO.setUserId(user.getId());
        userAuthDTO.setPasswordHash(user.getPassword());
        userAuthDTO.setRole(user.getRole());
        return userAuthDTO;
    }

    public UserVo findByUserId(Long userId){
        return userToVo(userMapper.selectById(userId));
    }

    // helper function
    private UserVo userToVo(User user) {
        if(user == null){
            return null;
        }
        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setUsername(user.getUsername());
        userVo.setName(user.getName());
        userVo.setAge(user.getAge());
        userVo.setGender(user.getGender());
        userVo.setEmail(user.getEmail());
        userVo.setRole(user.getRole());
        userVo.setStatus(user.getStatus());
        return userVo;
    }


}
