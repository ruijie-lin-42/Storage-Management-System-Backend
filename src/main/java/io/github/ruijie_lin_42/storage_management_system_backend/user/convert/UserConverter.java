package io.github.ruijie_lin_42.storage_management_system_backend.user.convert;

import io.github.ruijie_lin_42.storage_management_system_backend.user.dto.ProfileEditUserDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserConverter {

    @Mapping(target = "id", source = "userId")
    public User toEntity(ProfileEditUserDTO profileEditUserDTO, Long userId);

}
