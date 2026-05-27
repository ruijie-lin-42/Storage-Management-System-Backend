package io.github.ruijie_lin_42.storage_management_system_backend.user.service;

import io.github.ruijie_lin_42.storage_management_system_backend.common.vo.PageResultVo;
import io.github.ruijie_lin_42.storage_management_system_backend.user.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.ruijie_lin_42.storage_management_system_backend.user.dto.CreateUserDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.user.dto.EditUserDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.user.dto.UserQueryDTO;
import io.github.ruijie_lin_42.storage_management_system_backend.user.vo.UserVo;

/**
 * <p>
 *  user service interface
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-05-24
 */
public interface IUserService extends IService<User> {

    int register(CreateUserDTO dto);
    int editById(EditUserDTO dto, Long id);
    int removeById(Long id);
    PageResultVo<UserVo> queryInPage(UserQueryDTO dto);

}
