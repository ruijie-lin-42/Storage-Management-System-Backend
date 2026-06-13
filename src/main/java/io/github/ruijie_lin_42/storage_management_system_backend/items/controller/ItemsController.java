package io.github.ruijie_lin_42.storage_management_system_backend.items.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.vo.PageResultVo;
import io.github.ruijie_lin_42.storage_management_system_backend.items.dto.ItemsQueryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.items.dto.ItemsStockChangeDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.items.service.ItemsService;
import io.github.ruijie_lin_42.storage_management_system_backend.items.vo.ItemsVo;
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
    public PageResultVo<ItemsVo> queryInPage(@RequestBody @Valid ItemsQueryDTO itemsQueryDTO){
        return itemsService.queryInPage(itemsQueryDTO);
    }

    @PostMapping("/changeStock")
    public Void changeStock(@RequestBody @Valid ItemsStockChangeDTO itemsStockChangeDTO){
        itemsService.changeStock(itemsStockChangeDTO);
        return null;
    }

}
