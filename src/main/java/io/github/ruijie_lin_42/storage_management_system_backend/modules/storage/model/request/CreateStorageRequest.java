package io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.request;

import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.constants.StorageConstraints;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateStorageRequest {

    @NotNull
    @NotBlank
    @Size(min = StorageConstraints.NAME_LENGTH_MIN, max = StorageConstraints.NAME_LENGTH_MAX)
    private String name;

    @NotNull
    @NotBlank
    @Size(min = StorageConstraints.ADDRESS_LENGTH_MIN, max = StorageConstraints.ADDRESS_LENGTH_MAX)
    private String address;

    @NotNull
    private Long managerId;

    @NotNull
    private Long createdBy;

    @Size(max = StorageConstraints.REMARK_LENGTH_MAX)
    private String remark;

}
