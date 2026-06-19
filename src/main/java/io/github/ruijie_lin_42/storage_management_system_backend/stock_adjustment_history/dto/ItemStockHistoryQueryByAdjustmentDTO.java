package io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemStockHistoryQueryByAdjustmentDTO {

    @NotNull
    private Long adjustmentId;
    @NotNull
    private String itemName;

}
