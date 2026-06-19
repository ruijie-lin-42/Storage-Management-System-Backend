package io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.convert;

import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.dto.CreateStockAdjustmentHistoryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.entity.StockAdjustmentHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockAdjustmentHistoryConverter {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    public StockAdjustmentHistory toEntity(CreateStockAdjustmentHistoryDTO createStockAdjustmentHistoryDTO);

}
