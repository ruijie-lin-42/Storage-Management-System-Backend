package io.github.ruijie_lin_42.storage_management_system_backend.items.vo;

import lombok.Data;

@Data
public class ItemInfoDetailVo {

    private Long id;
    private String name;
    private String description;
    private String category;
    private String unit;
    private String sku;
    private Integer price;

}
