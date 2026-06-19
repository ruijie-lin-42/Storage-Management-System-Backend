package io.github.ruijie_lin_42.storage_management_system_backend.items.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.common.converter.PageConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.StockAdjustmentType;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.ApiException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.DataIntegrityException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.utils.SecurityUtils;
import io.github.ruijie_lin_42.storage_management_system_backend.common.vo.PageResultVo;
import io.github.ruijie_lin_42.storage_management_system_backend.items.dto.ItemsQueryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.items.dto.ItemsStockChangeDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.items.dto.StockChangeDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.items.entity.Items;
import io.github.ruijie_lin_42.storage_management_system_backend.items.mapper.ItemsMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.ruijie_lin_42.storage_management_system_backend.items.vo.ItemsVo;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.dto.CreateItemStockHistoryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.dto.CreateStockAdjustmentHistoryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.entity.StockAdjustmentHistory;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.service.StockAdjustmentHistoryService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class ItemsService extends ServiceImpl<ItemsMapper, Items> {

    private final ItemsMapper itemsMapper;
    private final StockAdjustmentHistoryService stockAdjustmentHistoryService;

    public PageResultVo<ItemsVo> queryInPage(ItemsQueryDTO itemsQueryDTO) {
        Page<ItemsVo> page = new Page<>(itemsQueryDTO.getPageQueryDTO().getPageNum(), itemsQueryDTO.getPageQueryDTO().getPageSize());
        Page<ItemsVo> result = itemsMapper.selectItemsDetail(page, itemsQueryDTO.getName(), itemsQueryDTO.getCategory(), itemsQueryDTO.getStorageId());
        return PageConverter.convert(result, Function.identity());
    }

    @Transactional
    public Integer changeStock(ItemsStockChangeDTO itemsStockChangeDTO) {
        int affectedRows, totalAffectedRows = 0;
        Long storageId = itemsStockChangeDTO.getStorageId();
        CreateStockAdjustmentHistoryDTO createStockAdjustmentHistoryDTO = getCreateStockAdjustmentHistoryDTO(itemsStockChangeDTO);
        StockAdjustmentHistory result = stockAdjustmentHistoryService.addStockAdjustmentHistory(createStockAdjustmentHistoryDTO);
        Long adjustmentId = result.getId();
        for (StockChangeDTO stockChangeDTO : itemsStockChangeDTO.getChangedStocks()) {
            Items item = itemsMapper.selectForUpdate(stockChangeDTO.getItemId(), storageId);
            if (item == null) {
                if (stockChangeDTO.getChange() < 0) {
                    throw new ApiException(ResultCode.INVALID_ITEM_STOCK);
                }
                affectedRows = itemsMapper.insertItem(stockChangeDTO.getItemId(), storageId, stockChangeDTO.getChange());
                if(affectedRows > 1 || affectedRows < 0){
                    throw new DataIntegrityException(ResultCode.INSERT_AFFECTED_ROWS_INVALID);
                }
            } else {
                int newCount = item.getCount() + stockChangeDTO.getChange();
                if (newCount < 0) {
                    throw new ApiException(ResultCode.INVALID_ITEM_STOCK);
                }
                affectedRows = itemsMapper.updateItemCount(stockChangeDTO.getItemId(), storageId, newCount);
                if(affectedRows > 1 || affectedRows < 0){
                    throw new DataIntegrityException(ResultCode.UPDATE_AFFECTED_ROWS_INVALID);
                }
            }
            totalAffectedRows += affectedRows;
            CreateItemStockHistoryDTO createItemStockHistoryDTO = getCreateItemStockHistoryDTO(stockChangeDTO, adjustmentId, item);
            stockAdjustmentHistoryService.addItemStockHistory(createItemStockHistoryDTO);
        }
        return totalAffectedRows;
    }

    /**
     * Helper methods
     */

    private CreateStockAdjustmentHistoryDTO getCreateStockAdjustmentHistoryDTO(ItemsStockChangeDTO itemsStockChangeDTO){
        CreateStockAdjustmentHistoryDTO createStockAdjustmentHistoryDTO = new CreateStockAdjustmentHistoryDTO();
        createStockAdjustmentHistoryDTO.setStorageId(itemsStockChangeDTO.getStorageId());
        createStockAdjustmentHistoryDTO.setCreatedBy(SecurityUtils.getUserIdFromContext());
        createStockAdjustmentHistoryDTO.setType(StockAdjustmentType.ADJUSTMENT);
        createStockAdjustmentHistoryDTO.setAmount(itemsStockChangeDTO.getChangedStocks().size());
        createStockAdjustmentHistoryDTO.setRemark(itemsStockChangeDTO.getRemark());
        return createStockAdjustmentHistoryDTO;
    }

    private CreateItemStockHistoryDTO getCreateItemStockHistoryDTO(StockChangeDTO stockChangeDTO, Long adjustmentId, @Nullable Items item){
        CreateItemStockHistoryDTO createItemStockHistoryDTO = new CreateItemStockHistoryDTO();
        createItemStockHistoryDTO.setAdjustmentId(adjustmentId);
        createItemStockHistoryDTO.setItemId(stockChangeDTO.getItemId());
        createItemStockHistoryDTO.setStockBefore(item == null ? 0 : item.getCount());
        createItemStockHistoryDTO.setAmountChange(stockChangeDTO.getChange());
        createItemStockHistoryDTO.setStockAfter((item == null ? 0 : item.getCount()) + stockChangeDTO.getChange());
        return createItemStockHistoryDTO;
    }

}
