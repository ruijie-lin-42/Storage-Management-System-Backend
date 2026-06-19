package io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.entity.ItemStockHistory;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.vo.ItemStockHistoryDetailVo;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.vo.ItemStockHistoryVo;
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

    public Integer insertItemStockHistory(ItemStockHistory itemStockHistory);

    public List<ItemStockHistoryVo> queryItemStockHistoryByAdjustmentId(
            @Param("adjustmentId") Long adjustmentId,
            @Param("itemName") String itemName
    );

    public Page<ItemStockHistoryDetailVo> queryItemStockHistoryByItemId(
            Page<ItemStockHistoryDetailVo> page,
            @Param("itemId") Long itemId
    );

}