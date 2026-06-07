package io.github.ruijie_lin_42.storage_management_system_backend.transaction.service;

import io.github.ruijie_lin_42.storage_management_system_backend.transaction.entity.Transaction;
import io.github.ruijie_lin_42.storage_management_system_backend.transaction.mapper.TransactionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  service
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-05
 */
@Service
public class TransactionService extends ServiceImpl<TransactionMapper, Transaction> {

}
