package io.github.ruijie_lin_42.storage_management_system_backend.storage.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.ApiException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.DataIntegrityException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.vo.PageResultVo;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.dto.CreateStorageDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.dto.EditStorageDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.dto.StorageQueryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.entity.Storage;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.service.StorageService;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.vo.StorageVo;
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
    public Void save(@RequestBody @Valid CreateStorageDTO user) {
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
    @Operation(description = "fuzzy search for a user using their names")
    public PageResultVo<StorageVo> queryPage(@RequestBody StorageQueryDTO storageQueryDTO) {
        return storageService.queryInPage(storageQueryDTO);
    }

    @PatchMapping("/{id}")
    @Operation(description = "edit user using their id")
    public Void updateById(@RequestBody @Valid EditStorageDTO storage, @PathVariable Long id) {
        int affectedNumRows = storageService.editById(storage, id);
        if(affectedNumRows == 0){
            throw new ApiException(ResultCode.STORAGE_UNAVAILABLE);
        }else if(affectedNumRows > 1){
            throw new DataIntegrityException(ResultCode.UPDATE_AFFECTED_ROWS_INVALID);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(description = "delete user (mark them as deleted)")
    public Void remove(@PathVariable Long id) {
        int affectedNumRows = storageService.removeById(id);
        if(affectedNumRows > 1){
            throw new DataIntegrityException(ResultCode.DELETE_AFFECTED_ROWS_INVALID);
        }
        return null;
    }

    @GetMapping("/exists")
    @Operation(description = "check if the username already exists")
    public Boolean ifUsernameExists(@RequestParam String name) {
        return !storageService.lambdaQuery().eq(Storage::getName, name).list().isEmpty();
    }

}
