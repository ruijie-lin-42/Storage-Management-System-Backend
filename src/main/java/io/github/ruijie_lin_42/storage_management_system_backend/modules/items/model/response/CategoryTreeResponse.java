package io.github.ruijie_lin_42.storage_management_system_backend.modules.items.model.response;

import lombok.Data;

import java.util.List;

@Data
public class CategoryTreeResponse {

    private Long id;
    private String name;
    private List<CategoryTreeResponse> children;

}
