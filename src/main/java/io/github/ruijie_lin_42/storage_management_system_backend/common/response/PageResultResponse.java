package io.github.ruijie_lin_42.storage_management_system_backend.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Pagination configuration in resource queries' responses")
public class PageResultResponse<T> {

    @Schema(description = "Records, list of resources required, empty list if none are found")
    private List<T> records;

    @Schema(description = "Total number of resources found", example = "100")
    private long total;

    @Schema(description = "Page number, the location of this page of data in all pages", example = "10")
    private long pageNum;

    @Schema(description = "Page size, the maximum number of resources are on this page", example = "20")
    private long pageSize;

}
