package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response;

import lombok.Data;

@Data
public class ItemInfoDetailResponse {

    private Long id;
    private String name;
    private String description;
    private String category;
    private String unit;
    private String sku;
    private Integer price;

}
