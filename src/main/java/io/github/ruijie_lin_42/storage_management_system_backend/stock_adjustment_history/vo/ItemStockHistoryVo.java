package io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.vo;

import lombok.Data;

@Data
public class ItemStockHistoryVo {

    private Long id;
    private Long itemId;
    private String itemName;
    private Integer stockBefore;
    private Integer amountChange;
    private Integer stockAfter;

}
