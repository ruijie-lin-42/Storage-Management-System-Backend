package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.CommonErrorApiResponses;
import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.RequiresAuthApiResponses;
import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.service.CategoryService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.CategoryTreeResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * controller
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-08
 */
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Tag(name = "Category query", description = "Endpoints for querying for items' categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/tree")
    @Operation(summary = "Get all categories as a tree",
            description = """
                    Returns all categories as a tree;\s\s
                    This is a read-only operation;\s\s
                    No limitations on user roles""")
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully as a tree")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public List<CategoryTreeResponse> queryCategoryTree() {
        return categoryService.queryCategoryTree();
    }

    @GetMapping("")
    @Operation(summary = "Search for categories",
            description = """
                    Fuzzy search for categories by name with pagination;\s\s
                    This is a read-only operation;\s\s
                    No limitations on user roles""",
            parameters = {@Parameter(name = "name", description = "Search keyword, category's name", example = "Electronics")})
    @ApiResponse(responseCode = "200", description = "Categories found retrieved successfully with pagination")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public PageResultResponse<CategoryResponse> queryCategoryByName(@RequestParam String name) {
        return categoryService.selectCategoryByName(name);
    }

}
