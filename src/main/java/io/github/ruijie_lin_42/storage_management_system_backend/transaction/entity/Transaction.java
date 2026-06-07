package io.github.ruijie_lin_42.storage_management_system_backend.transaction.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

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
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * storage operated
     */
    private Long storageId;

    /**
     * operator's id
     */
    private Long operatorId;

    /**
     * item's id
     */
    private Long itemId;

    /**
     * operation type, IN or OUT
     */
    private String type;

    /**
     * number of items operated
     */
    private Integer amount;
}
