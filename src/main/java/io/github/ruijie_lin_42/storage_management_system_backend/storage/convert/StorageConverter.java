package io.github.ruijie_lin_42.storage_management_system_backend.storage.convert;

import io.github.ruijie_lin_42.storage_management_system_backend.storage.dto.CreateStorageDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.dto.EditStorageDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.entity.Storage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StorageConverter {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Storage toEntity(CreateStorageDTO createStorageDTO);

    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Storage toEntity(EditStorageDTO editStorageDTO);

}
