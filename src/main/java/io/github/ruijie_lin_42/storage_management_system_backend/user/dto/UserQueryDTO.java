package io.github.ruijie_lin_42.storage_management_system_backend.user.dto;

import io.github.ruijie_lin_42.storage_management_system_backend.common.dto.PageQueryDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserQueryDTO {
    @NotNull
    private PageQueryDTO pageQueryDTO;
    @NotNull
    private String name;
}
