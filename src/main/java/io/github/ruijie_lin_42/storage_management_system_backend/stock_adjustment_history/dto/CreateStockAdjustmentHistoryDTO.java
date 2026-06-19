package io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.dto;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.StockAdjustmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateStockAdjustmentHistoryDTO {

    @NotNull
    private Long storageId;
    @NotNull
    private Long createdBy;
    @NotNull
    private StockAdjustmentType type;
    @NotNull
    private Integer amount;
    @NotNull
    @NotBlank
    private String remark;


}
