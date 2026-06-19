package io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockAdjustmentHistoryVo {

    private Long id;
    private Long storageId;
    private String storageName;
    private String createdBy;
    private LocalDateTime createdAt;
    private String type;
    private Integer amount;
    private String remark;

}
