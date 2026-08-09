package io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.entity.StockHistory;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.response.StockHistoryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.Instant;

/**
 * <p>
 * Mapper interface
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-13
 */
@Mapper
public interface StockHistoryMapper extends BaseMapper<StockHistory> {

    Page<StockHistoryResponse> selectStockHistoryByStorageName(
            Page<StockHistoryResponse> page,
            @Param("keyword") String keyword
    );

    Integer insertStockHistory(StockHistory stockHistory);

    Integer selectRankById(
            @Param("createdAt") Instant createdAt,
            @Param("stockHistoryId") Long stockHistoryId
    );

}
