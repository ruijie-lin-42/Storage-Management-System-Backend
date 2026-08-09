package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.entity.ItemInfo;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.ItemInfoDetailResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.StorageItemInfoResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper interface
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-05
 */
@Mapper
public interface ItemInfoMapper extends BaseMapper<ItemInfo> {

    Page<ItemInfoDetailResponse> getItemInfo(
            Page<ItemInfoDetailResponse> page,
            @Param("keyword") String keyword,
            @Param("categoryName") String categoryName
    );

    Page<StorageItemInfoResponse> getStorageItemInfo(
            Page<StorageItemInfoResponse> page,
            @Param("keyword") String keyword,
            @Param("storageId") Long storageId
    );

    Integer selectRankByItemId(
            @Param("itemId") Long itemId
    );

}
