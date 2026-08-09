package io.github.ruijie_lin_42.storage_management_system_backend.modules.user.model.response;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.Role;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.Status;
import lombok.Data;

@Data
public class UserQueryResponse {
    private Long id;
    private String username;
    private String name;
    private Integer age;
    private String gender;
    private String email;
    private Role role;
    private Status status;
}
