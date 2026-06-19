package io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateItemStockHistoryDTO {

    @NotNull
    private Long adjustmentId;
    @NotNull
    private Long itemId;
    @NotNull
    @Min(0)
    @Max(Integer.MAX_VALUE)
    private Integer stockBefore;
    @Min(Integer.MIN_VALUE)
    @Max(Integer.MAX_VALUE)
    private Integer amountChange;
    @Min(1)
    @Max(Integer.MAX_VALUE)
    private Integer stockAfter;

}
