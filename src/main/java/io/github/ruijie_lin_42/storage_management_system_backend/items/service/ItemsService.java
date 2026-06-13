package io.github.ruijie_lin_42.storage_management_system_backend.items.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.common.converter.PageConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.ApiException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions.DataIntegrityException;
import io.github.ruijie_lin_42.storage_management_system_backend.common.vo.PageResultVo;
import io.github.ruijie_lin_42.storage_management_system_backend.items.dto.ItemsQueryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.items.dto.ItemsStockChangeDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.items.dto.StockChangeDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.items.entity.Items;
import io.github.ruijie_lin_42.storage_management_system_backend.items.mapper.ItemsMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.ruijie_lin_42.storage_management_system_backend.items.vo.ItemsVo;
import lombok.RequiredArgsConstructor;
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

    public PageResultVo<ItemsVo> queryInPage(ItemsQueryDTO itemsQueryDTO) {
        Page<ItemsVo> page = new Page<>(itemsQueryDTO.getPageQueryDTO().getPageNum(), itemsQueryDTO.getPageQueryDTO().getPageSize());
        Page<ItemsVo> result = itemsMapper.selectItemsDetail(page, itemsQueryDTO.getName(), itemsQueryDTO.getCategory(), itemsQueryDTO.getStorageId());
        return PageConverter.convert(result, Function.identity());
    }

    @Transactional
    public Integer changeStock(ItemsStockChangeDTO itemsStockChangeDTO) {
        int affectedRows, totalAffectedRows = 0;
        Long storageId = itemsStockChangeDTO.getStorageId();
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
        }
        return totalAffectedRows;
    }

}
