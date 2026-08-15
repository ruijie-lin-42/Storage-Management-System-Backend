package io.github.ruijie_lin_42.storage_management_system_backend.common.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Pagination configuration in resource query with pagination requests")
public class PageQueryRequest {

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUM = 1;

    @Schema(description = "Page size, default" + DEFAULT_PAGE_SIZE, example = "10")
    private int pageSize = DEFAULT_PAGE_SIZE;

    @Schema(description = "Page number, default" + DEFAULT_PAGE_NUM, example = "1")
    private int pageNum = DEFAULT_PAGE_NUM;

}
