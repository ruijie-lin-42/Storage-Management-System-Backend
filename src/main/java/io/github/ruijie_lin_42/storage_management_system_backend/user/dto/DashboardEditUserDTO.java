package io.github.ruijie_lin_42.storage_management_system_backend.user.dto;

import io.github.ruijie_lin_42.storage_management_system_backend.user.constants.UserConstraints;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DashboardEditUserDTO {

    @NotNull
    @NotBlank
    @Size(min = UserConstraints.NAME_LENGTH_MIN, max = UserConstraints.NAME_LENGTH_MAX)
    private String name;
    @NotNull
    @Min(UserConstraints.AGE_MIN)
    @Max(UserConstraints.AGE_MAX)
    private Integer age;
    @NotNull
    @NotBlank
    @Size(min = UserConstraints.GENDER_LENGTH_MIN, max = UserConstraints.GENDER_LENGTH_MAX)
    private String gender;
    @NotNull
    @NotBlank
    @Email
    @Size(max = UserConstraints.EMAIL_LENGTH_MAX)
    private String email;
}
