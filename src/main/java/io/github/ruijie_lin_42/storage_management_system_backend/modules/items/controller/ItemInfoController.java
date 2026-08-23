package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.CommonErrorApiResponses;
import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.RequiresAuthApiResponses;
import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.request.ItemInfoDetailQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.request.StorageItemInfoQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.service.ItemInfoService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.ItemInfoDetailResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.StorageItemInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * controller
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-08
 */
@RestController
@RequestMapping("/itemInfo")
@RequiredArgsConstructor
@Tag(name = "Item Info Management", description = "Endpoints for managing items' detailed info")
public class ItemInfoController {

    private final ItemInfoService itemInfoService;

    @PostMapping("/searchDetail")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Query for an item's detail info",
            description = """
                    Query for an item's basic detailed information, without storage related fields (like count);\s\s
                    Query based on item's name keyword and precise category name;\s\s
                    Requires at least USER role\s\s""")
    @ApiResponse(responseCode = "200", description = "Item info retrieved successfully with pagination")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public PageResultResponse<ItemInfoDetailResponse> queryInPage(@RequestBody @Valid ItemInfoDetailQueryRequest itemInfoDetailQueryRequest) {
        return itemInfoService.queryItemInfoDetailInPage(itemInfoDetailQueryRequest);
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Query for an item's info from storage's perspective",
            description = """
                    Query for an item's information, with field count, but not as detailed as GET /searchDetail;\s\s
                    Requires at least USER role;\s\s
                    See response body schema for differences""")
    @ApiResponse(responseCode = "200", description = "Item info retrieved successfully")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public PageResultResponse<StorageItemInfoResponse> queryStorageItemInfo(@RequestBody @Valid StorageItemInfoQueryRequest storageItemInfoQueryRequest) {
        return itemInfoService.queryStorageItemInfoInPage(storageItemInfoQueryRequest);
    }

    @GetMapping("/{id}/page")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Query for the position that specified item should be in Item Info page",
            description = """
                    Used for jumping from stock history to item info page to see more detailed information about the item;\s\s
                    Requires at least USER role""",
            parameters = {@Parameter(name = "id", description = "Item's id, which is being aimed at", example = "1"),
                    @Parameter(name = "pageSize", description = "The number of items listed on one page of item info", example = "10")})
    @ApiResponse(responseCode = "200", description = "Page number retrieved successfully")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public Integer getStockHistoryPageNumById(@PathVariable("id") Long itemId, @RequestParam Integer pageSize) {
        return itemInfoService.getPageNumByItemId(itemId, pageSize);
    }

}
