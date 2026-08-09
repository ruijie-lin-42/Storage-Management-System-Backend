package io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.response;

import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.response.UserQueryResponse;
import lombok.Data;

@Data
public class AuthResponse {

    private String accessToken;
    private UserQueryResponse user;

}
