package io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.common.converter.PageConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.BusinessException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.DataIntegrityException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.common.utils.SecurityUtils;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.convert.StorageConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.request.CreateStorageRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.request.EditStorageRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.request.StorageQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.entity.Storage;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.mapper.StorageMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.response.StorageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.function.Function;

/**
 * <p>
 * service
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-05
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService extends ServiceImpl<StorageMapper, Storage> {

    private final StorageMapper storageMapper;
    private final StorageConverter storageConverter;

    public void createStorage(CreateStorageRequest createStorageRequest) {
        Storage storage = storageConverter.toEntity(createStorageRequest);
        try {
            storageMapper.insert(storage);
            // TODO: 设置回填id，不然这里拿不到id
            log.info("Storage created successfully, operatorId={}, storageId={}",
                    SecurityUtils.getUserIdFromContext(), storage.getId());
        } catch (DuplicateKeyException e) {
            throw new BusinessException(ResultCode.DUPLICATE_STORAGE_NAME, Map.of(
                    "storageNameAttempted", createStorageRequest.getName()
            ));
        }
    }

    public PageResultResponse<StorageResponse> queryInPage(StorageQueryRequest storageQueryRequest) {
        Page<StorageResponse> page = new Page<>(storageQueryRequest.getPageQueryRequest().getPageNum(), storageQueryRequest.getPageQueryRequest().getPageSize());
        Page<StorageResponse> result = storageMapper.selectStorageDetailByName(page, storageQueryRequest.getName());
        return PageConverter.convert(result, Function.identity());
    }

    public void editById(EditStorageRequest editStorageRequest, Long id) {
        Storage storage = storageConverter.toEntity(editStorageRequest);
        storage.setId(id);
        LambdaUpdateWrapper<Storage> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Storage::getId, id)
                .isNull(Storage::getDeletedAt);
        int affectedNumRows = storageMapper.update(storage, lambdaUpdateWrapper);
        if (affectedNumRows == 0) {
            throw new BusinessException(ResultCode.STORAGE_UNAVAILABLE, Map.of(
                    "storageId", id
            ));
        } else if (affectedNumRows > 1) {
            throw new DataIntegrityException(ResultCode.UPDATE_AFFECTED_ROWS_INVALID, 1, affectedNumRows, "updateStorageById", Map.of(
                    "storageId", id
            ));
        }
        log.info("Storage updated successfully, operatorId={}, storageId={}, newValues={}",
                SecurityUtils.getUserIdFromContext(), id, editStorageRequest);
    }

    public void removeById(Long id) {
        Storage storage = new Storage();
        storage.setId(id);
        storage.setDeletedAt(Instant.now());
        int affectedNumRows = storageMapper.updateById(storage);
        if (affectedNumRows > 1) {
            throw new DataIntegrityException(ResultCode.DELETE_AFFECTED_ROWS_INVALID, 1, affectedNumRows, "deleteStorageById", Map.of(
                    "storageId", id
            ));
        }
        log.info("Storage soft-deleted successfully, operatorId={}, storageId={}",
                SecurityUtils.getUserIdFromContext(), id);
    }

    public boolean ifStorageNameExists(String storageName){
        return !lambdaQuery().eq(Storage::getName, storageName).list().isEmpty();
    }

}
