package io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import lombok.Getter;

@Getter
public class DataIntegrityException extends CustomException{

    private final ResultCode code;

    public DataIntegrityException(ResultCode resultCode){
        super(resultCode);
        this.code = resultCode;
    }

}
