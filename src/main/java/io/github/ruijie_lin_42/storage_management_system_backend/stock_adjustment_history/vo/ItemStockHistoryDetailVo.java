package io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.vo;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.StockAdjustmentType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemStockHistoryDetailVo {

    private Long id;
    private Long adjustmentId;
    private Integer stockBefore;
    private Integer amountChange;
    private Integer stockAfter;
    private String storageName;
    private LocalDateTime adjustmentTime;
    private String operatorName;
    private StockAdjustmentType type;
    private Integer amount;
    private String remark;

}
