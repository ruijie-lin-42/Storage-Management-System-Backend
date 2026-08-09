package io.github.ruijie_lin_42.storage_management_system_backend.modules.stock_history.model.response;

import lombok.Data;

import java.time.Instant;

@Data
public class StockHistoryResponse {

    private Long id;
    private Long storageId;
    private String storageName;
    private String createdBy;
    private Instant createdAt;
    private String type;
    private Integer amount;
    private String remark;

}
