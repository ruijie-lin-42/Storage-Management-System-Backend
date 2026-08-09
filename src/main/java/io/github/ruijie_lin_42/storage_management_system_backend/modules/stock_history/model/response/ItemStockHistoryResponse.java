package io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.response;

import lombok.Data;

@Data
public class ItemStockHistoryResponse {

    private Long id;
    private Long itemId;
    private String itemName;
    private Integer stockBefore;
    private Integer amountChange;
    private Integer stockAfter;

}
