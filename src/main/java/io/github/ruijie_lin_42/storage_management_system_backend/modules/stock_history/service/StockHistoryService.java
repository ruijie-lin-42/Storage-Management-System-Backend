package io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.common.converter.PageConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.convert.ItemStockHistoryConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.convert.StockHistoryConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.dto.CreateItemStockHistoryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.dto.CreateStockHistoryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.entity.StockHistory;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.mapper.ItemStockHistoryMapper;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.mapper.StockHistoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.request.*;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.response.ItemStockHistoryDetailResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.response.ItemStockHistoryResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.response.StockHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
public class StockHistoryService extends ServiceImpl<StockHistoryMapper, StockHistory> {

    private final ItemStockHistoryMapper itemStockHistoryMapper;
    private final StockHistoryMapper stockHistoryMapper;
    private final ItemStockHistoryConverter itemStockHistoryConverter;
    private final StockHistoryConverter stockHistoryConverter;

    public PageResultResponse<StockHistoryResponse> queryStockAdjustmentHistoryByStorageName(StockHistoryQueryRequest stockHistoryQueryRequest) {
        Page<StockHistoryResponse> page = new Page<>(stockHistoryQueryRequest.getPageQueryRequest().getPageNum(), stockHistoryQueryRequest.getPageQueryRequest().getPageSize());
        Page<StockHistoryResponse> result = stockHistoryMapper.selectStockHistoryByStorageName(page, stockHistoryQueryRequest.getStorageName());
        return PageConverter.convert(result, Function.identity());
    }

    public List<ItemStockHistoryResponse> queryItemStockHistoryByAdjustmentId
            (ItemStockHistoryQueryByStockHistoryRequest itemStockHistoryQueryByStockHistoryRequest) {
        return itemStockHistoryMapper.queryStockHistoryById(itemStockHistoryQueryByStockHistoryRequest.getStockHistoryId(), itemStockHistoryQueryByStockHistoryRequest.getItemName());
    }

    public PageResultResponse<ItemStockHistoryDetailResponse> queryItemStockHistoryByItemId
            (ItemStockHistoryQueryByItemRequest itemStockHistoryQueryByItemRequest) {
        Page<ItemStockHistoryDetailResponse> page = new Page<>(itemStockHistoryQueryByItemRequest.getPageQueryRequest().getPageNum(), itemStockHistoryQueryByItemRequest.getPageQueryRequest().getPageSize());
        Page<ItemStockHistoryDetailResponse> result = itemStockHistoryMapper.queryItemStockHistoryByItemId(page, itemStockHistoryQueryByItemRequest.getItemId());
        return PageConverter.convert(result, Function.identity());
    }

    public StockHistory addStockAdjustmentHistory(CreateStockHistoryDTO createStockHistoryDTO) {
        StockHistory stockHistory = stockHistoryConverter.toEntity(createStockHistoryDTO);
        stockHistoryMapper.insertStockHistory(stockHistory);
        return stockHistory;
    }

    public Integer addItemStockHistory(CreateItemStockHistoryDTO createItemStockHistoryDTO) {
        return itemStockHistoryMapper.insertItemStockHistory(itemStockHistoryConverter.toEntity(createItemStockHistoryDTO));
    }

    public Integer getPageNumById(Long stockHistoryId, Integer pageSize) {
        StockHistory stockHistory = stockHistoryMapper.selectById(stockHistoryId);
        if (stockHistory == null) {
            return null;
        }
        Integer rank = stockHistoryMapper.selectRankById(stockHistory.getCreatedAt(), stockHistoryId);
        return (rank / pageSize) + 1;
    }

}
