package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response;

import lombok.Data;

@Data
public class StorageItemInfoResponse {

    private Long id;
    private String name;
    private String sku;
    private String unit;
    private Integer count;

}
