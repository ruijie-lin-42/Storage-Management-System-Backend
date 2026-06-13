package io.github.ruijie_lin_42.storage_management_system_backend.items.dto;

import io.github.ruijie_lin_42.storage_management_system_backend.common.dto.PageQueryDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StorageItemInfoQueryDTO {

    @NotNull
    private PageQueryDTO pageQueryDTO;
    @NotNull
    private String name;
    @NotNull
    private Long storageId;

}
