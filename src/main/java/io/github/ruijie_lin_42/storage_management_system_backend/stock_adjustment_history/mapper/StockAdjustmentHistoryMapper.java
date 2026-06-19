package io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.entity.StockAdjustmentHistory;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.vo.StockAdjustmentHistoryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 * Mapper interface
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-13
 */
@Mapper
public interface StockAdjustmentHistoryMapper extends BaseMapper<StockAdjustmentHistory> {

    public Page<StockAdjustmentHistoryVo> selectStockAdjustmentHistoryByStorageName(
            Page<StockAdjustmentHistoryVo> page,
            @Param("keyword") String keyword
    );

    public Integer insertStockAdjustmentHistory(StockAdjustmentHistory stockAdjustmentHistory);

    public Integer selectRankByAdjustmentId(
            @Param("createdAt") LocalDateTime createdAt,
            @Param("adjustmentId") Long adjustmentId
    );

}
