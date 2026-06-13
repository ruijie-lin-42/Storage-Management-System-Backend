package io.github.ruijie_lin_42.storage_management_system_backend.items.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.common.converter.PageConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.common.dto.PageQueryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.common.vo.PageResultVo;
import io.github.ruijie_lin_42.storage_management_system_backend.items.convert.CategoryConverter;
import io.github.ruijie_lin_42.storage_management_system_backend.items.entity.Category;
import io.github.ruijie_lin_42.storage_management_system_backend.items.mapper.CategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.ruijie_lin_42.storage_management_system_backend.items.vo.CategoryTreeVo;
import io.github.ruijie_lin_42.storage_management_system_backend.items.vo.CategoryVo;
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

    public List<CategoryTreeVo> queryCategoryTree() {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());
        Map<Long, CategoryTreeVo> map = new HashMap<>();
        for (Category category : categories) {
            map.put(category.getId(), categoryConverter.toTreeVo(category));
        }
        List<CategoryTreeVo> result = new ArrayList<>();
        for (Category category : categories) {
            if (category.getParentId() == null) {
                result.add(map.get(category.getId()));
            } else {
                map.get(category.getParentId()).getChildren().add(map.get(category.getId()));
            }
        }
        return result;
    }

    public PageResultVo<CategoryVo> selectCategoryByName(String name){
        Page<Category> page = new Page<>(PageQueryDTO.DEFAULT_PAGE_NUM, PageQueryDTO.DEFAULT_PAGE_SIZE);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Category::getName, name);
        Page<Category> result = categoryMapper.selectPage(page, lambdaQueryWrapper);
        return PageConverter.convert(result, categoryConverter::toVo);
    }

}
