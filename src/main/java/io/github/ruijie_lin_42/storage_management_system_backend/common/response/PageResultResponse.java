package io.github.ruijie_lin_42.storage_management_system_backend.common.response;

import lombok.Data;

import java.util.List;

@Data
public class PageResultResponse<T> {

    private List<T> records;
    private long total;
    private long pageNum;
    private long pageSize;

}
