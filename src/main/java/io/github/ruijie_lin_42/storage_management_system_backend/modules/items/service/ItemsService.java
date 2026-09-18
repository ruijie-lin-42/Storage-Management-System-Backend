package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.common.converter.PageConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.StockAdjustmentType;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.BusinessException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.DataIntegrityException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.utils.SecurityUtils;
import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.request.ItemsQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.request.ItemsStockChangeRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.request.StockChangeRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.entity.Items;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.mapper.ItemsMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.ItemsResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.dto.CreateItemStockHistoryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.dto.CreateStockHistoryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.entity.StockHistory;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.service.StockHistoryService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.function.Function;

/**
 * <p>
 * service
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-05
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ItemsService extends ServiceImpl<ItemsMapper, Items> {

    private final ItemsMapper itemsMapper;
    private final StockHistoryService stockHistoryService;

    public PageResultResponse<ItemsResponse> queryInPage(ItemsQueryRequest itemsQueryRequest) {
        Page<ItemsResponse> page = new Page<>(itemsQueryRequest.getPageQueryRequest().getPageNum(), itemsQueryRequest.getPageQueryRequest().getPageSize());
        Page<ItemsResponse> result = itemsMapper.selectItemsDetail(page, itemsQueryRequest.getName(), itemsQueryRequest.getCategory(), itemsQueryRequest.getStorageId());
        return PageConverter.convert(result, Function.identity());
    }

    @Transactional
    public void changeStock(ItemsStockChangeRequest itemsStockChangeRequest) {
        int affectedRows;
        Long storageId = itemsStockChangeRequest.getStorageId();
        CreateStockHistoryDTO createStockHistoryDTO = getCreateStockAdjustmentHistoryDTO(itemsStockChangeRequest);
        StockHistory result = stockHistoryService.addStockAdjustmentHistory(createStockHistoryDTO);
        Long stockHistoryId = result.getId();
        for (StockChangeRequest stockChangeRequest : itemsStockChangeRequest.getChangedStocks()) {
            Items item = itemsMapper.selectForUpdate(stockChangeRequest.getItemId(), storageId);
            if (item == null) {
                if (stockChangeRequest.getChange() < 0) {
                    throw new BusinessException(ResultCode.INVALID_ITEM_STOCK, Map.of(
                            "storageId", storageId,
                            "itemId", stockChangeRequest.getItemId(),
                            "operatorId", createStockHistoryDTO.getCreatedBy(),
                            "currentStock", 0,
                            "requestedChange", stockChangeRequest.getChange(),
                            "expectedStockAfter", -stockChangeRequest.getChange()
                    ));
                }
                affectedRows = itemsMapper.insertItem(stockChangeRequest.getItemId(), storageId, stockChangeRequest.getChange());
                if (affectedRows != 1) {
                    throw new DataIntegrityException(ResultCode.INSERT_AFFECTED_ROWS_INVALID, 1, affectedRows, "changeStock", Map.of(
                            "storageId", storageId,
                            "itemId", stockChangeRequest.getItemId(),
                            "operatorId", createStockHistoryDTO.getCreatedBy()
                    ));
                }
            } else {
                int newCount = item.getCount() + stockChangeRequest.getChange();
                if (newCount < 0) {
                    throw new BusinessException(ResultCode.INVALID_ITEM_STOCK, Map.of(
                            "storageId", storageId,
                            "itemId", stockChangeRequest.getItemId(),
                            "operatorId", createStockHistoryDTO.getCreatedBy(),
                            "currentStock", item.getCount(),
                            "requestedChange", stockChangeRequest.getChange(),
                            "expectedStockAfter", newCount
                    ));
                }
                affectedRows = itemsMapper.updateItemCount(stockChangeRequest.getItemId(), storageId, newCount);
                if (affectedRows != 1) {
                    throw new DataIntegrityException(ResultCode.UPDATE_AFFECTED_ROWS_INVALID, 1, affectedRows, "changeStock", Map.of(
                            "storageId", storageId,
                            "itemId", stockChangeRequest.getItemId(),
                            "operatorId", createStockHistoryDTO.getCreatedBy()
                    ));
                }
            }
            CreateItemStockHistoryDTO createItemStockHistoryDTO = getCreateItemStockHistoryDTO(stockChangeRequest, stockHistoryId, item);
            stockHistoryService.addItemStockHistory(createItemStockHistoryDTO);
            log.info("Stock changed successfully, operatorId={}, stockHistory={}",
                    SecurityUtils.getUserIdFromContext(), result);
        }
    }

    // ==================== Helper Methods ====================

    private CreateStockHistoryDTO getCreateStockAdjustmentHistoryDTO(ItemsStockChangeRequest itemsStockChangeRequest) {
        CreateStockHistoryDTO createStockHistoryDTO = new CreateStockHistoryDTO();
        createStockHistoryDTO.setStorageId(itemsStockChangeRequest.getStorageId());
        createStockHistoryDTO.setCreatedBy(SecurityUtils.getUserIdFromContext());
        createStockHistoryDTO.setType(StockAdjustmentType.ADJUSTMENT);
        createStockHistoryDTO.setAmount(itemsStockChangeRequest.getChangedStocks().size());
        createStockHistoryDTO.setRemark(itemsStockChangeRequest.getRemark());
        return createStockHistoryDTO;
    }

    private CreateItemStockHistoryDTO getCreateItemStockHistoryDTO(StockChangeRequest stockChangeRequest, Long stockHistoryId, @Nullable Items item) {
        CreateItemStockHistoryDTO createItemStockHistoryDTO = new CreateItemStockHistoryDTO();
        createItemStockHistoryDTO.setStockHistoryId(stockHistoryId);
        createItemStockHistoryDTO.setItemId(stockChangeRequest.getItemId());
        createItemStockHistoryDTO.setStockBefore(item == null ? 0 : item.getCount());
        createItemStockHistoryDTO.setAmountChange(stockChangeRequest.getChange());
        createItemStockHistoryDTO.setStockAfter((item == null ? 0 : item.getCount()) + stockChangeRequest.getChange());
        return createItemStockHistoryDTO;
    }

}
