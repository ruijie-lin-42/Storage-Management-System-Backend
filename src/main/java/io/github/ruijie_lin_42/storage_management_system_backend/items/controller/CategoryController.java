package io.github.ruijie_lin_42.storage_management_system_backend.items.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.vo.PageResultVo;
import io.github.ruijie_lin_42.storage_management_system_backend.items.service.CategoryService;
import io.github.ruijie_lin_42.storage_management_system_backend.items.vo.CategoryTreeVo;
import io.github.ruijie_lin_42.storage_management_system_backend.items.vo.CategoryVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  controller
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-08
 */
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/tree")
    public List<CategoryTreeVo> queryCategoryTree(){
        return categoryService.queryCategoryTree();
    }

    @GetMapping("")
    public PageResultVo<CategoryVo> queryCategoryByName(@RequestParam String name){
        return categoryService.selectCategoryByName(name);
    }

}
