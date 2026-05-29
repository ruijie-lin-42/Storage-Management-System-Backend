package io.github.ruijie_lin_42.storage_management_system_backend.user.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.ApiException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.DataIntegrityException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.vo.PageResultVo;
import io.github.ruijie_lin_42.storage_management_system_backend.user.dto.CreateUserDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.user.dto.EditUserDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.user.dto.UserQueryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.user.service.UserService;
import io.github.ruijie_lin_42.storage_management_system_backend.user.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import io.github.ruijie_lin_42.storage_management_system_backend.user.entity.User;

/**
 * <p>
 *  user controller
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-05-24
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(description = "add new user")
    public Void save(@RequestBody @Valid CreateUserDTO user) {
        try{
            int affectedNumRows = userService.register(user);
            if(affectedNumRows > 1){
                throw new DataIntegrityException(ResultCode.INSERT_AFFECTED_ROWS_INVALID);
            }
        }catch(DuplicateKeyException e) {
            throw new ApiException(ResultCode.DUPLICATE_USERNAME);
        }
        return null;
    }

    @PostMapping("/search")
    @Operation(description = "fuzzy search for a user using their names")
    public PageResultVo<UserVo> queryPage(@RequestBody UserQueryDTO userQueryDTO) {
        return userService.queryInPage(userQueryDTO);
    }

    @PatchMapping("/{id}")
    @Operation(description = "edit user using their id")
    public Void updateById(@RequestBody @Valid EditUserDTO user, @PathVariable Long id) {
        int affectedNumRows = userService.editById(user, id);
        if(affectedNumRows == 0){
            throw new ApiException(ResultCode.USER_UNAVAILABLE);
        }else if(affectedNumRows > 1){
            throw new DataIntegrityException(ResultCode.UPDATE_AFFECTED_ROWS_INVALID);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(description = "delete user (mark them as deleted)")
    public Void remove(@PathVariable Long id) {
        int affectedNumRows = userService.removeById(id);
        if(affectedNumRows > 1){
            throw new DataIntegrityException(ResultCode.DELETE_AFFECTED_ROWS_INVALID);
        }
        return null;
    }

    @GetMapping("/exists")
    @Operation(description = "check if the username already exists")
    public Boolean ifUsernameExists(@RequestParam String username) {
        return !userService.lambdaQuery().eq(User::getUsername, username).list().isEmpty();
    }

}
