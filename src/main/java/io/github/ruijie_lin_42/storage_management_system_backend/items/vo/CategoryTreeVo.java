package io.github.ruijie_lin_42.storage_management_system_backend.items.vo;

import lombok.Data;

import java.util.List;

@Data
public class CategoryTreeVo {

    private Long id;
    private String name;
    private List<CategoryTreeVo> children;

}
