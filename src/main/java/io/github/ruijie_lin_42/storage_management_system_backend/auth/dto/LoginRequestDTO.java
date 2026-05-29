package io.github.ruijie_lin_42.storage_management_system_backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    private String password;

}
