package io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request;

import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.constants.AuthConstraints;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotNull
    @NotBlank
    @Size(min = AuthConstraints.USERNAME_LENGTH_MIN, max = AuthConstraints.USERNAME_LENGTH_MAX)
    private String username;

    @NotNull
    @NotBlank
    @Size(min = AuthConstraints.PASSWORD_LENGTH_MIN, max = AuthConstraints.PASSWORD_LENGTH_MAX)
    private String oldPassword;

    @NotNull
    @NotBlank
    @Size(min = AuthConstraints.PASSWORD_LENGTH_MIN, max = AuthConstraints.PASSWORD_LENGTH_MAX)
    private String newPassword;

}
