package io.github.ruijie_lin_42.storage_management_system_backend.items.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ItemsStockChangeDTO {

    @NotNull
    private Long storageId;
    @NotNull
    private List<StockChangeDTO> changedStocks;
    @NotNull
    @NotBlank
    private String remark;

}
