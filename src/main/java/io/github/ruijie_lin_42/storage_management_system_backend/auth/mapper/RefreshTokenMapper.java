package io.github.ruijie_lin_42.storage_management_system_backend.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.ruijie_lin_42.storage_management_system_backend.auth.entity.RefreshToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefreshTokenMapper extends BaseMapper<RefreshToken> {
}
