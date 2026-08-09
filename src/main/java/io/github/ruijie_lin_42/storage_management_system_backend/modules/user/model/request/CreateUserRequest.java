package io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.request;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.Role;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.constants.UserConstraints;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotNull
    @NotBlank
    @Size(min = UserConstraints.USERNAME_LENGTH_MIN, max = UserConstraints.USERNAME_LENGTH_MAX)
    private String username;

    @NotNull
    @NotBlank
    @Size(min = UserConstraints.NAME_LENGTH_MIN, max = UserConstraints.NAME_LENGTH_MAX)
    private String name;

    @NotNull
    @NotBlank
    @Size(min = UserConstraints.PASSWORD_LENGTH_MIN, max = UserConstraints.PASSWORD_LENGTH_MAX)
    private String password;

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

    @NotNull
    private Role role;

}
