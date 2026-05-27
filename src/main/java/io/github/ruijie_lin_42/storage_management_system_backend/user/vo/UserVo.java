package io.github.ruijie_lin_42.storage_management_system_backend.user.vo;

import lombok.Data;

@Data
public class UserVo {
    private Long id;
    private String username;
    private String name;
    private Integer age;
    private String gender;
    private String email;
    private Integer role;
    private Integer status;
}
