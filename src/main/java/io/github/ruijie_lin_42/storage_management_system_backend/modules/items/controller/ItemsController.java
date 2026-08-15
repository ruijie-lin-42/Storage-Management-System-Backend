package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.ApiErrorResponseExample;
import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.CommonErrorApiResponses;
import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.RequiresAuthApiResponses;
import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.request.ItemsQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.request.ItemsStockChangeRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.service.ItemsService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.ItemsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  controller
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-05
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Tag(name = "Item Management", description = "Endpoints for managing items that are in a storage")
public class ItemsController {

    private final ItemsService itemsService;

    @PostMapping("/search")
    @Operation(summary = "Fuzzy search for items that are in a specified storage with pagination",
            description = """
                    Fuzzy search for items in the storage specified by name and category with pagination;\s\s
                    No limitations on user roles""")
    @ApiResponse(responseCode = "200", description = "Items retrieved successfully")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public PageResultResponse<ItemsResponse> queryInPage(@RequestBody @Valid ItemsQueryRequest itemsQueryRequest){
        return itemsService.queryInPage(itemsQueryRequest);
    }

    @PostMapping("/changeStock")
    @Operation(summary = "Change the stock count for an item in a storage",
            description = """
                    Change the stock count for a list of items;\s\s
                    Items that are decreasing stocks must have enough stock count, otherwise the whole request would fail;\s\s
                    No limitations on user roles""")
    @ApiResponse(responseCode = "200", description = "Stock count for all specified items are changed successfully")
    @ApiResponse(responseCode = "400", description = "Item does not have enough stock to decrease")
    @ApiErrorResponseExample(responseCode = "400", resultCode = ResultCode.INVALID_ITEM_STOCK)
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public Void changeStock(@RequestBody @Valid ItemsStockChangeRequest itemsStockChangeRequest){
        itemsService.changeStock(itemsStockChangeRequest);
        return null;
    }

}
