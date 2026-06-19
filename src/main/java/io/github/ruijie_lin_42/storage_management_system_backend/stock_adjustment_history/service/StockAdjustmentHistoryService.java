package io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.common.converter.PageConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.common.vo.PageResultVo;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.convert.ItemStockHistoryConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.convert.StockAdjustmentHistoryConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.dto.*;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.entity.StockAdjustmentHistory;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.mapper.ItemStockHistoryMapper;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.mapper.StockAdjustmentHistoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.vo.ItemStockHistoryDetailVo;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.vo.ItemStockHistoryVo;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.vo.StockAdjustmentHistoryVo;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

/**
 * <p>
 * service
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-13
 */
@Service
@RequiredArgsConstructor
public class StockAdjustmentHistoryService extends ServiceImpl<StockAdjustmentHistoryMapper, StockAdjustmentHistory> {

    private final ItemStockHistoryMapper itemStockHistoryMapper;
    private final StockAdjustmentHistoryMapper stockAdjustmentHistoryMapper;
    private final ItemStockHistoryConverter itemStockHistoryConverter;
    private final StockAdjustmentHistoryConverter stockAdjustmentHistoryConverter;

    public PageResultVo<StockAdjustmentHistoryVo> queryStockAdjustmentHistoryByStorageName(StockAdjustmentHistoryQueryDTO stockAdjustmentHistoryQueryDTO) {
        Page<StockAdjustmentHistoryVo> page = new Page<>(stockAdjustmentHistoryQueryDTO.getPageQueryDTO().getPageNum(), stockAdjustmentHistoryQueryDTO.getPageQueryDTO().getPageSize());
        Page<StockAdjustmentHistoryVo> result = stockAdjustmentHistoryMapper.selectStockAdjustmentHistoryByStorageName(page, stockAdjustmentHistoryQueryDTO.getStorageName());
        return PageConverter.convert(result, Function.identity());
    }

    public List<ItemStockHistoryVo> queryItemStockHistoryByAdjustmentId
            (ItemStockHistoryQueryByAdjustmentDTO itemStockHistoryQueryByAdjustmentDTO) {
        return itemStockHistoryMapper.queryItemStockHistoryByAdjustmentId(itemStockHistoryQueryByAdjustmentDTO.getAdjustmentId(), itemStockHistoryQueryByAdjustmentDTO.getItemName());
    }

    public PageResultVo<ItemStockHistoryDetailVo> queryItemStockHistoryByItemId
            (ItemStockHistoryQueryByItemDTO itemStockHistoryQueryByItemDTO) {
        Page<ItemStockHistoryDetailVo> page = new Page<>(itemStockHistoryQueryByItemDTO.getPageQueryDTO().getPageNum(), itemStockHistoryQueryByItemDTO.getPageQueryDTO().getPageSize());
        Page<ItemStockHistoryDetailVo> result = itemStockHistoryMapper.queryItemStockHistoryByItemId(page, itemStockHistoryQueryByItemDTO.getItemId());
        return PageConverter.convert(result, Function.identity());
    }

    public StockAdjustmentHistory addStockAdjustmentHistory(CreateStockAdjustmentHistoryDTO createStockAdjustmentHistoryDTO) {
        StockAdjustmentHistory stockAdjustmentHistory = stockAdjustmentHistoryConverter.toEntity(createStockAdjustmentHistoryDTO);
        stockAdjustmentHistoryMapper.insertStockAdjustmentHistory(stockAdjustmentHistory);
        return stockAdjustmentHistory;
    }

    public Integer addItemStockHistory(CreateItemStockHistoryDTO createItemStockHistoryDTO) {
        return itemStockHistoryMapper.insertItemStockHistory(itemStockHistoryConverter.toEntity(createItemStockHistoryDTO));
    }

    public Integer getPageNumByAdjustmentId(Long adjustmentId, Integer pageSize) {
        StockAdjustmentHistory stockAdjustmentHistory = stockAdjustmentHistoryMapper.selectById(adjustmentId);
        if (stockAdjustmentHistory == null) {
            return null;
        }
        Integer rank = stockAdjustmentHistoryMapper.selectRankByAdjustmentId(stockAdjustmentHistory.getCreatedAt(), adjustmentId);
        return (rank / pageSize) + 1;
    }

}
