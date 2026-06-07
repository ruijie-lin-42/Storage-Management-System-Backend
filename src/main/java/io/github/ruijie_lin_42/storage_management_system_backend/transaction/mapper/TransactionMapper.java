package io.github.ruijie_lin_42.storage_management_system_backend.transaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.ruijie_lin_42.storage_management_system_backend.transaction.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper interface
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-05
 */
@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {

}
