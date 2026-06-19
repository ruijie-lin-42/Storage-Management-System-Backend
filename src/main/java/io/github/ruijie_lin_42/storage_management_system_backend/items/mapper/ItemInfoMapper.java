package io.github.ruijie_lin_42.storage_management_system_backend.items.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.items.entity.ItemInfo;
import io.github.ruijie_lin_42.storage_management_system_backend.items.vo.ItemInfoDetailVo;
import io.github.ruijie_lin_42.storage_management_system_backend.items.vo.StorageItemInfoVo;
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

    public Page<ItemInfoDetailVo> getItemInfo(
            Page<ItemInfoDetailVo> page,
            @Param("keyword") String keyword,
            @Param("categoryName") String categoryName
    );

    public Page<StorageItemInfoVo> getStorageItemInfo(
            Page<StorageItemInfoVo> page,
            @Param("keyword") String keyword,
            @Param("storageId") Long storageId
    );

    public Integer selectRankByItemId(
            @Param("itemId") Long itemId
    );

}
