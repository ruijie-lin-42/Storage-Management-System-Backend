package io.github.ruijie_lin_42.storage_management_system_backend.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *  user entity
 * </p>
 *
 * @author ruijie-lin-42
 * @since 2026-05-24
 */
@Getter
@Setter
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * username
     */
    private String username;

    /**
     * full name
     */
    private String name;

    /**
     * password
     */
    private String password;

    /**
     * age
     */
    private Integer age;

    /**
     * gender: male, female, others
     */
    private String gender;

    /**
     * e-mail
     */
    private String email;

    /**
     * role: 0=SUPER_ADMIN，1=ADMIN，2=USER
     */
    private Integer role;

    /**
     * user status: 0=valid, 1=banned
     */
    private Integer status;

    /**
     * if user is deleted
     */
    private Boolean isDeleted;
}
