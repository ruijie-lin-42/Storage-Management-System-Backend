package io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.convert;

import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.dto.CreateItemStockHistoryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.entity.ItemStockHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemStockHistoryConverter {

    public ItemStockHistory toEntity(CreateItemStockHistoryDTO createItemStockHistoryDTO);

}
