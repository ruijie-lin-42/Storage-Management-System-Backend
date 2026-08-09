package io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.request;

import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.constants.ItemStockHistoryConstraints;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemStockHistoryQueryByStockHistoryRequest {

    @NotNull
    private Long stockHistoryId;

    @NotNull
    @Size(max = ItemStockHistoryConstraints.ITEM_NAME_LENGTH_MAX)
    private String itemName;

}
