package io.github.ruijie_lin_42.storage_management_system_backend.user.dto;

import io.github.ruijie_lin_42.storage_management_system_backend.common.dto.PageQueryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.user.constants.UserConstraints;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserQueryDTO {
    @NotNull
    private PageQueryDTO pageQueryDTO;
    @NotNull
    @Size(max = UserConstraints.NAME_LENGTH_MAX)
    private String name;
}
