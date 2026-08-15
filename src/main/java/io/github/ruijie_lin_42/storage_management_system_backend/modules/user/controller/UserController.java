package io.github.ruijie_lin_42.storage_management_system_backend.modules.user.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.ApiException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.DataIntegrityException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.ApiErrorResponseExample;
import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.CommonErrorApiResponses;
import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.RequiresAuthApiResponses;
import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.request.CreateUserRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.request.DashboardEditUserRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.request.ProfileEditUserRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.request.UserQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.service.UserService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.response.UserQueryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.entity.User;

/**
 * <p>
 * user controller
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-05-24
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user",
            description = """
                    User creation requires at least ADMIN role;\s\s
                    SUPER_ADMIN could create ADMIN and USER, ADMIN could create USER;\s\s
                    See request body schema for required fields""")
    @ApiResponse(responseCode = "200", description = "User successfully created")
    @ApiResponse(responseCode = "409", description = "User creation failed since username already exists")
    @ApiErrorResponseExample(responseCode = "409", resultCode = ResultCode.DUPLICATE_USERNAME)
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public Void createUser(@RequestBody @Valid CreateUserRequest user) {
        // TODO: move logic to service methods, remove affectedRows check
        try {
            int affectedNumRows = userService.createUser(user);
            if (affectedNumRows > 1) {
                throw new DataIntegrityException(ResultCode.INSERT_AFFECTED_ROWS_INVALID);
            }
        } catch (DuplicateKeyException e) {
            throw new ApiException(ResultCode.DUPLICATE_USERNAME);
        }
        return null;
    }

    @PostMapping("/search")
    @Operation(summary = "Search for users",
            description = """
                    Fuzzy search for users;\s\s
                    Search requires at least USER role;\s\s
                    Records will be empty if no users are found""")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public PageResultResponse<UserQueryResponse> queryPage(@RequestBody UserQueryRequest userQueryRequest) {
        return userService.queryUserInPage(userQueryRequest);
    }

    @PatchMapping("/dashboard/{id}")
    @Operation(summary = "Edit user info via dashboard",
            description = """
            Edit user info via dashboard, different from edit user info through profile;\s\s
            Through dashboard, user modification requires at least ADMIN role;\s\s
            Through dashboard, SUPER_ADMIN could edit ADMIN and USER, ADMIN could edit USER;\s\s
            See request body schema for editable fields""",
            parameters = {@Parameter(name = "id", description = "The user's id whose info is being edited via dashboard", example = "1")})
    @ApiResponse(responseCode = "200", description = "User information successfully edited through dashboard")
    @ApiResponse(responseCode = "404", description = "User requested for info edit not found")
    @ApiErrorResponseExample(responseCode = "404", resultCode = ResultCode.USER_UNAVAILABLE)
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public Void dashboardEditUser(@RequestBody @Valid DashboardEditUserRequest user, @PathVariable Long id) {
        int affectedNumRows = userService.dashboardEditUserById(user, id);
        // TODO: move logic to service methods
        if (affectedNumRows == 0) {
            throw new ApiException(ResultCode.USER_UNAVAILABLE);
        } else if (affectedNumRows > 1) {
            throw new DataIntegrityException(ResultCode.UPDATE_AFFECTED_ROWS_INVALID);
        }
        return null;
    }

    @PatchMapping("/profile/{id}")
    @Operation(summary = "Edit user info via profile page",
            description = """
                    Edit user info via profile, different from edit user info via dashboard;\s\s
                    Through profile, user modification requires at least USER role;\s\s
                    Through profile, users could edit their own info but not others;\s\s
                    See request body schema for editable fields""",
            parameters = {@Parameter(name = "id", description = "The user's id whose info is being edited via profile page", example = "1")})
    @ApiResponse(responseCode = "200", description = "User information successfully edited through profile page")
    @ApiResponse(responseCode = "404", description = "User requested for info edit not found")
    @ApiErrorResponseExample(responseCode = "404", resultCode = ResultCode.USER_UNAVAILABLE)
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public Void profileEditUser(@RequestBody @Valid ProfileEditUserRequest profileEditUserRequest, @PathVariable Long id) {
        int affectedRows = userService.profileEditUserById(profileEditUserRequest, id);
        if (affectedRows == 0) {
            throw new ApiException(ResultCode.USER_UNAVAILABLE);
        } else if (affectedRows > 1) {
            throw new DataIntegrityException(ResultCode.UPDATE_AFFECTED_ROWS_INVALID);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user",
            description = """
                    Soft deletion: mark user as deleted in database instead of real deletion;\s\s
                    User deletion requires at least ADMIN role;\s\s
                    SUPER_ADMIN could delete ADMIN and USER, ADMIN could delete USER;\s\s
                    User deletion requires user's id;\s\s
                    Returns success if target user not found or already deleted""",
            parameters = {@Parameter(name = "id", description = "The user's id whose info is being deleted", example = "1")})
    @ApiResponse(responseCode = "200", description = "User availability successfully set to unavailable")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public Void deleteUserById(@PathVariable Long id) {
        int affectedNumRows = userService.deleteUserById(id);
        if (affectedNumRows > 1) {
            throw new DataIntegrityException(ResultCode.DELETE_AFFECTED_ROWS_INVALID);
        }
        return null;
    }

    @GetMapping("/exists")
    @Operation(summary = "Check if the given username already exists.",
            description = """
                    Check whether username specified exists;\s\s
                    This step only happens on user creation or modification, therefore requires at least USER role;\s\s
                    Returns true/false if found/not found, does not throw error if not found""",
            parameters = {@Parameter(name = "username", description = "Specified username for checking if it already exists", example = "ZhangSan123")})
    @ApiResponse(responseCode = "200", description = "Successfully checked if the given username exists")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public Boolean ifUsernameExists(@RequestParam String username) {
        return !userService.lambdaQuery().eq(User::getUsername, username).list().isEmpty();
    }

}
