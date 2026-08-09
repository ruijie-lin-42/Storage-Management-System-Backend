package io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.convert;

import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.dto.CreateItemStockHistoryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.entity.ItemStockHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemStockHistoryConverter {

    ItemStockHistory toEntity(CreateItemStockHistoryDTO createItemStockHistoryDTO);

}
