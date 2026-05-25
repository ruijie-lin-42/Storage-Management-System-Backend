package io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{

    private final ResultCode code;

    public ApiException(ResultCode resultCode){
        super(resultCode.getMessage());
        this.code = resultCode;
    }

}
