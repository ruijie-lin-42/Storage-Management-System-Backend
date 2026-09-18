package io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.convert;

import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.dto.LoginRequestDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.dto.LogoutRequestDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.auth.model.request.LoginRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthConverter {

    LoginRequestDTO toLoginRequestDTO(LoginRequest loginRequest, String ip, String userAgent);

    LogoutRequestDTO toLogoutRequestDTO(String tokenValue, String ip, String userAgent);

}
