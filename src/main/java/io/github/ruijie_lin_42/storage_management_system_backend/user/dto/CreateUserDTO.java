package io.github.ruijie_lin_42.storage_management_system_backend.user.dto;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserDTO {

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String username;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;
    @NotNull
    @NotBlank
    @Size(min = 8)
    private String password;
    @Min(1)
    private int age;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 20)
    private String gender;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    private Role role;
}
