package io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import lombok.Getter;

import java.util.Map;

@Getter
public abstract class CustomException extends RuntimeException {

    private final ResultCode resultCode;
    private final Map<String, Object> context;

    protected CustomException(ResultCode resultCode, Map<String, Object> context){
        super(resultCode.getMessage());
        this.resultCode = resultCode;
        this.context = context;
    }

}
