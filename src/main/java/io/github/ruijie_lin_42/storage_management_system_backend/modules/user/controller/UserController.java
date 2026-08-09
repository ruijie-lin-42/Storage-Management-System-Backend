package io.github.ruijie_lin_42.storage_management_system_backend.modules.user.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.ApiException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.DataIntegrityException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.request.CreateUserRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.request.DashboardEditUserRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.request.ProfileEditUserRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.request.UserQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.service.UserService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.response.UserQueryResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.entity.User;

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
    public Void createUser(@RequestBody @Valid CreateUserRequest user) {
        try{
            int affectedNumRows = userService.createUser(user);
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
    public PageResultResponse<UserQueryResponse> queryPage(@RequestBody UserQueryRequest userQueryRequest) {
        return userService.queryUserInPage(userQueryRequest);
    }

    @PatchMapping("/dashboard/{id}")
    @Operation(description = "edit user using their id")
    public Void profileEditUser(@RequestBody @Valid DashboardEditUserRequest user, @PathVariable Long id) {
        int affectedNumRows = userService.dashboardEditUserById(user, id);
        if(affectedNumRows == 0){
            throw new ApiException(ResultCode.USER_UNAVAILABLE);
        }else if(affectedNumRows > 1){
            throw new DataIntegrityException(ResultCode.UPDATE_AFFECTED_ROWS_INVALID);
        }
        return null;
    }

    @PatchMapping("/profile/{id}")
    @Operation(description = "edit user info using their id (edit info in profile page)")
    public Void profileEditUser(@RequestBody @Valid ProfileEditUserRequest profileEditUserRequest, @PathVariable Long id){
        int affectedRows = userService.profileEditUserById(profileEditUserRequest, id);
        if(affectedRows == 0){
            throw new ApiException(ResultCode.USER_UNAVAILABLE);
        }else if(affectedRows > 1){
            throw new DataIntegrityException(ResultCode.UPDATE_AFFECTED_ROWS_INVALID);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(description = "delete user (mark them as deleted)")
    public Void deleteUserById(@PathVariable Long id) {
        int affectedNumRows = userService.deleteUserById(id);
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
