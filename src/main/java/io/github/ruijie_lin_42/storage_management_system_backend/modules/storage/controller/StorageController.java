package io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.ApiException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.DataIntegrityException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.request.CreateStorageRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.request.EditStorageRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.request.StorageQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.entity.Storage;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.service.StorageService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.response.StorageResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  controller
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-05
 */
@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @PostMapping
    @Operation(description = "add new storage")
    public Void addNewStorage(@RequestBody @Valid CreateStorageRequest user) {
        try{
            int affectedNumRows = storageService.createStorage(user);
            if(affectedNumRows > 1){
                throw new DataIntegrityException(ResultCode.INSERT_AFFECTED_ROWS_INVALID);
            }
        }catch(DuplicateKeyException e) {
            throw new ApiException(ResultCode.DUPLICATE_STORAGE_NAME);
        }
        return null;
    }

    @PostMapping("/search")
    @Operation(description = "fuzzy search for a storage using their names")
    public PageResultResponse<StorageResponse> queryStorageByNameInPage(@RequestBody StorageQueryRequest storageQueryRequest) {
        return storageService.queryInPage(storageQueryRequest);
    }

    @PatchMapping("/{id}")
    @Operation(description = "edit storage using the id")
    public Void updateStorageById(@RequestBody @Valid EditStorageRequest storage, @PathVariable Long id) {
        int affectedNumRows = storageService.editById(storage, id);
        if(affectedNumRows == 0){
            throw new ApiException(ResultCode.STORAGE_UNAVAILABLE);
        }else if(affectedNumRows > 1){
            throw new DataIntegrityException(ResultCode.UPDATE_AFFECTED_ROWS_INVALID);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(description = "delete storage (mark them as deleted) by id")
    public Void deleteStorageById(@PathVariable Long id) {
        int affectedNumRows = storageService.removeById(id);
        if(affectedNumRows > 1){
            throw new DataIntegrityException(ResultCode.DELETE_AFFECTED_ROWS_INVALID);
        }
        return null;
    }

    @GetMapping("/exists")
    @Operation(description = "check if the storage's name already exists")
    public Boolean ifStorageNameExists(@RequestParam String name) {
        return !storageService.lambdaQuery().eq(Storage::getName, name).list().isEmpty();
    }

}
