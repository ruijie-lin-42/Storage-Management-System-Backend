package io.github.ruijie_lin_42.storage_management_system_backend.items.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.items.entity.Items;
import io.github.ruijie_lin_42.storage_management_system_backend.items.vo.ItemsVo;
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
public interface ItemsMapper extends BaseMapper<Items> {

    public Page<ItemsVo> selectItemsDetail(
            Page<ItemsVo> page,
            @Param("keyword") String keyword,
            @Param("categoryName") String categoryName,
            @Param("storageId") Long storageId
    );

    // add lock on row reference, auto unlock on commit
    public Items selectForUpdate(
            @Param("itemId") Long itemId,
            @Param("storageId") Long storageId
    );

    public Integer insertItem(
            @Param("itemId") Long itemId,
            @Param("storageId") Long storageId,
            @Param("count") Integer count
    );

    public Integer updateItemCount(
            @Param("itemId") Long itemId,
            @Param("storageId") Long storageId,
            @Param("count") Integer count
    );

}
