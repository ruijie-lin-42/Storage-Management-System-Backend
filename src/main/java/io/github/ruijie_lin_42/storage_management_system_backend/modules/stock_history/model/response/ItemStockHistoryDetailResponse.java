package io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.response;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.StockAdjustmentType;
import lombok.Data;

import java.time.Instant;

@Data
public class ItemStockHistoryDetailResponse {

    private Long id;
    private Long stockHistoryId;
    private Integer stockBefore;
    private Integer amountChange;
    private Integer stockAfter;
    private String storageName;
    private Instant createdAt;
    private String operatorName;
    private StockAdjustmentType type;
    private Integer amount;
    private String remark;

}
