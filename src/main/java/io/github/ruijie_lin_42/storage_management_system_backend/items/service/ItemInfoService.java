package io.github.ruijie_lin_42.storage_management_system_backend.items.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.common.converter.PageConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.common.vo.PageResultVo;
import io.github.ruijie_lin_42.storage_management_system_backend.items.dto.ItemInfoDetailQueryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.items.dto.StorageItemInfoQueryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.items.entity.ItemInfo;
import io.github.ruijie_lin_42.storage_management_system_backend.items.mapper.ItemInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.ruijie_lin_42.storage_management_system_backend.items.vo.ItemInfoDetailVo;
import io.github.ruijie_lin_42.storage_management_system_backend.items.vo.StorageItemInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * <p>
 *  service
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-05
 */
@Service
@RequiredArgsConstructor
public class ItemInfoService extends ServiceImpl<ItemInfoMapper, ItemInfo> {

    private final ItemInfoMapper itemInfoMapper;

    public PageResultVo<ItemInfoDetailVo> queryItemInfoDetailInPage(ItemInfoDetailQueryDTO itemInfoDetailQueryDTO){
        Page<ItemInfoDetailVo> page = new Page<>(itemInfoDetailQueryDTO.getPageQueryDTO().getPageNum(), itemInfoDetailQueryDTO.getPageQueryDTO().getPageSize());
        Page<ItemInfoDetailVo> result = itemInfoMapper.getItemInfo(page, itemInfoDetailQueryDTO.getName(), itemInfoDetailQueryDTO.getCategory());
        return PageConverter.convert(result, Function.identity());
    }

    public PageResultVo<StorageItemInfoVo> queryStorageItemInfoInPage(StorageItemInfoQueryDTO storageItemInfoQueryDTO){
        Page<StorageItemInfoVo> page = new Page<>(storageItemInfoQueryDTO.getPageQueryDTO().getPageNum(), storageItemInfoQueryDTO.getPageQueryDTO().getPageSize());
        Page<StorageItemInfoVo> result = itemInfoMapper.getStorageItemInfo(page, storageItemInfoQueryDTO.getName(), storageItemInfoQueryDTO.getStorageId());
        return PageConverter.convert(result, Function.identity());
    }

    public Integer getPageNumByItemId(Long itemId, Integer pageSize) {
        ItemInfo itemInfo = itemInfoMapper.selectById(itemId);
        if (itemInfo == null) {
            return null;
        }
        Integer rank = itemInfoMapper.selectRankByItemId(itemId);
        return (rank / pageSize) + 1;
    }


}
