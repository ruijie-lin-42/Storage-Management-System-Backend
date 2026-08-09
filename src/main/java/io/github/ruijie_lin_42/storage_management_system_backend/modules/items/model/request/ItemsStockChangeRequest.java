package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.request;

import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.constants.StockChangeConstraints;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ItemsStockChangeRequest {

    @NotNull
    private Long storageId;

    @NotNull
    @Size(min=StockChangeConstraints.STOCK_CHANGE_LIST_MIN)
    @Valid
    private List<@NotNull StockChangeRequest> changedStocks;

    @NotNull
    @NotBlank
    @Size(min = StockChangeConstraints.REMARK_LENGTH_MIN, max = StockChangeConstraints.REMARK_LENGTH_MAX)
    private String remark;

}
