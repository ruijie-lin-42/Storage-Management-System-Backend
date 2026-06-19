package io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.dto;

import io.github.ruijie_lin_42.storage_management_system_backend.common.dto.PageQueryDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemStockHistoryQueryByItemDTO {

    @NotNull
    private PageQueryDTO pageQueryDTO;
    @NotNull
    private Long itemId;

}
