package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.request.ItemInfoDetailQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.request.StorageItemInfoQueryRequest;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.service.ItemInfoService;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.ItemInfoDetailResponse;
import io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response.StorageItemInfoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  controller
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-08
 */
@RestController
@RequestMapping("/itemInfo")
@RequiredArgsConstructor
public class ItemInfoController {

    private final ItemInfoService itemInfoService;

    @PostMapping("/searchDetail")
    public PageResultResponse<ItemInfoDetailResponse> queryInPage(@RequestBody @Valid ItemInfoDetailQueryRequest itemInfoDetailQueryRequest){
        return itemInfoService.queryItemInfoDetailInPage(itemInfoDetailQueryRequest);
    }

    @PostMapping("/search")
    public PageResultResponse<StorageItemInfoResponse> queryStorageItemInfo(@RequestBody @Valid StorageItemInfoQueryRequest storageItemInfoQueryRequest){
        return itemInfoService.queryStorageItemInfoInPage(storageItemInfoQueryRequest);
    }

    @GetMapping("/{id}/page")
    public Integer getStockHistoryPageNumById(@PathVariable("id") Long itemId, @RequestParam Integer pageSize){
        return itemInfoService.getPageNumByItemId(itemId, pageSize);
    }

}
