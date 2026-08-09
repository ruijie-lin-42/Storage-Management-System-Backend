package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.request.ItemsQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.request.ItemsStockChangeRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.service.ItemsService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.ItemsResponse;
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
public class ItemsController {

    private final ItemsService itemsService;

    @PostMapping("/search")
    public PageResultResponse<ItemsResponse> queryInPage(@RequestBody @Valid ItemsQueryRequest itemsQueryRequest){
        return itemsService.queryInPage(itemsQueryRequest);
    }

    @PostMapping("/changeStock")
    public Void changeStock(@RequestBody @Valid ItemsStockChangeRequest itemsStockChangeRequest){
        itemsService.changeStock(itemsStockChangeRequest);
        return null;
    }

}
