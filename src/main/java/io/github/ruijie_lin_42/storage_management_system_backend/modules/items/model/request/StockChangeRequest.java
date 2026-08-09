package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.request;

import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.constants.StockChangeConstraints;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockChangeRequest {

    @NotNull
    private Long itemId;

    @NotNull
    @Min(StockChangeConstraints.AMOUNT_CHANGE_MIN)
    @Max(StockChangeConstraints.AMOUNT_CHANGE_MAX)
    private Integer change;

}
