package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.service.CategoryService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.CategoryTreeResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.CategoryResponse;
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
    public List<CategoryTreeResponse> queryCategoryTree(){
        return categoryService.queryCategoryTree();
    }

    @GetMapping("")
    public PageResultResponse<CategoryResponse> queryCategoryByName(@RequestParam String name){
        return categoryService.selectCategoryByName(name);
    }

}
