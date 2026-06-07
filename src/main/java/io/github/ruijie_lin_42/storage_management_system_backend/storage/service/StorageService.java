package io.github.ruijie_lin_42.storage_management_system_backend.storage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.common.converter.PageConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.common.vo.PageResultVo;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.convert.StorageConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.dto.CreateStorageDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.dto.EditStorageDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.dto.StorageQueryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.entity.Storage;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.mapper.StorageMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.vo.StorageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public int createStorage(CreateStorageDTO createStorageDTO) {
        Storage storage = storageConverter.toEntity(createStorageDTO);
        return storageMapper.insert(storage);
    }

    public PageResultVo<StorageVo> queryInPage(StorageQueryDTO storageQueryDTO) {
        Page<StorageVo> page = new Page<>(storageQueryDTO.getPageQueryDTO().getPageNum(), storageQueryDTO.getPageQueryDTO().getPageSize());
        Page<StorageVo> result = storageMapper.selectStorageDetailByName(page, storageQueryDTO.getName());
        return PageConverter.convert(result, Function.identity());
    }

    public int editById(EditStorageDTO editStorageDTO, Long id){
        Storage storage = storageConverter.toEntity(editStorageDTO);
        storage.setId(id);
        LambdaQueryWrapper<Storage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Storage::getId, id);
        lambdaQueryWrapper.eq(Storage::getIsDeleted, false);
        return storageMapper.update(storage, lambdaQueryWrapper);
    }

    public int removeById(Long id){
        Storage storage = new Storage();
        storage.setId(id);
        storage.setIsDeleted(true);
        LambdaQueryWrapper<Storage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Storage::getId, id);
        return storageMapper.update(storage, lambdaQueryWrapper);
    }

}
