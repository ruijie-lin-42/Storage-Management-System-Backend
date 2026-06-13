package io.github.ruijie_lin_42.storage_management_system_backend.items.controller;

import io.github.ruijie_lin_42.storage_management_system_backend.common.vo.PageResultVo;
import io.github.ruijie_lin_42.storage_management_system_backend.items.dto.ItemInfoDetailQueryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.items.dto.StorageItemInfoQueryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.items.service.ItemInfoService;
import io.github.ruijie_lin_42.storage_management_system_backend.items.vo.ItemInfoDetailVo;
import io.github.ruijie_lin_42.storage_management_system_backend.items.vo.StorageItemInfoVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public PageResultVo<ItemInfoDetailVo> queryInPage(@RequestBody @Valid ItemInfoDetailQueryDTO itemInfoDetailQueryDTO){
        return itemInfoService.queryItemInfoDetailInPage(itemInfoDetailQueryDTO);
    }

    @PostMapping("/search")
    public PageResultVo<StorageItemInfoVo> queryStorageItemInfo(@RequestBody @Valid StorageItemInfoQueryDTO storageItemInfoQueryDTO){
        return itemInfoService.queryStorageItemInfoInPage(storageItemInfoQueryDTO);
    }

}
