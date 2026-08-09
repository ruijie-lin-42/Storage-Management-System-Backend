package io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.common.converter.PageConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.convert.StorageConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.request.CreateStorageRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.request.EditStorageRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.request.StorageQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.entity.Storage;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.mapper.StorageMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.response.StorageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
public class StorageService extends ServiceImpl<StorageMapper, Storage> {

    private final StorageMapper storageMapper;
    private final StorageConverter storageConverter;

    public int createStorage(CreateStorageRequest createStorageRequest) {
        Storage storage = storageConverter.toEntity(createStorageRequest);
        return storageMapper.insert(storage);
    }

    public PageResultResponse<StorageResponse> queryInPage(StorageQueryRequest storageQueryRequest) {
        Page<StorageResponse> page = new Page<>(storageQueryRequest.getPageQueryRequest().getPageNum(), storageQueryRequest.getPageQueryRequest().getPageSize());
        Page<StorageResponse> result = storageMapper.selectStorageDetailByName(page, storageQueryRequest.getName());
        return PageConverter.convert(result, Function.identity());
    }

    public int editById(EditStorageRequest editStorageRequest, Long id) {
        Storage storage = storageConverter.toEntity(editStorageRequest);
        storage.setId(id);
        LambdaUpdateWrapper<Storage> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Storage::getId, id)
                .isNull(Storage::getDeletedAt);
        return storageMapper.update(storage, lambdaUpdateWrapper);
    }

    public int removeById(Long id) {
        Storage storage = new Storage();
        storage.setId(id);
        storage.setDeletedAt(Instant.now());
        return storageMapper.updateById(storage);
    }

}
