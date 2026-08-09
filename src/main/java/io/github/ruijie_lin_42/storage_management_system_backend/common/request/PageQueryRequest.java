package io.github.ruijie_lin_42.storage_management_system_backend.common.request;

import lombok.Data;

@Data
public class PageQueryRequest {

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUM = 1;

    private int pageSize = DEFAULT_PAGE_SIZE;
    private int pageNum = DEFAULT_PAGE_NUM;

}
