package io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.request;

import io.github.ruijie_lin_42.storage_management_system_backend.common.request.PageQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.constants.StorageConstraints;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StorageQueryRequest {

    @NotNull
    @Valid
    private PageQueryRequest pageQueryRequest;

    @NotNull
    @Size(max = StorageConstraints.NAME_LENGTH_MAX)
    private String name;

}
