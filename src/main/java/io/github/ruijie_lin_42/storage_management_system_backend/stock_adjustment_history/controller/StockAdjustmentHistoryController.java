package io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.vo.PageResultVo;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.dto.CreateStockAdjustmentHistoryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.dto.ItemStockHistoryQueryByAdjustmentDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.dto.ItemStockHistoryQueryByItemDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.dto.StockAdjustmentHistoryQueryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.service.StockAdjustmentHistoryService;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.vo.ItemStockHistoryDetailVo;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.vo.ItemStockHistoryVo;
import io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.vo.StockAdjustmentHistoryVo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
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
@RequestMapping("/stockAdjustmentHistory")
@RequiredArgsConstructor
public class StockAdjustmentHistoryController {

    private final StockAdjustmentHistoryService stockAdjustmentHistoryService;

    @PostMapping("/search")
    @Operation(description = "Get stock adjustment histories in page")
    public PageResultVo<StockAdjustmentHistoryVo> queryStockAdjustmentHistoryByStorageName
            (@RequestBody @Valid StockAdjustmentHistoryQueryDTO stockAdjustmentHistoryQueryDTO) {
        return stockAdjustmentHistoryService.queryStockAdjustmentHistoryByStorageName(stockAdjustmentHistoryQueryDTO);
    }

    @PostMapping("/adjustment")
    @Operation(description = "Get exact items stock history in an adjustment")
    public List<ItemStockHistoryVo> queryItemStockHistoryByAdjustment
            (@RequestBody @Valid ItemStockHistoryQueryByAdjustmentDTO itemStockHistoryQueryByAdjustmentDTO){
        return stockAdjustmentHistoryService.queryItemStockHistoryByAdjustmentId(itemStockHistoryQueryByAdjustmentDTO);
    }

    @PostMapping("/item")
    @Operation(description = "Get stock history for an exact item")
    public PageResultVo<ItemStockHistoryDetailVo> queryItemStockHistoryByItem
            (@RequestBody @Valid ItemStockHistoryQueryByItemDTO itemStockHistoryQueryByItemDTO){
        return stockAdjustmentHistoryService.queryItemStockHistoryByItemId(itemStockHistoryQueryByItemDTO);
    }

    @GetMapping("/{id}/page")
    public Integer getStockAdjustmentHistoryPageNumById(@PathVariable("id") Long adjustmentId, @RequestParam Integer pageSize){
        return stockAdjustmentHistoryService.getPageNumByAdjustmentId(adjustmentId, pageSize);
    }

}
