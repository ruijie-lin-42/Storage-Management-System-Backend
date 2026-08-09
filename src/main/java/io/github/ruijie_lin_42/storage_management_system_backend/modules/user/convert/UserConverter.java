package io.github.ruijie_lin_42.storage_management_system_backend.modules.user.convert;

import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.dto.UserAuthDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.request.CreateUserRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.request.DashboardEditUserRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.request.ProfileEditUserRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.entity.User;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.response.UserQueryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserConverter {

    @Mapping(target = "status", expression = "java(io.github.ruijie_lin_42.storage_management_system_backend.common.enums.Status.VALID)")
    @Mapping(target = "password", source = "passwordHash")
    User toEntity(CreateUserRequest createUserRequest, String passwordHash);

    User toEntity(DashboardEditUserRequest dashboardEditUserRequest);

    User toEntity(ProfileEditUserRequest profileEditUserRequest);

    UserQueryResponse toQueryResponse(User user);

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "passwordHash", source = "password")
    UserAuthDTO toAutoDTO(User user);

}
