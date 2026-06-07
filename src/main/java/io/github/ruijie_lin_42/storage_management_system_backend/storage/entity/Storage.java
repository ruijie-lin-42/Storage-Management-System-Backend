package io.github.ruijie_lin_42.storage_management_system_backend.storage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-05
 */
@Getter
@Setter
@ToString
public class Storage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * storage name
     */
    private String name;

    /**
     * location
     */
    private String address;

    /**
     * person in charge, should not be user-level
     */
    private Long managerId;

    /**
     * when the storage is created
     */
    private LocalDateTime createdAt;

    /**
     * person who created the storage
     */
    private Long createdBy;

    /**
     * the last update time of the storage
     */
    private LocalDateTime updatedAt;

    /**
     * the last person updated the storage
     */
    private Long updatedBy;

    /**
     * remark
     */
    private String remark;

    /**
     * whether the storage is deleted
     */
    private Boolean isDeleted;
}
