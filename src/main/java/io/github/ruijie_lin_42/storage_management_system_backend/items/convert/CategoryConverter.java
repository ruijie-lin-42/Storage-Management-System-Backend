package io.github.ruijie_lin_42.storage_management_system_backend.items.convert;

import io.github.ruijie_lin_42.storage_management_system_backend.items.entity.Category;
import io.github.ruijie_lin_42.storage_management_system_backend.items.vo.CategoryTreeVo;
import io.github.ruijie_lin_42.storage_management_system_backend.items.vo.CategoryVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryConverter {

    @Mapping(target = "children", expression = "java(new java.util.ArrayList<CategoryTreeVo>())")
    public CategoryTreeVo toTreeVo(Category category);

    public CategoryVo toVo(Category category);

}
