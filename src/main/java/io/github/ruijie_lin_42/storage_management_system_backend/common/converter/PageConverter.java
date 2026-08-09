package io.github.ruijie_lin_42.storage_management_system_backend.common.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.ruijie_lin_42.storage_management_system_backend.common.response.PageResultResponse;

import java.util.function.Function;

public class PageConverter {

    public static <T, R> PageResultResponse<R> convert(
            Page<T> page,
            Function<T, R> mapper
    ) {
        PageResultResponse<R> result = new PageResultResponse<>();
        result.setRecords(page.getRecords().stream().map(mapper).toList());
        result.setPageSize(page.getSize());
        result.setPageNum(page.getCurrent());
        result.setTotal(page.getTotal());
        return result;
    }

}
