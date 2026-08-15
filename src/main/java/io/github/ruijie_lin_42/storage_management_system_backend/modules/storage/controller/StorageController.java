package io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.ApiException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.DataIntegrityException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.ApiErrorResponseExample;
import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.CommonErrorApiResponses;
import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.RequiresAuthApiResponses;
import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.request.CreateStorageRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.request.EditStorageRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.request.StorageQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.entity.Storage;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.service.StorageService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.response.StorageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;

/**
 * <p>
 * controller
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-05
 */
@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
@Tag(name = "Storage Management", description = "Endpoints for managing storages")
public class StorageController {

    private final StorageService storageService;

    @PostMapping
    @Operation(summary = "Create a new storage",
            description = """
                    Storage creation requires ADMIN role;\s\s
                    See request body schema for required fields""")
    @ApiResponse(responseCode = "200", description = "Storage successfully created")
    @ApiResponse(responseCode = "409", description = "Storage name already exists, creation failed")
    @ApiErrorResponseExample(responseCode = "409", resultCode = ResultCode.DUPLICATE_STORAGE_NAME)
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public Void addNewStorage(@RequestBody @Valid CreateStorageRequest user) {
        // TODO: move logic to service methods, remove affectedRows check
        try {
            int affectedNumRows = storageService.createStorage(user);
            if (affectedNumRows > 1) {
                throw new DataIntegrityException(ResultCode.INSERT_AFFECTED_ROWS_INVALID);
            }
        } catch (DuplicateKeyException e) {
            throw new ApiException(ResultCode.DUPLICATE_STORAGE_NAME);
        }
        return null;
    }

    @PostMapping("/search")
    @Operation(summary = "Search for storages",
            description = """
                    Fuzzy search for storages;\s\s
                    Search requires at least USER role;\s\s
                    Records will be empty if no storages are found""")
    @ApiResponse(responseCode = "200", description = "Storage retrieved successfully")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public PageResultResponse<StorageResponse> queryStorageByNameInPage(@RequestBody StorageQueryRequest storageQueryRequest) {
        return storageService.queryInPage(storageQueryRequest);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Edit storage info",
            description = """
                    Edit storage info via dashboard;\s\s
                    Storage modification requires at least ADMIN role;\s\s
                    See request body schema for editable fields""",
            parameters = {@Parameter(name = "id", description = "The storage's id, which info is being edited via dashboard", example = "1")})
    @ApiResponse(responseCode = "200", description = "Storage information successfully edited")
    @ApiResponse(responseCode = "404", description = "Storage requested for info edit not found")
    @ApiErrorResponseExample(responseCode = "404", resultCode = ResultCode.STORAGE_UNAVAILABLE)
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public Void updateStorageById(@RequestBody @Valid EditStorageRequest storage, @PathVariable Long id) {
        int affectedNumRows = storageService.editById(storage, id);
        if (affectedNumRows == 0) {
            throw new ApiException(ResultCode.STORAGE_UNAVAILABLE);
        } else if (affectedNumRows > 1) {
            throw new DataIntegrityException(ResultCode.UPDATE_AFFECTED_ROWS_INVALID);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete storage",
            description = """
                    Soft deletion: mark storage deleted as deleted instead of physical delete them;\s\s
                    Storage deletion requires at least ADMIN role;\s\s
                    Returns success if target storage not found or already deleted""",
            parameters = {@Parameter(name = "id", description = "The storage's id, which is being deleted", example = "1")})
    @ApiResponse(responseCode = "200", description = "Storage availability successfully set to unavailable")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public Void deleteStorageById(@PathVariable Long id) {
        int affectedNumRows = storageService.removeById(id);
        if (affectedNumRows > 1) {
            throw new DataIntegrityException(ResultCode.DELETE_AFFECTED_ROWS_INVALID);
        }
        return null;
    }

    @GetMapping("/exists")
    @Operation(summary = "Check if the storage's name already exists",
            description = """
                    Check whether storage name specified already exists;\s\s
                    This step would only happen on storage's creation or modification, therefore requires at least ADMIN role;\s\s
                    Returns true/false if found/not found, should always return success""",
            parameters = {@Parameter(name = "name", description = "Specified storage's name for checking if it already exists")})
    @ApiResponse(responseCode = "200", description = "Successfully checked if the given storage exists")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public Boolean ifStorageNameExists(@RequestParam String name) {
        return !storageService.lambdaQuery().eq(Storage::getName, name).list().isEmpty();
    }

}
