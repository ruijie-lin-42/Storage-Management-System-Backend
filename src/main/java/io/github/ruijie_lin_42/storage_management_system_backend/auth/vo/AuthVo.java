package io.github.ruijie_lin_42.storage_management_system_backend.auth.vo;

import io.github.ruijie_lin_42.storage_management_system_backend.user.vo.UserVo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthVo {

    @NotNull
    @NotBlank
    private String accessToken;
    @NotNull
    private UserVo user;

}
