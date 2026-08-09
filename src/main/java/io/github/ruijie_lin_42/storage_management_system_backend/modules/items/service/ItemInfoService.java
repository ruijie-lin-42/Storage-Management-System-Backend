package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.common.converter.PageConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.request.ItemInfoDetailQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.request.StorageItemInfoQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.entity.ItemInfo;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.mapper.ItemInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.ItemInfoDetailResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.StorageItemInfoResponse;
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

    public PageResultResponse<ItemInfoDetailResponse> queryItemInfoDetailInPage(ItemInfoDetailQueryRequest itemInfoDetailQueryRequest){
        Page<ItemInfoDetailResponse> page = new Page<>(itemInfoDetailQueryRequest.getPageQueryRequest().getPageNum(), itemInfoDetailQueryRequest.getPageQueryRequest().getPageSize());
        Page<ItemInfoDetailResponse> result = itemInfoMapper.getItemInfo(page, itemInfoDetailQueryRequest.getName(), itemInfoDetailQueryRequest.getCategory());
        return PageConverter.convert(result, Function.identity());
    }

    public PageResultResponse<StorageItemInfoResponse> queryStorageItemInfoInPage(StorageItemInfoQueryRequest storageItemInfoQueryRequest){
        Page<StorageItemInfoResponse> page = new Page<>(storageItemInfoQueryRequest.getPageQueryRequest().getPageNum(), storageItemInfoQueryRequest.getPageQueryRequest().getPageSize());
        Page<StorageItemInfoResponse> result = itemInfoMapper.getStorageItemInfo(page, storageItemInfoQueryRequest.getName(), storageItemInfoQueryRequest.getStorageId());
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
