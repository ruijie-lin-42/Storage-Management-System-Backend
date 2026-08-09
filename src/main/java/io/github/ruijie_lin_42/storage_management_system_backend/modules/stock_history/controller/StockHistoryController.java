package io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.request.ItemStockHistoryQueryByStockHistoryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.request.ItemStockHistoryQueryByItemRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.request.StockHistoryQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.service.StockHistoryService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.response.ItemStockHistoryDetailResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.response.ItemStockHistoryResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.response.StockHistoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class StockHistoryController {

    private final StockHistoryService stockHistoryService;

    @PostMapping("/search")
    @Operation(description = "Get stock histories in page")
    public PageResultResponse<StockHistoryResponse> queryStockAdjustmentHistoryByStorageName
            (@RequestBody @Valid StockHistoryQueryRequest stockHistoryQueryRequest) {
        return stockHistoryService.queryStockAdjustmentHistoryByStorageName(stockHistoryQueryRequest);
    }

    @PostMapping("/itemStockHistory")
    @Operation(description = "Get exact items stock history from a stock history")
    public List<ItemStockHistoryResponse> queryItemStockHistoryByStockHistoryId
            (@RequestBody @Valid ItemStockHistoryQueryByStockHistoryRequest itemStockHistoryQueryByStockHistoryRequest){
        return stockHistoryService.queryItemStockHistoryByAdjustmentId(itemStockHistoryQueryByStockHistoryRequest);
    }

    @PostMapping("/item")
    @Operation(description = "Get stock history for an exact item")
    public PageResultResponse<ItemStockHistoryDetailResponse> queryItemStockHistoryByItem
            (@RequestBody @Valid ItemStockHistoryQueryByItemRequest itemStockHistoryQueryByItemRequest){
        return stockHistoryService.queryItemStockHistoryByItemId(itemStockHistoryQueryByItemRequest);
    }

    @GetMapping("/{id}/page")
    public Integer getStockHistoryPageNumById(@PathVariable("id") Long stockHistoryId, @RequestParam Integer pageSize){
        return stockHistoryService.getPageNumById(stockHistoryId, pageSize);
    }

}
