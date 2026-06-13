package io.github.ruijie_lin_42.storage_management_system_backend.items.vo;

import lombok.Data;

@Data
public class StorageItemInfoVo {

    private Long id;
    private String name;
    private String sku;
    private String unit;
    private Integer count;

}
