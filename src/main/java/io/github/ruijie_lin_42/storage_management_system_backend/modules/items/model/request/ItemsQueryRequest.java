package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.request;

import io.github.ruijie_lin_42.storage_management_system_backend.common.request.PageQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.constants.ItemInfoConstraints;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemsQueryRequest {

    @NotNull
    @Valid
    private PageQueryRequest pageQueryRequest;

    @NotNull
    private Long storageId;

    @NotNull
    @Size(max = ItemInfoConstraints.NAME_LENGTH_MAX)
    private String name;

    @NotNull
    @Size(max = ItemInfoConstraints.NAME_LENGTH_MAX)
    private String category;

}
