package io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.convert;

import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.entity.StockHistory;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.dto.CreateStockHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockHistoryConverter {

    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    StockHistory toEntity(CreateStockHistoryDTO createStockHistoryDTO);

}
