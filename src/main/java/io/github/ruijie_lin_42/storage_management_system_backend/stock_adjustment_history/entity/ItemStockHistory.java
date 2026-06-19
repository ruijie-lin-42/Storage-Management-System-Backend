package io.github.ruijie_lin_42.storage_management_system_backend.stock_adjustment_history.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2026-06-13
 */
@Getter
@Setter
@ToString
@TableName("item_stock_history")
public class ItemStockHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * which adjustment did the stock change happened
     */
    private Long adjustmentId;

    /**
     * which item is being changed (id)
     */
    private Long itemId;

    /**
     * the stock before the change
     */
    private Integer stockBefore;

    /**
     * the amount of stock being changed
     */
    private Integer amountChange;

    /**
     * the stock after the change
     */
    private Integer stockAfter;
}
