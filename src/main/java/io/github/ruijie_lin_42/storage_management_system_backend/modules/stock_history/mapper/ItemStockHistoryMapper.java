package io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.entity.ItemStockHistory;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.response.ItemStockHistoryDetailResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.response.ItemStockHistoryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper interface
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-13
 */
@Mapper
public interface ItemStockHistoryMapper extends BaseMapper<ItemStockHistory> {

    Integer insertItemStockHistory(ItemStockHistory itemStockHistory);

    List<ItemStockHistoryResponse> queryStockHistoryById(
            @Param("stockHistoryId") Long stockHistoryId,
            @Param("itemName") String itemName
    );

    Page<ItemStockHistoryDetailResponse> queryItemStockHistoryByItemId(
            Page<ItemStockHistoryDetailResponse> page,
            @Param("itemId") Long itemId
    );

}