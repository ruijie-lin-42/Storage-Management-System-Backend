package io.github.ruijie_lin_42.storage_management_system_backend.items.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockChangeDTO {

    @NotNull
    private Long itemId;
    @NotNull
    @Min(Integer.MIN_VALUE)
    @Max(Integer.MAX_VALUE)
    private Integer change;

}
