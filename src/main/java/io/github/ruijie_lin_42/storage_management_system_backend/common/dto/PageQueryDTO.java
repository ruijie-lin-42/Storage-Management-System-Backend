package io.github.ruijie_lin_42.storage_management_system_backend.common.dto;

import lombok.Data;

@Data
public class PageQueryDTO {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int DEFAULT_PAGE_NUM = 1;

    private int pageSize = DEFAULT_PAGE_SIZE;
    private int pageNum = DEFAULT_PAGE_NUM;

}
