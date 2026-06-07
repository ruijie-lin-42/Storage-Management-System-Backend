package io.github.ruijie_lin_42.storage_management_system_backend.storage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditStorageDTO {

    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String address;
    @NotNull
    private Long managerId;
    @NotNull
    private Long updatedBy;
    private String remark;

}
