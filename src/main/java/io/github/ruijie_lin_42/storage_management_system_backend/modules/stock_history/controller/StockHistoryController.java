package io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.CommonErrorApiResponses;
import io.github.ruijie_lin_42.storage_management_system_backend.common.openapi.RequiresAuthApiResponses;
import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.request.ItemStockHistoryQueryByStockHistoryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.request.ItemStockHistoryQueryByItemRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.request.StockHistoryQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.service.StockHistoryService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.response.ItemStockHistoryDetailResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.response.ItemStockHistoryResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.response.StockHistoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * controller
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-13
 */
@RestController
@RequestMapping("/stockHistory")
@RequiredArgsConstructor
@Tag(name = "Stock History Management", description = "Endpoints for managing stock histories")
public class StockHistoryController {

    private final StockHistoryService stockHistoryService;

    @PostMapping("/search")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Fuzzy search for a storage's stock history",
            description = """
                    Used for show simple information about the storage's stock history;\s\s
                    e.g. storage1's stock changed by xxx at xxx;\s\s
                    Requires at least USER role""")
    @ApiResponse(responseCode = "200", description = "Stock history retrieved successfully")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public PageResultResponse<StockHistoryResponse> queryStockAdjustmentHistoryByStorageName
            (@RequestBody @Valid StockHistoryQueryRequest stockHistoryQueryRequest) {
        return stockHistoryService.queryStockAdjustmentHistoryByStorageName(stockHistoryQueryRequest);
    }

    @PostMapping("/itemStockHistory")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Query for all the items in a stock history record",
            description = """
                    Query for detailed items stock changes in a stock history;\s\s
                    e.g. what items' stock counts changed in one record of storage1's stock change history;\s\s
                    Requires at least USER role""")
    @ApiResponse(responseCode = "200", description = "Items info in a stock history record retrieved successfully")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public List<ItemStockHistoryResponse> queryItemStockHistoryByStockHistoryId
            (@RequestBody @Valid ItemStockHistoryQueryByStockHistoryRequest itemStockHistoryQueryByStockHistoryRequest) {
        return stockHistoryService.queryItemStockHistoryByAdjustmentId(itemStockHistoryQueryByStockHistoryRequest);
    }

    @PostMapping("/item")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Query for all the stock history for an exact item",
            description = """
                    Given the item's id, return all of its stock history;\s\s
                    Requires at least USER role""")
    @ApiResponse(responseCode = "200", description = "Stock history for an exact item retrieved successfully")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public PageResultResponse<ItemStockHistoryDetailResponse> queryItemStockHistoryByItem
            (@RequestBody @Valid ItemStockHistoryQueryByItemRequest itemStockHistoryQueryByItemRequest) {
        return stockHistoryService.queryItemStockHistoryByItemId(itemStockHistoryQueryByItemRequest);
    }

    @GetMapping("/{id}/page")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Query for the position that specified stock history should be in the Stock History page",
            description = """
                    Used for jumping from item info to stock history page to see more info;\s\s
                    Requires at least USER role""",
            parameters = {@Parameter(name = "id", description = "Stock history's id", example = "1"),
                    @Parameter(name = "pageSize", description = "The number of stock histories on one page of stock history", example = "10")})
    @ApiResponse(responseCode = "200", description = "Page number retrieved successfully")
    @CommonErrorApiResponses
    @RequiresAuthApiResponses
    public Integer getStockHistoryPageNumById(@PathVariable("id") Long stockHistoryId, @RequestParam Integer pageSize) {
        return stockHistoryService.getPageNumById(stockHistoryId, pageSize);
    }

}
