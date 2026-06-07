package io.github.ruijie_lin_42.storage_management_system_backend.storage.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StorageVo {

    private String name;
    private String address;
    private String manager;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String remark;

}
