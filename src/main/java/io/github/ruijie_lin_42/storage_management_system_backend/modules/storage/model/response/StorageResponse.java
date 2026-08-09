package io.github.ruijie_lin_42.storage_management_system_backend.modules.storage.model.response;

import lombok.Data;

import java.time.Instant;

@Data
public class StorageResponse {

    private Long id;
    private String name;
    private String address;
    private Long managerId;
    private String managerName;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String remark;

}
