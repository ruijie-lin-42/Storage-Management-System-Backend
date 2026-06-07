package io.github.ruijie_lin_42.storage_management_system_backend.storage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.entity.Storage;
import io.github.ruijie_lin_42.storage_management_system_backend.storage.vo.StorageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper interface
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-06-05
 */
@Mapper
public interface StorageMapper extends BaseMapper<Storage> {

    Page<StorageVo> selectStorageDetailByName(
            Page<StorageVo> page,
            @Param("keyword") String keyword
    );

}
