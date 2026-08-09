package io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.convert;

import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.request.CreateStorageRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.request.EditStorageRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.entity.Storage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StorageConverter {

    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    Storage toEntity(CreateStorageRequest createStorageRequest);

    @Mapping(target = "updatedAt", expression = "java(java.time.Instant.now())")
    Storage toEntity(EditStorageRequest editStorageRequest);

}
