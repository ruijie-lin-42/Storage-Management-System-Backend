package io.github.ruijie_lin_42.storage_management_system_backend.user.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EditUserDTO {

    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;
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
}
