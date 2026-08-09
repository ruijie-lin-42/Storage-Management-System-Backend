package io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.request;

import io.github.ruijie_lin_42.storage_management_system_backend.common.request.PageQueryRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockHistoryQueryRequest {

    @NotNull
    @Valid
    private PageQueryRequest pageQueryRequest;

    @NotNull
    private String storageName;

}
