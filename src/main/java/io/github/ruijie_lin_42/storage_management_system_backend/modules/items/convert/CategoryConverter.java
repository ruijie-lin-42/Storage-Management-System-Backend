package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.convert;

import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.entity.Category;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.CategoryTreeResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryConverter {

    @Mapping(target = "children", expression = "java(new java.util.ArrayList<CategoryTreeResponse>())")
    CategoryTreeResponse toTreeVo(Category category);

    CategoryResponse toVo(Category category);

}
