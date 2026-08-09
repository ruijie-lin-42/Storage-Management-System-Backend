package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.common.converter.PageConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.common.request.PageQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.convert.CategoryConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.entity.Category;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.mapper.CategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.CategoryResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.CategoryTreeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * service
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-07
 */
@Service
@RequiredArgsConstructor
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    private final CategoryMapper categoryMapper;
    private final CategoryConverter categoryConverter;

    public List<CategoryTreeResponse> queryCategoryTree() {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());
        Map<Long, CategoryTreeResponse> map = new HashMap<>();
        for (Category category : categories) {
            map.put(category.getId(), categoryConverter.toTreeVo(category));
        }
        List<CategoryTreeResponse> result = new ArrayList<>();
        for (Category category : categories) {
            if (category.getParentId() == null) {
                result.add(map.get(category.getId()));
            } else {
                map.get(category.getParentId()).getChildren().add(map.get(category.getId()));
            }
        }
        return result;
    }

    public PageResultResponse<CategoryResponse> selectCategoryByName(String name){
        Page<Category> page = new Page<>(PageQueryRequest.DEFAULT_PAGE_NUM, PageQueryRequest.DEFAULT_PAGE_SIZE);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Category::getName, name);
        Page<Category> result = categoryMapper.selectPage(page, lambdaQueryWrapper);
        return PageConverter.convert(result, categoryConverter::toVo);
    }

}
